package idgwpi.votingsimulator.voter;

import idgwpi.votingsimulator.dtos.Ballot;

public interface IVoting {
    VotersVote getVote(Ballot ballot, int userID);
}
