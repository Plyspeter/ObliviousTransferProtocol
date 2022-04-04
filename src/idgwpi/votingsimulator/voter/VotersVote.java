package idgwpi.votingsimulator.voter;

public class VotersVote {
    private int userID;
    private int choice;
    private int voteCount;

    public VotersVote(int userID, int choice){
        this.userID = userID;
        this.choice = choice;
        voteCount = 1;
    }

    public int getUserID() {
        return userID;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
        voteCount++;
    }

    public int getVoteCount() {
        return voteCount;
    }
}
