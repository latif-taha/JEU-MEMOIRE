package io.github.slavmetal;

import org.apache.commons.dbutils.DbUtils;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Statement;
import java.sql.Time;
import java.util.Objects;
import java.util.Properties;

/**
 * Shows modal dialog to input player's
 * nickname and write his score to the DB.
 */
class NicknameDialog extends JDialog {
    NicknameDialog(Time playTime, int boardSize, int score) {
        DbConnection dbConnection = new DbConnection();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        Properties prop = new Properties();
        InputStream input;

        try {
            input = new FileInputStream(Objects.requireNonNull(classLoader.getResource("config.properties")).getFile());
            prop.load(input);
        } catch (IOException e){
            e.printStackTrace();
        }

        // Components of the dialog
        JPanel nickPanel = new JPanel();
        nickPanel.setLayout(new BoxLayout(nickPanel, BoxLayout.Y_AXIS));

        JTextField nickField = new JTextField();
        nickField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
                if (nickField.getText().length() >= Integer.parseInt(prop.getProperty("maxnicklength")))
                    // Just say we got this request but do nothing
                    keyEvent.consume();
            }
        });

        JButton button = new JButton("OK");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Statement statement = dbConnection.getConnection().createStatement();

                    statement.executeUpdate("INSERT INTO SCORES (NICKNAME, PLAYTIME, BOARDSIZE, SCORE) VALUES (" +
                            "'"+nickField.getText().replaceAll("'", "").trim()+"', " +
                            "'"+playTime+"', " +
                            "'"+boardSize+"', " +
                            "'"+((score < 0) ? 0 : score)+"')"
                    );

                    DbUtils.close(statement);
                    DbUtils.close(dbConnection.getConnection());

                    Logger.info("Score added to the DB");
                } catch (Exception e){
                    e.printStackTrace();
                }
                dispose();
            }
        });

        // Create 'padding' from the top
        nickPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Use temp panel to use elements' preferred size on the BoxLayout
        JPanel tempPanel = new JPanel();
        tempPanel.add(new JLabel("Input your nickname:"));
        nickPanel.add(tempPanel);

        tempPanel = new JPanel();
        nickField.setPreferredSize(new Dimension(250, 30));
        tempPanel.add(nickField);
        nickPanel.add(tempPanel);

        tempPanel = new JPanel();
        tempPanel.add(button);
        nickPanel.add(tempPanel);

        // Some parameters
        setContentPane(nickPanel);
        setSize(new Dimension(300, 190));
        setLocationRelativeTo(null);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
