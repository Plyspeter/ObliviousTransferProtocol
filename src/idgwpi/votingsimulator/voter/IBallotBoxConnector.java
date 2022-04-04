package idgwpi.votingsimulator.voter;

import idgwpi.helpers.Connection;

import java.io.IOException;

public interface IBallotBoxConnector {

    void setup(String serverAddress, int serverPort);

    Connection connect(int userID) throws IOException;
}
