package com.mykyta.crud.util;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseStatement {

    private DatabaseStatement(){

    }
    public static PreparedStatement createPreparedStatement(String sql) throws SQLException {
        return DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
    }
    public static PreparedStatement createDeveloperPreparedStatement(String sql) throws SQLException {
        return DatabaseConnection.getInstance().getConnection().prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }
}
