package io.github.slavmetal;

import org.apache.commons.dbutils.DbUtils;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * Game's highscores table.
 */
class ScoreState extends JPanel implements GameState {
    private Properties prop = new Properties();
    private InputStream input = null;

    private Connection connection;
    private Statement statement;

    /**
     * Creates a table if it doesn't exist.
     */
    ScoreState() {
        try {
            input = new FileInputStream("config.properties");
            prop.load(input);

            Class.forName(prop.getProperty("dbdriver"));
            connection = DriverManager.getConnection(
                    prop.getProperty("dbconnection"),
                    prop.getProperty("dbuser"),
                    prop.getProperty("dbpassword"));
            statement = connection.createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS SCORES " +
                    "(NICKNAME VARCHAR('"+prop.getProperty("maxnicklength")+"'), " +
                    "PLAYTIME TIME NOT NULL," +
                    "BOARDSIZE INT NOT NULL," +
                    "SCORE INT NOT NULL)"
            );
        } catch (IOException | ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            try { DbUtils.close(statement); } catch (SQLException e1) { }
            try { DbUtils.close(connection); } catch (SQLException e1) { }
        }
    }

    /**
     * Updates the content of current panel.
     * @param gsm       GameStateManager
     * @param gamePanel Panel to update
     */
    @Override
    public void update(GameStateManager gsm, JPanel gamePanel) {
        // Create model, get data from the database and add it to the JTable
        ScoresTableModel model = new ScoresTableModel();
        JTable table = new JTable(model);
        model.updateData();

        JPanel scoresPanel = new JPanel(new BorderLayout());
        scoresPanel.add(new JScrollPane(table));

        // Create menu bar and add back button to it
        JPanel menuPanel = new JPanel();
        JMenuBar menuBar = new JMenuBar();
        JButton backButt = new JButton("Return to main menu");
        backButt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gsm.setCurrentState(GameStateManager.MENUSTATE, gamePanel);
            }
        });

        menuBar.add(backButt);
        menuPanel.add(menuBar);

        gamePanel.removeAll();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.add(scoresPanel, BorderLayout.CENTER);
        gamePanel.add(menuPanel, BorderLayout.SOUTH);
        gamePanel.updateUI();

        Logger.info("Score State updated");
    }
}
