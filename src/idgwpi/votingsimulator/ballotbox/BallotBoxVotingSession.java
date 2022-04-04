package idgwpi.votingsimulator.ballotbox;

import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.helpers.RandomBigInteger;
import idgwpi.votingsimulator.dtos.*;
import idgwpi.helpers.Connection;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;

public class BallotBoxVotingSession implements IBallotBoxVotingSession {
    private final int userID;
    private Connection connection;
    private IReceiptGenerator receiptGenerator;
    private IVoteLogger voteLogger;

    public BallotBoxVotingSession(Connection connection, IReceiptGenerator receiptGenerator,
                                  IVoteLogger voteLogger, int userID) {
        this.connection = connection;
        this.receiptGenerator = receiptGenerator;
        this.voteLogger = voteLogger;
        this.userID = userID;
        receiptGenerator.setup(Provider.getReceiptGeneratorServer(), connection);
    }

    public void runSession() throws IOException, ClassNotFoundException {
        var request = connection.getInputStream().readObject();
        if(!(request instanceof RequestBallot))
            throw new RuntimeException("BallotBoxVotingSession unexpected class");

        var choices = Constants.getBallotChoices();
        var ecPoints = generateECPoints(choices.length);

        connection.getOutputStream().writeObject(new Ballot(ecPoints, choices));
        connection.getOutputStream().flush();

        var vote = (VoteMessage) connection.getInputStream().readObject();

        receiptGenerator.generate(ecPoints);

        var accept = (AcceptReceipt) connection.getInputStream().readObject();

        if(accept.isAccepted()){
            voteLogger.addVote(userID, vote);
            connection.getOutputStream().writeObject(new VoteStored(true));
            connection.getOutputStream().flush();
        }
        connection.close();
    }

    public ECPoint[] generateECPoints(int numberOfECPoints){
        var points = new ECPoint[numberOfECPoints];
        var group = Constants.getGroup();
        for (int i = 0; i < numberOfECPoints; i++){
            points[i] = group.getG().multiply(RandomBigInteger.generate(group.getN()));
        }

        return points;
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
        try {
            runSession();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("BallotBoxVotingSession session failed:\n" + e);
        }

    }
}
