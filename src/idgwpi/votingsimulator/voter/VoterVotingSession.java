package idgwpi.votingsimulator.voter;

import idgwpi.globals.Provider;
import idgwpi.helpers.Connection;
import idgwpi.votingsimulator.dtos.*;

import java.io.IOException;

public class VoterVotingSession implements IVoterVotingSession {
    private IReceiptHandler receiptHandler;
    private IVoting voting;

    @Override
    public void setup(IReceiptHandler receiptHandler, IVoting voting) {
        this.receiptHandler = receiptHandler;
        this.voting = voting;
        receiptHandler.setup(Provider.getReceiptGeneratorClient());
    }

    @Override
    public VotersVote begin(Connection connection, int userID) throws IOException, ClassNotFoundException {
        connection.getOutputStream().writeObject(new RequestBallot());
        connection.getOutputStream().flush();

        var ballot = (Ballot) connection.getInputStream().readObject();

        var vote = voting.getVote(ballot, userID);

        connection.getOutputStream().writeObject(new VoteMessage(vote.getChoice()));
        connection.getOutputStream().flush();

        receiptHandler.getReceipt(connection, vote.getChoice());

        if (receiptHandler.verifyReceipt(ballot)){
            connection.getOutputStream().writeObject(new AcceptReceipt(true));
            connection.getOutputStream().flush();

            if(((VoteStored) connection.getInputStream().readObject()).isStored()){
                connection.close();
                return vote;
            } else {
                connection.close();
                throw new RuntimeException("Server error: Vote was not stored");
            }

        } else {
            connection.getOutputStream().writeObject(new AcceptReceipt(false));
            connection.getOutputStream().flush();

            connection.close();
            throw new RuntimeException("Receipt failed to verify");
        }
    }
}
