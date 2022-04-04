package idgwpi.votingsimulator.decryptionservice;

import idgwpi.globals.Constants;

public class DecryptionService{
    private IBallotBoxConnector ballotBoxConnector;
    private IVoteCounter voteCounter;

    public void setup(IBallotBoxConnector ballotBoxConnector, IVoteCounter voteCounter){
        this.ballotBoxConnector = ballotBoxConnector;
        this.voteCounter = voteCounter;
        ballotBoxConnector.setup(Constants.getDecryptionServicePort());
    }

    public void start(){
        var runnable = new Thread(ballotBoxConnector);
        runnable.start();
    }

    public void close(){
        ballotBoxConnector.close();
        voteCounter.setup(ballotBoxConnector.getResults());
        voteCounter.printResults();
    }
}
