package io.github.overrun.mc2dlauncher.screen;

import javax.swing.*;

/**
 * @author squid233
 * @since 2021/04/13
 */
public final class ConsoleScreen extends JPanel {
    private final JTextArea textArea = new JTextArea();

    public ConsoleScreen() {
        textArea.setEditable(false);
        add(new JScrollPane(textArea));
    }

    public JTextArea getTextArea() {
        return textArea;
    }
}
