package idgwpi.votingsimulator.dtos;

import java.io.Serializable;

public class ElectionResultMessage implements Serializable {
    private final VoteMessage[] votes;

    public ElectionResultMessage(VoteMessage[] votes) {
        this.votes = votes;
    }

    public VoteMessage[] getVotes() {
        return votes;
    }
}
