package io.github.slavmetal;

import org.apache.commons.dbutils.DbUtils;
import org.pmw.tinylog.Logger;

import javax.swing.*;
import java.awt.*;
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
    public ScoreState() {
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
        try {
            Class.forName(prop.getProperty("dbdriver"));
            connection = DriverManager.getConnection(
                    prop.getProperty("dbconnection"),
                    prop.getProperty("dbuser"),
                    prop.getProperty("dbpassword"));
            statement = connection.createStatement();

            gamePanel.removeAll();
            gamePanel.setLayout(new BorderLayout());
            ScoresTableModel model = new ScoresTableModel();
            model.refresh();
            JTable table = new JTable(model);
            gamePanel.add(new JScrollPane(table));
            gamePanel.updateUI();

        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        } finally {
            try { DbUtils.close(statement); } catch (SQLException e1) { }
            try { DbUtils.close(connection); } catch (SQLException e1) { }
        }

        Logger.info("Score State updated");
    }
}
