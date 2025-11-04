/*
 * Copyright (C) 2025 The Android Open Source Project
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
package com.android.ggi.vts;

import static org.junit.Assert.assertFalse;
import static org.junit.Assume.assumeFalse;

import com.android.compatibility.common.util.VsrTest;
import com.android.tradefed.testtype.DeviceJUnit4ClassRunner;
import com.android.tradefed.testtype.junit4.BaseHostJUnit4Test;
import com.google.common.base.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/*
 * VTS test for GGI requirements.
 */
@RunWith(DeviceJUnit4ClassRunner.class)
public class VtsGgiTests extends BaseHostJUnit4Test {
    /**
     * GGI tests applied for devices that set ro.ggi.version property
     */
    @Before
    public void setUp() throws Exception {
        final String ggiVersion = getDevice().getProperty("ro.ggi.version");
        assumeFalse(Strings.isNullOrEmpty(ggiVersion));
    }

    @VsrTest(requirements = {"VSR-5.1-007"})
    @Test
    public void vulkanDriverLoadedFromApex() throws Exception {
        final String vulkanApex = getDevice().getProperty("ro.vulkan.apex");
        assertFalse(Strings.isNullOrEmpty(vulkanApex));
    }
}
