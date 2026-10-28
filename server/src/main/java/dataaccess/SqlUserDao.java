package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

public class SqlUserDao implements UserDAO{

    public SqlUserDao() throws DataAccessException {
        DatabaseManager.createDatabase();
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        //create the table if it doesn't exist
        String sqlStatement = """
            CREATE TABLE IF NOT EXISTS users ( 
                username VARCHAR(256) PRIMARY KEY NOT NULL,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL
                )
                """;

        try (var connection = DatabaseManager.getConnection();
            var statement = connection.prepareStatement(sqlStatement)) {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(
                    "The table could not be created",
                    e
            );
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        //open the database connection
        //INSERT
        String sqlStatement = """
            INSERT INTO users (username, password, email)
            VALUES (?,?,?);
                """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.setString(1, userData.username());
            String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            statement.setString(2, hashedPassword);
            statement.setString(3, userData.email());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(
                    "The info was not inserted into the table",
                    e
            );
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        //SELECT
        String sqlStatement = """
            SELECT * FROM users
            WHERE username = ?;
            """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.setString(1, username);
            try (var obtainedInfo = statement.executeQuery()) {

                if (obtainedInfo.next()) {
                    String obtainedUsername = obtainedInfo.getString("username");
                    String obtainedPassword = obtainedInfo.getString("password");
                    String obtainedEmail = obtainedInfo.getString("email");

                    return new UserData(obtainedUsername, obtainedPassword, obtainedEmail
                    );
                }
                return null;
            }
        }   catch (SQLException e) {
                throw new DataAccessException(
                    "The info was not obtained from the table",
                    e
            );
        }
    }

    @Override
    public void clear() throws DataAccessException {
        //DELETE
        String sqlStatement = """
            DELETE FROM users;
            """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(
                    "could not clear table",
                    e
            );
        }
    };


    }

