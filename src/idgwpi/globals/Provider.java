package idgwpi.globals;

import idgwpi.receiptgenerator.client.*;
import idgwpi.receiptgenerator.cryptosystem.*;
import idgwpi.receiptgenerator.server.*;
import idgwpi.votingsimulator.ballotbox.*;
import idgwpi.votingsimulator.decryptionservice.IVoteCounter;
import idgwpi.votingsimulator.decryptionservice.VoteCounter;
import idgwpi.votingsimulator.voter.*;

public class Provider {
    private static boolean useECOTWithSF = false;
    private static IVoteLogger voteLogger = new VoteLogger();

    public static IHashLogic getHashLogic() {
        return new HashLogic();
    }

    public static IHash getHash() {
        return new Hash();
    }

    public static ICryptoSystemLogic getCryptoSystemLogic() {
        return new CryptoSystemLogic();
    }

    public static ICryptoSystem getCryptoSystem() {
        return new CryptoSystem();
    }

    public static IRGClientLogicECOT getClientLogic() {
        return new RGClientLogicECOT();
    }

    public static IRGServerLogicECOT getServerLogic() {
        return new RGServerLogicECOT();
    }

    public static IVoting getVoting() {
        return new Voting();
    }

    public static IReceiptHandler getReceiptHandler() {
        return new ReceiptHandler();
    }

    public static IRGClient getReceiptGeneratorClient() {
        if(useECOTWithSF)
            return new RGClientECOTWithSF();
        else
            return new RGClientECOTWithoutSF();
    }

    public static IBallotBoxConnector getBallotBoxConnector() {
        return new BallotBoxConnector();
    }

    public static IVoterVotingSession getVoterVotingSession() {
        return new VoterVotingSession();
    }

    public static IDecryptionServiceConnector getDecryptionServiceConnector() {
        return new DecryptionServiceConnector();
    }

    public static IReceiptGenerator getReceiptGenerator() {
        return new ReceiptGenerator();
    }

    public static IVoterConnector getVoterConnector() {
        return new VoterConnector();
    }

    public static IVoteLogger getVoteLogger() {
        return voteLogger;
    }

    public static IRGServer getReceiptGeneratorServer() {
        if(useECOTWithSF)
            return new RGServerECOTWithSF();
        else
            return new RGServerECOTWithoutSF();
    }

    public static IVoteCounter getVoteCounter() {
        return new VoteCounter();
    }

    public static idgwpi.votingsimulator.decryptionservice.IBallotBoxConnector getBallotBoxConnectorForDecryptionService() {
        return new idgwpi.votingsimulator.decryptionservice.BallotBoxConnector();
    }

    public static IRGClientCommunicator getClientCommunicator() {
        if(useECOTWithSF)
            return new RGClientCommunicatorECOTWithSF();
        else
            return new RGClientCommunicatorECOTWithoutSF();
    }

    public static IRGServerCommunicator getServerCommunicator() {
        if(useECOTWithSF)
            return new RGServerCommunicatorECOTWithSF();
        else
            return new RGServerCommunicatorECOTWithoutSF();
    }

    public static void setUseECOTWithSF(boolean useECOTWithSF) {
        Provider.useECOTWithSF = useECOTWithSF;
    }
}
