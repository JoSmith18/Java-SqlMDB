package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class GetConnection {
    public static Connection get() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql:movies", "basecamp", "pgpass");
    }
}
