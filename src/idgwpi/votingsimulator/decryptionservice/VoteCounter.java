package idgwpi.votingsimulator.decryptionservice;

import idgwpi.votingsimulator.dtos.ElectionResultMessage;
import idgwpi.votingsimulator.dtos.VoteMessage;

import java.util.ArrayList;

public class VoteCounter implements IVoteCounter {
    private ArrayList<ElectionResultMessage> results;
    private int yesVotes;
    private int noVotes;

    @Override
    public void setup(ArrayList<ElectionResultMessage> results) {
        this.results = results;
        yesVotes = 0;
        noVotes = 0;
    }

    @Override
    public void printResults() {
        for (ElectionResultMessage electionResultMessage : results) {
            for (VoteMessage voteMessage : electionResultMessage.getVotes()) {
                if ((voteMessage.getChoice() == 0)) {
                    yesVotes++;
                } else {
                    noVotes++;
                }
            }
        }
        var winner = (yesVotes == noVotes) ? "It is a tie!" : (yesVotes > noVotes) ? "The ayes have it!" : "The nays have it!";

        System.out.println("Election results:\n" + "Votes for yes: " + yesVotes + "\nVotes for no: " + noVotes + "\n" + winner);
    }
}
