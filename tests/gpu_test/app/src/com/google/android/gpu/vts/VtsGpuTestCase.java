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

package com.google.android.gpu.vts.testapp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.util.Log;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class VtsGpuTestCase {
    private static final String TAG = VtsGpuTestCase.class.getSimpleName();

    private static final String EGL_IMG_CONTEXT_PRIORITY_NAME = "EGL_IMG_context_priority";
    private static final String EGL_EXT_PROTECTED_CONTENT_NAME = "EGL_EXT_protected_content";

    /**
     * SoCs meeting certain requirements must support EGL_IMG_context_priority and
     * EGL_EXT_protected_content extensions.
     */
    @Test
    public void checkEglContextPrioritySupport() throws Exception {
        int error;

        // Get and initialize the display
        EGLDisplay display = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);

        if (display.equals(EGL14.EGL_NO_DISPLAY)) {
            throw new RuntimeException("no EGL display");
        }
        error = EGL14.eglGetError();
        if (error != EGL14.EGL_SUCCESS) {
            throw new RuntimeException("eglGetDisplay failed");
        }

        int[] version = new int[2];
        if (!EGL14.eglInitialize(display, version, 0, version, 0)) {
            throw new RuntimeException("error in eglInitialize");
        }
        error = EGL14.eglGetError();
        if (error != EGL14.EGL_SUCCESS) {
            throw new RuntimeException("eglInitialize failed");
        }

        // Query and print extensions
        final String extensions = EGL14.eglQueryString(display, EGL14.EGL_EXTENSIONS);
        Log.i(TAG, "EGL Extensions: " + extensions);

        // Clean up
        EGL14.eglTerminate(display);

        assertNotNull(extensions);
        assertTrue("EGL Extensions should not be empty", extensions.length() > 0);
        assertTrue("EGL_IMG_context_priority must be supported.",
                extensions.contains(EGL_IMG_CONTEXT_PRIORITY_NAME));
        assertTrue("EGL_EXT_protected_content must be supported.",
                extensions.contains(EGL_EXT_PROTECTED_CONTENT_NAME));
    }
}
