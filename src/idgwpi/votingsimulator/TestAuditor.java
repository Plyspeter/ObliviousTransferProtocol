package idgwpi.votingsimulator;

import idgwpi.votingsimulator.dtos.VoteMessage;
import idgwpi.votingsimulator.voter.VotersVote;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class TestAuditor {
    private ConcurrentHashMap<Integer, VotersVote> votes;
    private ConcurrentHashMap<Integer, VoteMessage> ballotBoxResults;
    private int voteCount;

    public TestAuditor(){
        votes = new ConcurrentHashMap<>();
        voteCount = 0;
    }

    public void addVote(VotersVote vote) {
        if (votes.containsKey(vote.getUserID()))
            votes.get(vote.getUserID()).setChoice(vote.getChoice());
        else
            votes.put(vote.getUserID(), vote);
        voteCount++;
    }

    public boolean auditElection(){
        if (votes.size() != ballotBoxResults.size())
            return false;
        System.out.println("Registered votes: " + votes.size());
        var extraVotes = 0;
        var userInput = new LinkedList<TestVote>();
        votes.forEach((userID,v) -> userInput.add(new TestVote(userID, v.getChoice(), v.getVoteCount())));
        var userInputIterator = userInput.descendingIterator();
        while(userInputIterator.hasNext()){
            var input = userInputIterator.next();
            if (!ballotBoxResults.containsKey(input.userID))
                return false;
            if (ballotBoxResults.get(input.userID).getChoice() != input.choice)
                return false;
            System.out.println(input.userID + " verified to have voted: " + input.choice + ", voted " + input.times + " times.");
            extraVotes += (input.times - 1);
        }
        System.out.println("__________________________________________");
        System.out.println("TestAuditor accepts election");
        System.out.println("Times voted: " + voteCount);
        System.out.println("Times people voted extra: " + extraVotes);
        return true;
    }

    public void sendBallotBoxResults(ConcurrentHashMap<Integer, VoteMessage> ballotBoxResults){
        this.ballotBoxResults = ballotBoxResults;
    }

    private static class TestVote{
        private int times;
        private int userID;
        private int choice;

        private TestVote(int userID, int choice, int times){
            this.userID = userID;
            this.choice = choice;
            this.times = times;
        }
    }
}
