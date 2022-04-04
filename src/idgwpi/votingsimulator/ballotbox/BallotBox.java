package idgwpi.votingsimulator.ballotbox;

import idgwpi.globals.Constants;
import idgwpi.votingsimulator.TestAuditor;

import java.io.IOException;

public class BallotBox {
    private IDecryptionServiceConnector decryptionServiceConnector;
    private IVoterConnector voterConnector;
    private IVoteLogger voteLogger;

    public void setup(IDecryptionServiceConnector decryptionServiceConnector, TestAuditor testAuditor,
                      IVoterConnector voterConnector, IVoteLogger voteLogger) {
        this.decryptionServiceConnector = decryptionServiceConnector;
        this.voterConnector = voterConnector;
        this.voteLogger = voteLogger;
        voteLogger.setup(testAuditor);
        try {
            voterConnector.setup(Constants.getServerPort());
        } catch (IOException e) {
            throw new RuntimeException("BallotBox failed to start:\n" + e);
        }
    }

    public void openBallotBox() {
        var connector = new Thread(voterConnector);
        connector.start();
    }

    public void closeBallotBox() {
        voterConnector.close();
    }

    public void transmitResults() {
        decryptionServiceConnector.setup(Constants.getDecryptionServiceAddress(), Constants.getDecryptionServicePort());
        decryptionServiceConnector.connect();
        decryptionServiceConnector.transmit(voteLogger.getVotes());
        decryptionServiceConnector.close();
    }


}
