package com.mykyta.crud.repository.jdbc;

import com.mykyta.crud.model.Specialty;
import com.mykyta.crud.repository.SpecialtyRepository;
import com.mykyta.crud.util.DatabaseStatement;
import com.mykyta.crud.util.SQLQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCSpecialtyRepository implements SpecialtyRepository {

    private final static String TABLE_NAME = "specialties";

    @Override
    public List<Specialty> getAll() {
        final List<Specialty> specialties = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_ALL, TABLE_NAME))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int specialtyID = resultSet.getInt("id");
                String specialtyName = resultSet.getString("name");
                specialties.add(new Specialty(specialtyID, specialtyName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specialties;
    }

    @Override
    public Specialty getById(Integer integer) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_BY_ID, TABLE_NAME))) {
            preparedStatement.setInt(1, integer);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String specialtyName = resultSet.getString("name");
                return new Specialty(integer, specialtyName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Specialty create(Specialty entity) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.INSERTION, TABLE_NAME))) {
            preparedStatement.setString(1, entity.getSpecialtyName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_LAST, TABLE_NAME))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getString("name").equals(entity.getSpecialtyName())) {
                return new Specialty(entity.getId(), resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Specialty update(Specialty entity) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.UPDATE, TABLE_NAME))) {
            preparedStatement.setString(1, entity.getSpecialtyName());
            preparedStatement.setInt(2, entity.getId());
            return preparedStatement.executeUpdate() > 0 ? entity : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteById(Integer integer) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.DELETE, TABLE_NAME))) {
            preparedStatement.setInt(1, integer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
