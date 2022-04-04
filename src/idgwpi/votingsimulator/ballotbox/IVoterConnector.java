package idgwpi.votingsimulator.ballotbox;

import java.io.IOException;

public interface IVoterConnector extends Runnable {
    void setup(int serverPort) throws IOException;

    void open();

    void close();
}
