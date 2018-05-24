package io.github.slavmetal;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame window = new JFrame();

        // Make sure app looks as a native one
        JFrame.setDefaultLookAndFeelDecorated(true);

        window.setTitle("Memory Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setContentPane(new GamePanel());

        window.setResizable(false);
        window.setLocationRelativeTo(null);
        window.pack();
        window.setVisible(true);
    }
}
