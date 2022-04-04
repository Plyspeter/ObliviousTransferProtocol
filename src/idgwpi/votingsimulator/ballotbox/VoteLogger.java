package idgwpi.votingsimulator.ballotbox;

import idgwpi.votingsimulator.TestAuditor;
import idgwpi.votingsimulator.dtos.VoteMessage;

import java.util.concurrent.ConcurrentHashMap;

public class VoteLogger implements IVoteLogger {
    private ConcurrentHashMap<Integer, VoteMessage> votes;
    private TestAuditor testAuditor;

    public VoteLogger(){
        votes = new ConcurrentHashMap<>();
    }

    @Override
    public void setup(TestAuditor testAuditor){
        this.testAuditor = testAuditor;
    }

    @Override
    public VoteMessage[] getVotes() {
        var returnVotes = new VoteMessage[votes.size()];
        votes.values().toArray(returnVotes);
        testAuditor.sendBallotBoxResults(votes);
        votes = new ConcurrentHashMap<>();
        return returnVotes;
    }

    @Override
    public void addVote(int userID, VoteMessage vote) {
        votes.put(userID, vote);
    }
}
