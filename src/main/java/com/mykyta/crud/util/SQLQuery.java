package com.mykyta.crud.util;

public class SQLQuery {

    public static final String GET_ALL = ("SELECT * FROM %s");
    public static final String GET_BY_ID = ("SELECT name FROM %s WHERE id = ?");
    public static final String GET_SPECIALTY_BY_ID = ("SELECT name FROM %s WHERE specialty_id = ?");
    public static final String INSERTION = ("INSERT INTO %s (name) VALUES (?)");
    public static final String GET_LAST = ("SELECT * FROM %s ORDER BY id DESC LIMIT 1");
    public static final String UPDATE = ("UPDATE %s SET name = ? WHERE id = ?");
    public static final String DELETE = ("DELETE FROM %s WHERE id = ?");

    public static final String GET_DEVELOPER_BY_ID = ("SELECT * FROM developers \n" +
            "LEFT JOIN developer_Skill\n" +
            "ON developers.id = developer_Skill.developer_ID\n" +
            "LEFT JOIN specialties\n" +
            "ON developers.specialty = specialties.specialty_id\n" +
            "WHERE developers.id = ? ;");
}
