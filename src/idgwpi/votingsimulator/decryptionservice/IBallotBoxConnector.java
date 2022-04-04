package idgwpi.votingsimulator.decryptionservice;

import idgwpi.votingsimulator.dtos.ElectionResultMessage;

import java.util.ArrayList;

public interface IBallotBoxConnector extends Runnable{

    void close();

    void start();

    void setup(int decryptionServicePort);

    ArrayList<ElectionResultMessage> getResults();
}
