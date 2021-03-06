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

import io.github.overrun.mc2dlauncher.Main;
import io.github.overrun.mc2dlauncher.util.Libraries;
import io.github.overrun.mc2dlauncher.util.OperatingSystem;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static io.github.overrun.mc2dlauncher.Main.RESOURCE_BUNDLE;
import static io.github.overrun.mc2dlauncher.screen.DownloadScreen.LIB_GSON;

/**
 * @author squid233
 * @since 2021/02/06
 */
public final class TitleScreen extends JPanel {
    private final JComboBox<String> box = new JComboBox<>();

    public TitleScreen() {
        refresh();
        JScrollPane scrollPane = new JScrollPane();
        JTextArea area = new JTextArea(1, 90);
        area.setToolTipText("VM options");
        if (Main.getLauncherJson().getVmOptions() != null) {
            area.setText(Main.getLauncherJson().getVmOptions());
        }
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        scrollPane.setViewportView(area);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JButton play = new JButton(RESOURCE_BUNDLE.getString("play"));
        play.addActionListener(e -> {
            try {
                Libraries libraries;
                try (Reader r = new BufferedReader(new FileReader(".mc2d/versions/" + box.getSelectedItem() + "/" + box.getSelectedItem() + ".json"))) {
                    libraries = LIB_GSON.fromJson(r, Libraries.class);
                }
                List<String> libs = new ArrayList<>(libraries.getLibs().length);
                for (String lib : libraries.getLibs()) {
                    libs.add("../../libraries/" + lib + ".jar");
                    if (lib.contains("lwjgl")) {
                        libs.add("../../libraries/" + lib + "-natives-" + OperatingSystem.CURRENT.getNativeName() + ".jar");
                    }
                }
                String[] cmdArray = new String[5];
                String[] arr = libs.toArray(new String[0]);
                cmdArray[0] = "java";
                cmdArray[1] = "-cp";
                cmdArray[2] = "\"";
                for (String s : arr) {
                    cmdArray[2] += s + File.pathSeparator;
                }
                cmdArray[2] += box.getSelectedItem() + ".jar\"";
                cmdArray[3] = area.getText();
                cmdArray[4] = "io.github.overrun.mc2d.Main";
                Process process = exec(".mc2d/versions/" + box.getSelectedItem(), cmdArray);
                InputStream stdout = process.getInputStream(), stderr = process.getErrorStream();
                try (InputStreamReader isr = new InputStreamReader(stdout);
                     BufferedReader br = new BufferedReader(isr);
                     InputStreamReader errIsr = new InputStreamReader(stderr);
                     BufferedReader errBr = new BufferedReader(errIsr)) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                    while ((line = errBr.readLine()) != null) {
                        System.err.println(line);
                    }
                }
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        });
        play.setSize(play.getWidth() << 1, play.getHeight() << 1);
        add(play);
        add(box);
        JButton refresh = new JButton(RESOURCE_BUNDLE.getString("refresh"));
        refresh.addActionListener(e -> refresh());
        add(refresh);
        add(new JLabel("Recommend use the command prompt to open this launcher"));
        add(scrollPane, BorderLayout.CENTER);
    }

    public static Process exec(String dir, String... cmdArray) throws IOException {
        return Runtime.getRuntime().exec(cmdArray, null, new File(dir));
    }

    public void refresh() {
        box.removeAllItems();
        for (String ver : Main.getLauncherJson().getVersions()) {
            box.addItem(ver);
        }
    }
}
