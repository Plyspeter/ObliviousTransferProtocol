package idgwpi.votingsimulator.dtos;

import java.io.Serializable;

public class VoteMessage implements Serializable {
    private int choice;

    public VoteMessage(int choice){
        this.choice = choice;
    }

    public int getChoice() {
        return choice;
    }
}
