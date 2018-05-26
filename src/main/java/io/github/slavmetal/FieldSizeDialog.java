package io.github.slavmetal;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

/**
 * Shows modal dialog to input size of the board.
 */
class FieldSizeDialog extends JDialog {
    private int size;

    FieldSizeDialog(int defaultSize) {
        size = defaultSize;
        // Components of the dialog
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(defaultSize, 2, 20, 2));
        JButton button = new JButton("OK");
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));

        // TODO Focus on OK button

        button.addActionListener(actionEvent -> {
            try{
                // Check if current value is OK
                // If not, last correct value will be used
                spinner.commitEdit();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            size = (Integer) spinner.getValue();    // Set new size of the board
            dispose();                              // Close the dialog
        });

        // Use temp panel to use elements' preferred size on the gamePanel
        JPanel tempPanel = new JPanel();
        tempPanel.add(new JLabel("Select grid size:"));
        tempPanel.add(spinner);
        sizePanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(button);
        sizePanel.add(tempPanel);

        // Some parameters
        setContentPane(sizePanel);
        setSize(new Dimension(300, 190));
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    int getChosenSize() {
        return size;
    }
}
