/*
 * MIT License
 *
 * Copyright (c) 2021 Over-Run
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.mc2dlauncher.util;

import java.util.function.Function;

/**
 * @author squid233
 * @since 2021/02/07
 */
public enum OperatingSystem {
    /**
     * The operation system.
     */
    WINDOWS(osArch -> System.getProperty("os.arch").contains("64") ? "windows" : "windows-x86"),
    LINUX(osArch -> osArch.startsWith("arm") || osArch.startsWith("aarch64")
            ? "linux-" + (osArch.contains("64") || osArch.startsWith("armv8") ? "arm64" : "arm32")
            : "linux"),
    MACOS(osArch -> "macos");

    public static final OperatingSystem CURRENT;
    private final Function<String, String> nativeName;

    static {
        String osName = System.getProperty("os.name");
        //noinspection AlibabaUndefineMagicConstant
        if (osName.startsWith("Windows")) {
            CURRENT = WINDOWS;
        } else //noinspection AlibabaUndefineMagicConstant
            if (osName.startsWith("Linux") || osName.startsWith("FreeBSD") || osName.startsWith("SunOS") || osName.startsWith("Unix")) {
                CURRENT = LINUX;
            } else //noinspection AlibabaUndefineMagicConstant
                if (osName.startsWith("Mac OS X") || osName.startsWith("Darwin")) {
                    CURRENT = MACOS;
                } else {
                    CURRENT = null;
                }
    }

    OperatingSystem(Function<String, String> nativeName) {
        this.nativeName = nativeName;
    }

    public String getNativeName() {
        return nativeName.apply(System.getProperty("os.arch"));
    }
}
