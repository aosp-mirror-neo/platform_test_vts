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
package com.google.android.angleallowlists.vts;

import com.android.compatibility.common.util.FeatureUtil;
import com.android.tradefed.device.DeviceNotAvailableException;
import com.android.tradefed.device.ITestDevice;

public class AngleTestDeviceCheck {
    public static final String FEATURE_PC = "android.hardware.type.pc";

    public static boolean isHandheld(final ITestDevice device) throws DeviceNotAvailableException {
        return !FeatureUtil.isTV(device) && !FeatureUtil.isWatch(device)
                && !FeatureUtil.isAutomotive(device) && !FeatureUtil.isXrHeadset(device)
                && !FeatureUtil.hasSystemFeature(device, FEATURE_PC);
    }
}
