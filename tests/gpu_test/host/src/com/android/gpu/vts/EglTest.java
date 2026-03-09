/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.gpu.vts;

import static com.android.gpu.vts.Util.mustChipsetMeetGrfRequirement;
import static com.android.gpu.vts.Util.mustNotBeEssentialTierChipset;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import com.android.compatibility.common.util.ApiLevelUtil;
import com.android.compatibility.common.util.VsrTest;
import com.android.tradefed.log.LogUtil;
import com.android.tradefed.testtype.DeviceJUnit4ClassRunner;
import com.android.tradefed.testtype.junit4.BaseHostJUnit4Test;
import com.android.tradefed.util.RunUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

/*
 * VTS test for EGL requirements.
 */
@RunWith(DeviceJUnit4ClassRunner.class)
public class EglTest extends BaseHostJUnit4Test {
    private static final String TAG = "EglTest";

    public static final String GPU_TEST_PKG = "com.google.android.gpu.vts.testapp";
    public static final String GPU_TEST_APP = "VtsGpuTestApp.apk";
    public static final String GPU_TEST_CLASS = "VtsGpuTestCase";
    public static final String GPU_TEST_LOCATION_METHOD = "checkEglContextPrioritySupport";

    // Package install attempts and intervals before install retries
    private static final int NUM_ATTEMPTS = 5;
    private static final int APP_INSTALL_REATTEMPT_SLEEP_MSEC = 500;
    private static final int VK_PHYSICAL_DEVICE_TYPE_CPU = 4;

    // Object that invokes adb commands and interacts with test devices
    private Helper mTestHelper;
    private JSONObject[] mVulkanDevices;

    @Rule public final TemporaryFolder mTemporaryFolder = new TemporaryFolder();

    @Rule
    public final DeviceJUnit4ClassRunner.TestMetrics mMetrics =
            new DeviceJUnit4ClassRunner.TestMetrics();

    @Rule
    public final DeviceJUnit4ClassRunner.TestLogData mLogData =
            new DeviceJUnit4ClassRunner.TestLogData();

    @Rule public final TestName mTestName = new TestName();

    @Before
    public void setUp() throws Exception {
        // Instantiate a Helper object, which also calls Helper.preTestSetup()
        // that sets the device ready for tests.
        // Helper object needs to be instantiated first, before assumption check, because
        // the uninstallTestApps() in tearDown() needs the Helper object.
        mTestHelper = new Helper(getTestInformation(), mTemporaryFolder, mMetrics, mLogData,
                mTestName.getMethodName());

        final String output = getDevice().executeShellCommand("cmd gpu vkjson");
        final JSONArray vkjson;
        // vkjson output in Android N is JSONArray.
        if (ApiLevelUtil.isBefore(getDevice(), Build.OC)) {
            vkjson = (new JSONArray(output));
        } else {
            vkjson = (new JSONObject(output)).getJSONArray("devices");
        }
        mVulkanDevices = new JSONObject[vkjson.length()];
        for (int i = 0; i < vkjson.length(); i++) {
            mVulkanDevices[i] = vkjson.getJSONObject(i);
        }
    }

    @After
    public void tearDown() throws Exception {
        if (mTestHelper != null) {
            uninstallTestApps();
        }
    }

    /**
     * Invokes BaseHostJUnit4Test installPackage() API, with NUM_ATTEMPTS of retries
     * Difference between this function and Helper.installApkFile() is this function can only
     * install apks that exist in the same test module (e.g. apks that are specified under
     * device_common_data or data field in Android.bp), while installApkFile() can install apks from
     * any directory.
     */
    private void installTestApp(String appName) throws Exception {
        for (int i = 0; i < NUM_ATTEMPTS; i++) {
            try {
                installPackage(appName);
                return;
            } catch (Exception e) {
                LogUtil.CLog.e("Exception in installing the app: %s, error message: %s", appName,
                        e.getMessage());
                if (i < NUM_ATTEMPTS - 1) {
                    RunUtil.getDefault().sleep(
                            (long) Math.pow(2, i) * APP_INSTALL_REATTEMPT_SLEEP_MSEC);
                } else {
                    throw e;
                }
            }
        }
    }

    private void uninstallTestApps() throws CommandException {
        mTestHelper.uninstallAppIgnoreErrors(GPU_TEST_PKG);
    }

    /**
     * SoCs meeting certain requirements must support EGL_IMG_context_priority and
     * EGL_EXT_protected_content extensions.
     */
    @VsrTest(requirements = {"VSR-3.2.2-008"})
    @Test
    public void checkEglContextPrioritySupport() throws Throwable {
        if (hasOnlyCpuDevice()) {
            return;
        }

        assumeTrue(mustChipsetMeetGrfRequirement(getDevice(), Build.VENDOR_26Q2));
        assumeTrue(mustNotBeEssentialTierChipset(getDevice()));
        assumeTrue("Android17 Graphics requirements for handheld devices & PC",
                Util.isHandheld(getDevice()) || Util.isPC(getDevice()));

        installTestApp(GPU_TEST_APP);

        runDeviceTests(GPU_TEST_PKG, GPU_TEST_PKG + "." + GPU_TEST_CLASS, GPU_TEST_LOCATION_METHOD);
    }

    private boolean hasOnlyCpuDevice() throws Exception {
        for (JSONObject device : mVulkanDevices) {
            if (device.getJSONObject("properties").getInt("deviceType")
                    != VK_PHYSICAL_DEVICE_TYPE_CPU) {
                return false;
            }
        }
        return true;
    }
}
