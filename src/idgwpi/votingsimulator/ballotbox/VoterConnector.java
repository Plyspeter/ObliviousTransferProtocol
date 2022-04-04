package idgwpi.votingsimulator.ballotbox;

import idgwpi.globals.Provider;
import idgwpi.votingsimulator.dtos.AuthenticationMessage;
import idgwpi.helpers.Connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class VoterConnector implements IVoterConnector {
    private ServerSocket serverSocket;
    private boolean close;

    @Override
    public void setup(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);
        close = false;
    }

    @Override
    public void open() {
        while (keepRunning()) {
            try {
                initConnection(serverSocket.accept());
            } catch (SocketException e) {
                //Do nothing
            } catch (IOException e) {
                throw new RuntimeException("VoterConnector serverSocket failed to accept:\n" + e);
            }
        }
    }

    @Override
    public void close() {
        close = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("VoterConnector clientSocket failed to close:\n" + e);
        }
    }

    private boolean keepRunning() {
        return !close;
    }

    public void initConnection(Socket clientSocket){
        try {
            var connection = new Connection(clientSocket);
            var userID = (AuthenticationMessage) connection.getInputStream().readObject();
            var ballotBoxVotingSession = new BallotBoxVotingSession(connection,
                    Provider.getReceiptGenerator(), Provider.getVoteLogger(), userID.getUserID());
            var session = new Thread(ballotBoxVotingSession);
            session.start();
        } catch (IOException | ClassNotFoundException e) {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException("VoterConnector clientSocket failed to close:\n" + e);
            }
            throw new RuntimeException("VoterConnector connection failed to be created:\n" + e);
        }
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        open();
    }
}
