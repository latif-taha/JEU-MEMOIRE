package io.github.slavmetal;

import org.apache.commons.dbutils.DbUtils;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

/**
 * Game's highscores table.
 */
class ScoreState implements GameState {
    private Statement statement;
    private JButton backButt = new JButton("Return to main menu");

    /**
     * Creates a table if it doesn't exist.
     */
    ScoreState(GameStateManager gsm, JPanel gamePanel) {
        backButt.addActionListener(actionEvent -> gsm.setCurrentState(GameStateManager.MENUSTATE, gamePanel));

        createDatabaseTable();
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

        menuBar.add(backButt);
        menuPanel.add(menuBar);

        gamePanel.removeAll();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.add(scoresPanel, BorderLayout.CENTER);
        gamePanel.add(menuPanel, BorderLayout.SOUTH);
        gamePanel.updateUI();

        Logger.info("Score State updated");
    }

    /**
     * Creates table in the database if it doesn't exist yet.
     */
    private void createDatabaseTable(){
        DbConnection dbConnection = new DbConnection();

        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            InputStream input = new FileInputStream(Objects.requireNonNull(classLoader.getResource("config.properties")).getFile());
            Properties prop = new Properties();
            prop.load(input);

            statement = dbConnection.getConnection().createStatement();

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS SCORES " +
                    "(NICKNAME VARCHAR('"+ prop.getProperty("maxnicklength")+"'), " +
                    "PLAYTIME TIME NOT NULL," +
                    "BOARDSIZE INT NOT NULL," +
                    "SCORE INT NOT NULL)"
            );
        } catch (IOException | SQLException e){
            e.printStackTrace();
        } finally {
            try { DbUtils.close(statement); } catch (SQLException e1) { }
            try { DbUtils.close(dbConnection.getConnection()); } catch (SQLException e1) { }
        }
    }
}
