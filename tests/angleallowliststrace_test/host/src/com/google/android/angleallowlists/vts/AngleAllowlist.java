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

import java.util.HashMap;
import java.util.Map;

// allowlist apps are defined per GMS requirement: b/369880861
public class AngleAllowlist {
    public static final Map<String, String> apps = new HashMap<>();
    static {
        apps.put("com.dreamgames.royalmatch", "royal_match");
        apps.put("com.dts.freefiremax", "free_fire_max");
        apps.put("com.dxx.firenow", "survivor_io");
        apps.put("com.gramgames.mergedragons", "merge_dragons");
        apps.put("com.ludo.king", "ludo_king");
        apps.put("com.mojang.minecraftpe", "minecraft_bedrock");
        apps.put("com.my.defense", "rush_royale");
        apps.put("com.nintendo.zaka", "mario_kart_tour");
        apps.put("com.os.airforce", "1945_air_force");
        apps.put("com.playrix.fishdomdd.gplay", "fishdom");
        apps.put("io.teslatech.callbreak", "callbreak");
        apps.put("jp.konami.prospia", "professional_baseball_spirits");
        apps.put("net.peakgames.toonblast", "toon_blast");
    }
}
