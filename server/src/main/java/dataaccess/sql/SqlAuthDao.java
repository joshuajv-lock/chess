package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;


import java.sql.SQLException;

public class SqlAuthDao implements AuthDAO {

    public SqlAuthDao() throws DataAccessException {
        DatabaseManager.createDatabase();
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        //create the table if it doesn't exist
        String sqlStatement = """
            CREATE TABLE IF NOT EXISTS auths ( 
                authToken VARCHAR(256) PRIMARY KEY NOT NULL,
                username VARCHAR(256) NOT NULL
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
    public void createAuth(AuthData authData) throws DataAccessException {
        //open the database connection
        //INSERT
        String sqlStatement = """
            INSERT INTO auths (authToken, username)
            VALUES (?,?);
                """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.setString(1, authData.authToken());
            statement.setString(2, authData.username());
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
    public AuthData getAuth(String authToken) throws DataAccessException {
        //SELECT
        String sqlStatement = """
            SELECT * FROM auths
            WHERE authToken = ?;
            """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.setString(1, authToken);
            try (var obtainedInfo = statement.executeQuery()) {

                if (obtainedInfo.next()) {
                    String obtainedAuthToken = obtainedInfo.getString("authToken");
                    String obtainedUsername = obtainedInfo.getString("username");


                    return new AuthData(obtainedAuthToken, obtainedUsername
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

    //repurpose clear into a delete auth for the loging out one
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        //DELETE
        String sqlStatement = """
            DELETE FROM auths
            WHERE authToken = ?;
            """;

        try (var connection = DatabaseManager.getConnection();
             var statement = connection.prepareStatement(sqlStatement)) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException(
                    "could not delete the authToken",
                    e
            );
        }
    };

    @Override
    public void clear() throws DataAccessException {
        //DELETE
        String sqlStatement = """
            DELETE FROM auths;
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
