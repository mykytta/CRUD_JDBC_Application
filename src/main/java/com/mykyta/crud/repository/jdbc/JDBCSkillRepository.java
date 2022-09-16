package com.mykyta.crud.repository.jdbc;

import com.mykyta.crud.model.Skill;
import com.mykyta.crud.repository.SkillRepository;
import com.mykyta.crud.util.DatabaseStatement;
import com.mykyta.crud.util.SQLQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCSkillRepository implements SkillRepository {

    private final static String TABLE_NAME = "skills";

    @Override
    public List<Skill> getAll() {
        final List<Skill> skills = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_ALL, TABLE_NAME))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int skillID = resultSet.getInt("id");
                String skillName = resultSet.getString("name");
                skills.add(new Skill(skillID, skillName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skills;
    }

    @Override
    public Skill getById(Integer integer) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_BY_ID, TABLE_NAME))) {
            preparedStatement.setInt(1, integer);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String skillName = resultSet.getString("name");
                return new Skill(integer, skillName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Skill create(Skill entity) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.INSERTION, TABLE_NAME))) {
            preparedStatement.setString(1, entity.getSkillName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_LAST, TABLE_NAME))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            if (resultSet.getString("name").equals(entity.getSkillName())) {
                return new Skill(entity.getId(), resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Skill update(Skill entity) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.UPDATE, TABLE_NAME))) {
            preparedStatement.setString(1, entity.getSkillName());
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
