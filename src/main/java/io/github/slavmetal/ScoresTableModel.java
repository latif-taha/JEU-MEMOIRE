package io.github.slavmetal;

import javax.swing.table.AbstractTableModel;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ScoresTableModel extends AbstractTableModel {
    private List<ScoresList> scoresList = new ArrayList<>(25);
    private List<String> columnNames = new ArrayList<>(25);

    @Override
    public int getRowCount() {
        return scoresList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ScoresList rowValue = scoresList.get(rowIndex);
        Object value = null;
        switch (columnIndex){
            case 0:
                value = rowValue.getNickname();
                break;
            case 1:
                value = rowValue.getTime();
                break;
            case 2:
                value = rowValue.getBoardSize();
                break;
            case 3:
                value = rowValue.getScore();
                break;
        }
        return value;
    }

    public void refresh() throws SQLException {
        Properties prop = new Properties();
        InputStream input = null;
        Connection con = null;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);

            Class.forName(prop.getProperty("dbdriver"));
            con = DriverManager.getConnection(
                    prop.getProperty("dbconnection"),
                    prop.getProperty("dbuser"),
                    prop.getProperty("dbpassword"));
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        List<String> values = new ArrayList<>(25);
        try (PreparedStatement ps = con.prepareStatement("SELECT * FROM SCORES")) {
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                for (int col = 0; col < md.getColumnCount(); col++) {
                    values.add(md.getColumnName(col + 1));
                }
                while (rs.next()) {
                    ScoresList list = new ScoresList(rs.getString(1), rs.getTime(2), rs.getInt(3), rs.getInt(4));
                    scoresList.add(list);
                }
            }
        } finally {
            if (columnNames.size() != values.size()) {
                columnNames = values;
                fireTableStructureChanged();
            } else {
                fireTableDataChanged();
            }
        }

    }

}