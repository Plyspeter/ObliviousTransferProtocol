package idgwpi.votingsimulator.voter;

import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.votingsimulator.TestAuditor;

import java.io.IOException;
import java.net.SocketException;

public class Voter implements Runnable {
    private IBallotBoxConnector ballotBoxConnector;
    private IVoterVotingSession votingSession;
    private TestAuditor testAuditor;
    private int userID;

    public void setup(IBallotBoxConnector ballotBoxConnector, IVoterVotingSession votingSession,
                      TestAuditor testAuditor, int userID) {
        this.ballotBoxConnector = ballotBoxConnector;
        this.votingSession = votingSession;
        this.testAuditor = testAuditor;
        this.userID = userID;
        ballotBoxConnector.setup(Constants.getServerAddress(), Constants.getServerPort());
        votingSession.setup(Provider.getReceiptHandler(), Provider.getVoting());
    }

    public void startVoting() {
        try {
            var ballotBoxConnection = ballotBoxConnector.connect(userID);
            var vote = votingSession.begin(ballotBoxConnection, userID);
            testAuditor.addVote(vote);
        } catch (SocketException e) {
            //Do nothing, server has shutdown
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage());

        }
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        startVoting();
    }
}
