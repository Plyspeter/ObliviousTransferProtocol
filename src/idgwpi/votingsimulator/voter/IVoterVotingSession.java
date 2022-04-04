package idgwpi.votingsimulator.voter;

import idgwpi.helpers.Connection;

import java.io.IOException;

public interface IVoterVotingSession {
    void setup(IReceiptHandler receiptHandler, IVoting voting);

    VotersVote begin(Connection connection, int userID) throws IOException, ClassNotFoundException;
}
