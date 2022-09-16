package com.mykyta.crud.repository.jdbc;

import com.mykyta.crud.model.Developer;
import com.mykyta.crud.model.Skill;
import com.mykyta.crud.model.Specialty;
import com.mykyta.crud.model.Status;
import com.mykyta.crud.repository.DeveloperRepository;
import com.mykyta.crud.repository.SkillRepository;
import com.mykyta.crud.util.DatabaseStatement;
import com.mykyta.crud.util.SQLQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCDeveloperRepository implements DeveloperRepository {

    private final SkillRepository skillRepository = new JDBCSkillRepository();
    private static final String TABLE_NAME = "developers";

    @Override
    public List<Developer> getAll() {
        List<Developer> developers = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_ALL, TABLE_NAME))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Developer developer = new Developer();
                developer.setId(resultSet.getInt("id"));
                developer.setFirstName(resultSet.getString("first_Name"));
                developer.setLastName(resultSet.getString("last_Name"));
                developer.setSpecialty(new JDBCSpecialtyRepository().getById(resultSet.getInt("specialty")));
                developer.setStatus(Status.valueOf(resultSet.getString("status")));
                developers.add(developer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_ALL, "developer_Skill"))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int developerID = resultSet.getInt("developer_ID");
                int skillID = resultSet.getInt("skill_ID");
                for (Developer developer : developers) {
                    if (developer.getId() == developerID) {
                        if (developer.getSkills() != null) {
                            developer.getSkills().add(skillRepository.getById(skillID));
                        } else {
                            developer.setSkills(new ArrayList<>());
                            developer.getSkills().add(skillRepository.getById(skillID));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return developers;
    }

    @Override
    public Developer getById(Integer integer) {
        Developer developer = new Developer();
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_DEVELOPER_BY_ID, TABLE_NAME))) {
            preparedStatement.setInt(1, integer);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                resultSet.next();
                developer.setId(resultSet.getInt("id"));
                developer.setFirstName(resultSet.getString("first_Name"));
                developer.setLastName(resultSet.getString("last_Name"));
                developer.setSpecialty(new JDBCSpecialtyRepository().getById(resultSet.getInt("specialty")));
                developer.setStatus(Status.valueOf(resultSet.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement(String.format(SQLQuery.GET_ALL, "developer_Skill"))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int developerID = resultSet.getInt("developer_ID");
                int skillID = resultSet.getInt("skill_ID");
                if(developer.getId() == developerID) {
                    if (developer.getSkills() != null) {
                        developer.getSkills().add(skillRepository.getById(skillID));
                    } else {
                        developer.setSkills(new ArrayList<>());
                        developer.getSkills().add(skillRepository.getById(skillID));
                    }
                }
            }
            return developer.getSkills() != null ? developer : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Developer create(Developer entity) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement("INSERT INTO " + TABLE_NAME + " (first_Name, last_Name, specialty," +
                        " status) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getSpecialty().getId());
            preparedStatement.setString(4, Status.ACTIVE.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement("INSERT INTO developer_Skill (developer_ID, skill_ID)" +
                        " VALUES (?, ?)")) {
            for(Skill skill : entity.getSkills()){
                preparedStatement.setInt(1, getIdOfLastRow());
                preparedStatement.setInt(2, skill.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public Developer update(Developer entity) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement("UPDATE developers SET " +
                        "first_Name = ?, last_Name = ?, specialty = ? WHERE id = ?")) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setInt(3, entity.getSpecialty().getId());
            preparedStatement.setInt(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement("DELETE FROM developer_Skill WHERE developer_ID = ?")) {
            preparedStatement.setInt(1, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement("INSERT INTO developer_Skill (developer_ID, skill_ID) " +
                        "VALUES (?, ?)")) {
            for(Skill skill : entity.getSkills()){
                preparedStatement.setInt(1, entity.getId());
                preparedStatement.setInt(2, skill.getId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public void deleteById(Integer integer) {
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement("UPDATE " + TABLE_NAME + " SET status = ? WHERE id = ?")) {
            preparedStatement.setString(1, Status.DELETED.toString());
            preparedStatement.setInt(2, integer);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getIdOfLastRow(){
        int row;
        try (PreparedStatement preparedStatement = DatabaseStatement
                .createPreparedStatement("SELECT id FROM developers " +
                        "WHERE id = (" +
                        "SELECT MAX(id) FROM developers)")){
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            row = resultSet.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return row;
    }
}
