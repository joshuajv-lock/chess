package server;

import java.util.ArrayList;
import java.util.Collection;

public class ConnectionManager {

    Collection<Connection> myConnections = new ArrayList<>();

    public void addConnection(Connection connection) {
        myConnections.add(connection);
    }

    public void removeConnection(Connection connection) {
        myConnections.remove(connection);
    }

    public void broadcast(String message, Connection connection) {
        for (Connection myConnection : myConnections) {
            if (connection.gameID == myConnection.gameID) {
                myConnection.ctx.send(message);
            }
        }
    }

    public void sendWithoutReception(String message, Connection connection) {
        for (Connection myConnection : myConnections) {
            if (connection.gameID == myConnection.gameID
                && connection != myConnection) {
                myConnection.ctx.send(message);
            }
        }
    }

}
