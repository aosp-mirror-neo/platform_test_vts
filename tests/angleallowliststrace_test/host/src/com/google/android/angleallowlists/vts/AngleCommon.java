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

import com.android.tradefed.device.ITestDevice;
import java.util.HashMap;
import java.util.Map;

public class AngleCommon {
    // Settings.Global
    public static final String SETTINGS_GLOBAL_ALL_USE_ANGLE = "angle_gl_driver_all_angle";
    public static final String SETTINGS_GLOBAL_DRIVER_PKGS = "angle_gl_driver_selection_pkgs";
    public static final String SETTINGS_GLOBAL_DRIVER_VALUES = "angle_gl_driver_selection_values";
    public static final String SETTINGS_GLOBAL_ANGLE_DEBUG_PACKAGE = "angle_debug_package";

    // ANGLE
    public static final String ANGLE_TEST_PKG = "com.google.android.vts.angle.testapp";
    public static final String ANGLE_TEST_APP = "VtsAngleTestApp.apk";

    public static final String ANGLE_DRIVER_TEST_CLASS = "VtsAngleTestCase";
    public static final String ANGLE_DRIVER_TEST_LOCATION_METHOD = "testAngleLocation";

    public static final Map<String, String> AngleAllowlistApps = new HashMap<>();

    // allow list apps are defined per GMS requirement: b/369880861
    static {
        AngleAllowlistApps.put("com.dreamgames.royalmatch", "royal_match");
        AngleAllowlistApps.put("com.dts.freefiremax", "free_fire_max");
        AngleAllowlistApps.put("com.dxx.firenow", "survivor_io");
        AngleAllowlistApps.put("com.gramgames.mergedragons", "merge_dragons");
        AngleAllowlistApps.put("com.ludo.king", "ludo_king");
        AngleAllowlistApps.put("com.mojang.minecraftpe", "minecraft_bedrock");
        AngleAllowlistApps.put("com.my.defense", "rush_royale");
        AngleAllowlistApps.put("com.nianticlabs.pokemongo", "pokemon_go");
        AngleAllowlistApps.put("com.nintendo.zaka", "mario_kart_tour");
        AngleAllowlistApps.put("com.os.airforce", "1945_air_force");
        AngleAllowlistApps.put("com.playrix.fishdomdd.gplay", "fishdom");
        AngleAllowlistApps.put("io.teslatech.callbreak", "callbreak");
        AngleAllowlistApps.put("jp.konami.prospia", "professional_baseball_spirits");
        AngleAllowlistApps.put("net.peakgames.toonblast", "toon_blast");
    }

    static void setGlobalSetting(ITestDevice device, String globalSetting, String value)
            throws Exception {
        device.setSetting("global", globalSetting, value);
        device.executeShellCommand("am refresh-settings-cache");
    }

    /** Clear ANGLE-related settings */
    public static void clearSettings(ITestDevice device) throws Exception {
        // Cached Activity Manager settings
        setGlobalSetting(device, SETTINGS_GLOBAL_ALL_USE_ANGLE, "0");
        setGlobalSetting(device, SETTINGS_GLOBAL_DRIVER_PKGS, "\"\"");
        setGlobalSetting(device, SETTINGS_GLOBAL_DRIVER_VALUES, "\"\"");
        setGlobalSetting(device, SETTINGS_GLOBAL_ANGLE_DEBUG_PACKAGE, "\"\"");
    }
}
