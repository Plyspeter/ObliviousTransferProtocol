package idgwpi.votingsimulator.ballotbox;

import idgwpi.votingsimulator.dtos.VoteMessage;

public interface IDecryptionServiceConnector {
    void setup(String decryptionServiceAddress, int decryptionServicePort);

    void transmit(VoteMessage[] votes);

    void close();

    void connect();
}
