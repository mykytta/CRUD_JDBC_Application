package com.mykyta.crud.util;


import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseStatement {

    private DatabaseStatement(){

    }
    public static PreparedStatement createPreparedStatement(String sql) throws SQLException {
        return DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
    }
}
