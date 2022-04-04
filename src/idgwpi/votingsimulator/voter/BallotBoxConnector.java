package idgwpi.votingsimulator.voter;

import idgwpi.helpers.Connection;
import idgwpi.votingsimulator.dtos.AuthenticationMessage;

import java.io.IOException;
import java.net.Socket;

public class BallotBoxConnector implements IBallotBoxConnector{
    private String serverAddress;
    private int serverPort;

    @Override
    public void setup(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    @Override
    public Connection connect(int userID) throws IOException {
        var socket = new Socket(serverAddress, serverPort);
        var connection = new Connection(socket);
        connection.getOutputStream().writeObject(new AuthenticationMessage(userID));
        connection.getOutputStream().flush();
        return connection;
    }
}
