package io.github.slavmetal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class AboutDialog extends JDialog {
    AboutDialog() throws URISyntaxException {
        // Components of the dialog
        JButton button = new JButton("OK");
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));

        // TODO Focus on OK button
        // Close dialog oon button press
        button.addActionListener(actionEvent -> dispose());

        // Create 'padding' from the top
        aboutPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Use temp panel to use elements' preferred size on the gamePanel
        JPanel tempPanel = new JPanel();
        tempPanel.add(new JLabel("Personal data goes here"));
        aboutPanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(new JLabel("MIT License"));
        aboutPanel.add(tempPanel);

        tempPanel = new JPanel();
        URI uri = new URI("https://git.io/vhqVi");
        JLabel txtlabel = new JLabel("Source code: ");
        JLabel linkLabel = new JLabel("<html><a href="+uri+">"+uri+"</a><html>");
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (Desktop.isDesktopSupported()){
                    try {
                        Desktop.getDesktop().browse(uri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        tempPanel.add(txtlabel);
        tempPanel.add(linkLabel);
        aboutPanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(button);
        aboutPanel.add(tempPanel);

        // Some parameters
        setContentPane(aboutPanel);
        setSize(new Dimension(300, 190));
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
