package idgwpi.votingsimulator.dtos;

import java.io.Serializable;

public class VoteStored  implements Serializable {
    private boolean voteStored;

    public VoteStored(boolean voteStored){
        this.voteStored = voteStored;
    }

    public boolean isStored() {
        return voteStored;
    }
}
