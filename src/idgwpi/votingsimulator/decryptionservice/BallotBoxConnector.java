package idgwpi.votingsimulator.decryptionservice;

import idgwpi.votingsimulator.dtos.ElectionResultMessage;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class BallotBoxConnector implements IBallotBoxConnector {
    private boolean close;
    private ServerSocket serverSocket;
    private ArrayList<ElectionResultMessage> results;

    @Override
    public void setup(int decryptionServicePort) {
        try {
            serverSocket = new ServerSocket(decryptionServicePort);
        } catch (SocketException e) {
            //Do nothing
        } catch (IOException e) {
            throw new RuntimeException("BallotBoxConnector decryptionServicePort failed to initialise:\n" + e);
        }
        results = new ArrayList<>();
    }

    @Override
    public ArrayList<ElectionResultMessage> getResults() {
        return results;
    }

    @Override
    public void close() {
        close = true;
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("BallotBoxConnector failed to close socket properly:\n" + e);
        }
    }

    @Override
    public void start() {
        while (keepRunning()) {
            try {
                handleConnection(serverSocket.accept());
            } catch (SocketException e) {
                //Do nothing
            } catch (IOException e) {
                throw new RuntimeException("BallotBoxConnector decryptionServicePort failed to accept:\n" + e);
            }
        }
    }

    private void handleConnection(Socket socket) {
        try {
            var inputStream = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
            results.add((ElectionResultMessage) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("BallotBoxConnector failure with handling connection:\n" + e);
        }
    }

    private boolean keepRunning() {
        return !close;
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
        start();
    }
}
