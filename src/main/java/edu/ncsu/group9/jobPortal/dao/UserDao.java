package edu.ncsu.group9.jobPortal.dao;

import edu.ncsu.group9.jobPortal.model.Student;
import edu.ncsu.group9.jobPortal.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Log4j2
@Service
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public static final String CHECK_USER_SQL = "SELECT userId, userPassword FROM JOBPORTAL.USER WHERE USERID=?";

    public static final String INSERT_USER_SQL = "INSERT INTO JOBPORTAL.USER VALUES (?, ?);";

    public static final String SUCCESSFUL = "LOGIN SUCCESSFUL";
    public static final String UNSUCCESSFUL = "LOGIN UNSUCCESSFUL";


    public String checkUserRecord(User user) {
        String userId = user.getUserId();
        String userPassword = user.getUserPassword();
        User fetchedUser = null;
        try {
            fetchedUser = jdbcTemplate.queryForObject(CHECK_USER_SQL, new Object[] { userId }, new BeanPropertyRowMapper<>(User.class));
            return Objects.isNull(fetchedUser) ? UNSUCCESSFUL : (user.getUserPassword().equals(fetchedUser.getUserPassword())? SUCCESSFUL : UNSUCCESSFUL);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return UNSUCCESSFUL;
    }

    public int addUser(Student student) {
        int rows = 0;
        try {
            rows = jdbcTemplate.update(INSERT_USER_SQL, student.getEmailId(), student.getPassword());
            log.info("Inserted {} rows(s) into the User Table");
        } catch (Exception exception) {
            log.error("Error Occurred while inserting record in the user table. Exception -> ");
            exception.printStackTrace();
        }
        return rows;
    }

}