package idgwpi.votingsimulator.ballotbox;

import idgwpi.votingsimulator.TestAuditor;
import idgwpi.votingsimulator.dtos.VoteMessage;

public interface IVoteLogger {
    void setup(TestAuditor testAuditor);

    VoteMessage[] getVotes();

    void addVote(int userID, VoteMessage vote);
}
