package idgwpi.votingsimulator.dtos;

import java.io.Serializable;

public class AuthenticationMessage implements Serializable {
    private int userID;

    public AuthenticationMessage(int userID){
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
