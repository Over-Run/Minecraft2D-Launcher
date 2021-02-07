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

package io.github.overrun.mc2dlauncher.screen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.overrun.mc2dlauncher.Main;
import io.github.overrun.mc2dlauncher.util.Libraries;
import io.github.overrun.mc2dlauncher.util.Mc2dVersion;
import io.github.overrun.mc2dlauncher.util.NetworkHelper;
import io.github.overrun.mc2dlauncher.util.OperatingSystem;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import static io.github.overrun.mc2dlauncher.Main.RESOURCE_BUNDLE;

/**
 * @author squid233
 * @since 2021/02/06
 */
public final class DownloadScreen extends JPanel {
    public static final String NATIVES_PLATFORM = OperatingSystem.CURRENT.getNativeName();
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(Mc2dVersion.class, new Mc2dVersion.Serializer())
            .create();
    public static final Gson LIB_GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(Libraries.class, new Libraries.Serializer())
            .create();
    private final JComboBox<String> versionsBox = new JComboBox<>();
    private Mc2dVersion mc2dVersion = null;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public DownloadScreen() {
        add(versionsBox);
        JButton button = new JButton(RESOURCE_BUNDLE.getString("download.download"));
        button.addActionListener(e -> {
            File dir = new File(".mc2d");
            if (!dir.exists()) {
                dir.mkdir();
            }
            dir = new File(".mc2d/versions");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            dir = new File(".mc2d/libraries");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                JOptionPane.showMessageDialog(null, RESOURCE_BUNDLE.getString("download.ready"));
                String ver = String.valueOf(versionsBox.getSelectedItem());
                dir = new File(".mc2d/versions/" + ver);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String versionJar = ".mc2d/versions/" + ver + "/" + ver + ".jar";
                if (!new File(versionJar).exists()) {
                    NetworkHelper.download(new URL("https://over-run.github.io/mc2d/version/" + mc2dVersion.getMap().get(ver) + ".jar"),
                            versionJar);
                    try (Writer w = new FileWriter("launcher.json")) {
                        Main.getLauncherJson().getVersions().add(ver);
                        w.write(GSON.toJson(Main.getLauncherJson()));
                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }
                }
                String versionJson = ".mc2d/versions/" + ver + "/" + ver + ".json";
                if (!new File(versionJson).exists()) {
                    NetworkHelper.download(new URL("https://over-run.github.io/mc2d/version/" + mc2dVersion.getMap().get(ver) + ".json"),
                            versionJson);
                }
                Libraries libraries;
                try (Reader r = new BufferedReader(new FileReader(versionJson))) {
                    libraries = LIB_GSON.fromJson(r, Libraries.class);
                }
                for (String lib : libraries.getLibs()) {
                    String libJar = ".mc2d/libraries/" + lib + ".jar";
                    if (!new File(libJar).exists()) {
                        try {
                            NetworkHelper.download(new URL("https://over-run.github.io/mc2d/libs/" + lib + ".jar"),
                                    libJar);
                        } catch (Throwable t) {
                            t.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Download failed. " + t.getMessage());
                        }
                    }
                    String nativeLibJar = ".mc2d/libraries/" + lib + "-natives-" + NATIVES_PLATFORM + ".jar";
                    if (lib.contains("lwjgl") && !new File(nativeLibJar).exists()) {
                        try {
                            NetworkHelper.download(new URL("https://over-run.github.io/mc2d/libs/" + lib + "-natives-" + NATIVES_PLATFORM + ".jar"),
                                    nativeLibJar);
                        } catch (Throwable t) {
                            t.printStackTrace();
                            JOptionPane.showMessageDialog(null, String.format(RESOURCE_BUNDLE.getString("download.failed"), t.getMessage()));
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
                JOptionPane.showMessageDialog(null, String.format(RESOURCE_BUNDLE.getString("download.failed"), t.getMessage()));
            }
        });
        add(button);
        JButton refresh = new JButton(RESOURCE_BUNDLE.getString("refresh"));
        refresh.addActionListener(e -> refresh());
        add(refresh);
        refresh();
    }

    public void refresh() {
        try {
            mc2dVersion = GSON.fromJson(NetworkHelper.read(new URL("https://over-run.github.io/mc2d/version_manifest.json")), Mc2dVersion.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (mc2dVersion == null) {
            mc2dVersion = new Mc2dVersion();
        }
        versionsBox.removeAllItems();
        for (String version : mc2dVersion.getMap().keySet()) {
            versionsBox.addItem(version);
        }
    }
}
