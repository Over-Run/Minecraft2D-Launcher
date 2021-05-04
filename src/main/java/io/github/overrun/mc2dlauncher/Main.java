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

package io.github.overrun.mc2dlauncher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.overrun.mc2dlauncher.screen.ConsoleScreen;
import io.github.overrun.mc2dlauncher.screen.DownloadScreen;
import io.github.overrun.mc2dlauncher.screen.TitleScreen;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ResourceBundle;

/**
 * @author squid233
 * @since 2021/02/06
 */
public final class Main {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .registerTypeAdapter(LauncherJson.class, new LauncherJson.Serializer())
            .create();
    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("texts");
    public static final ConsoleScreen CONSOLE_SCREEN = new ConsoleScreen();
    private static LauncherJson launcherJson;

    public static LauncherJson getLauncherJson() {
        return launcherJson;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(System.getProperty("java.version").split("\\.")[0]) < 11) {
            JOptionPane.showMessageDialog(null,
                    RESOURCE_BUNDLE.getString("java11.warning.text"),
                    RESOURCE_BUNDLE.getString("java11.warning.title"),
                    JOptionPane.WARNING_MESSAGE);
        }
        File file = new File("launcher.json");
        if (file.exists()) {
            try (Reader r = new FileReader(file)) {
                launcherJson = GSON.fromJson(r, LauncherJson.class);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (Writer w = new FileWriter(file)) {
                launcherJson = new LauncherJson();
                launcherJson.setGameCoreSrc("https://over-run.github.io/mc2d/version/");
                launcherJson.setLibSrc("https://repo1.maven.org/maven2/");
                w.write(GSON.toJson(launcherJson));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JFrame frame = new JFrame("Minecraft2D Launcher");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Home", new TitleScreen());
        tabbedPane.addTab(RESOURCE_BUNDLE.getString("download.download"), new DownloadScreen());
        tabbedPane.addTab(RESOURCE_BUNDLE.getString("screen.console"), CONSOLE_SCREEN);
        frame.getContentPane().add(tabbedPane);
        frame.setVisible(true);
    }
}
