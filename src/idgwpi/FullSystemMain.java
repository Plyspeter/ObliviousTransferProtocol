package idgwpi;

import idgwpi.globals.Provider;
import idgwpi.votingsimulator.TestAuditor;
import idgwpi.votingsimulator.ballotbox.BallotBox;
import idgwpi.votingsimulator.decryptionservice.DecryptionService;
import idgwpi.votingsimulator.voter.Voter;

import java.util.Random;

public class FullSystemMain {
    //Variables to change:
    private static int minVotersPerSession = 15;
    private static int maxVotersPerSession = 50;
    private static int numberOfVoters = 5000;
    private static int votingSessions = 20;
    private static long electionLengthSeconds = 120;
    private static boolean runElectionSimulation = false;
    private static boolean useECOTWithSelectiveFailures = false;
    private static int compareVoters = 1000;
    private static int sessionTimeoutInSeconds = 5;

    //Do not change values below this!
    private static int voteAttempts;

    public static void main(String[] args) {
        if (runElectionSimulation) {
            if (useECOTWithSelectiveFailures)
                Provider.setUseECOTWithSF(true);
            runElectionSimulation();
        } else
            runECOTCompare();
    }

    private static void runElectionSimulation(){
        var testAuditor = new TestAuditor();

        var ballotBox = new BallotBox();
        var decryptionService = new DecryptionService();
        ballotBox.setup(Provider.getDecryptionServiceConnector(), testAuditor, Provider.getVoterConnector(), Provider.getVoteLogger());
        decryptionService.setup(Provider.getBallotBoxConnectorForDecryptionService(), Provider.getVoteCounter());
        ballotBox.openBallotBox();

        var election = new Thread(() -> {
            for (int i = 0; i < votingSessions; i++) {
                var rushHour = new Random().nextBoolean();
                System.out.print("Session " + i + " started.");
                if (rushHour)
                    System.out.print(" RUSH HOUR!");
                var voters = new Random().nextInt(maxVotersPerSession - minVotersPerSession) + minVotersPerSession;
                countVoteAttempts(voters);
                System.out.println("\nVote attempts in session: " + voters);

                var session = new Thread(() -> {
                    for (int j = 0; j < voters; j++) {
                        if (!rushHour)
                            try {
                                Thread.sleep(new Random().nextInt(1000) + 500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        var voter = new Voter();
                        var userID = new Random().nextInt(numberOfVoters);
                        voter.setup(Provider.getBallotBoxConnector(), Provider.getVoterVotingSession(), testAuditor, userID);
                        var thread = new Thread(voter);
                        thread.start();
                    }
                });

                session.start();

                try {
                    Thread.sleep(sessionTimeoutInSeconds * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        election.start();

        try {
            Thread.sleep(electionLengthSeconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ballotBox.closeBallotBox();
        decryptionService.start();
        ballotBox.transmitResults();

        System.out.println("TestAuditor results: ");
        if(testAuditor.auditElection()) {
            System.out.println("Vote attempts: " + voteAttempts);
            decryptionService.close();
        }
        else
            System.out.println("TestAuditor fails election");
    }

    private static void runECOTCompare() {
        Provider.setUseECOTWithSF(true);
        System.out.println("Comparing ECOT with " + compareVoters + " voters.");
        var startTime = System.nanoTime();
        vote();
        var endTime = System.nanoTime();
        var duration = Math.floorDiv(endTime - startTime, 1000000) ;
        System.out.println("ECOT with selective failures used " + duration + " milliseconds.");
        Provider.setUseECOTWithSF(false);
        startTime = System.nanoTime();
        vote();
        endTime = System.nanoTime();
        duration = Math.floorDiv(endTime - startTime, 1000000);
        System.out.println("ECOT without selective failures used " + duration + " milliseconds.");
    }

    private static void vote(){
        var testAuditor = new TestAuditor();
        var ballotBox = new BallotBox();
        ballotBox.setup(Provider.getDecryptionServiceConnector(), new TestAuditor(), Provider.getVoterConnector(), Provider.getVoteLogger());
        ballotBox.openBallotBox();

        for (int j = 0; j < compareVoters; j++) {
            var voter = new Voter();
            var userID = new Random().nextInt(numberOfVoters);
            voter.setup(Provider.getBallotBoxConnector(), Provider.getVoterVotingSession(), testAuditor, userID);
            voter.run();
        }

        ballotBox.closeBallotBox();
    }

    private static void countVoteAttempts(int voters) {
        voteAttempts += voters;
    }
}
