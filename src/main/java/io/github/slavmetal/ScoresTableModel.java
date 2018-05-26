package io.github.slavmetal;

import org.apache.commons.dbutils.DbUtils;

import javax.swing.table.AbstractTableModel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model to use in the JTable to show all scores.
 */
public class ScoresTableModel extends AbstractTableModel {
    private List<ScoresList> scoresList = new ArrayList<>(25);
    private List<String> columnNames = new ArrayList<>(25);

    /**
     * @return Number of rows in the table
     */
    @Override
    public int getRowCount() {
        return scoresList.size();
    }

    /**
     * @return Number of columns in the table
     */
    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    /**
     * @param column Column to get name of
     * @return Name of the column
     */
    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    /**
     * @param rowIndex      Index of the row
     * @param columnIndex   Index of the column
     * @return              Value of a cell at given row/column index
     */
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

    /**
     * Updates the data stored in the database table.
     */
    void updateData() {
        DbConnection dbConnection = new DbConnection();
        PreparedStatement preparedStatement = null;
        List<String> values = new ArrayList<>();

        try {
            preparedStatement = dbConnection.getConnection().prepareStatement("SELECT * FROM SCORES");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();

            for (int col = 0; col < rsmd.getColumnCount(); col++) {
                values.add(rsmd.getColumnName(col + 1));
            }

            while (resultSet.next()) {
                ScoresList list = new ScoresList(resultSet.getString(1), resultSet.getTime(2),
                        resultSet.getInt(3), resultSet.getInt(4));
                scoresList.add(list);
            }

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if (columnNames.size() != values.size()) {
                columnNames = values;
                fireTableStructureChanged();
            } else {
                fireTableDataChanged();
            }

            try { DbUtils.close(preparedStatement); } catch (SQLException e1) { }
            try { DbUtils.close(dbConnection.getConnection()); } catch (SQLException e1) { }
        }
    }

}