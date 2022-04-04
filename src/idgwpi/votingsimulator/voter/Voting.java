package idgwpi.votingsimulator.voter;

import idgwpi.votingsimulator.dtos.Ballot;

import java.util.Random;

public class Voting implements IVoting {
    @Override
    public VotersVote getVote(Ballot ballot, int userID) {
        return new VotersVote(userID, new Random().nextInt(ballot.getStringChoices().length));
    }
}
