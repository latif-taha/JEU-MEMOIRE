package io.github.slavmetal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

/**
 * Creates connection to the DB specified in the properties.
 */
class DbConnection {
    private Connection connection;

    DbConnection() {
        try {
            // Load resources
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            InputStream input = new FileInputStream(Objects.requireNonNull(classLoader.getResource("config.properties")).getFile());
            Properties prop = new Properties();
            prop.load(input);

            Class.forName(prop.getProperty("dbdriver"));
            connection = DriverManager.getConnection(
                    prop.getProperty("dbconnection"),
                    prop.getProperty("dbuser"),
                    prop.getProperty("dbpassword"));

        } catch (IOException | ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * @return Active Connection to the database
     */
    Connection getConnection() {
        return connection;
    }
}
