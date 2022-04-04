package idgwpi.votingsimulator.ballotbox;

import idgwpi.votingsimulator.dtos.ElectionResultMessage;
import idgwpi.votingsimulator.dtos.VoteMessage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DecryptionServiceConnector implements IDecryptionServiceConnector {
    private String decryptionServiceAddress;
    private int decryptionServicePort;
    private Socket connection;

    @Override
    public void setup(String decryptionServiceAddress, int decryptionServicePort) {
        this.decryptionServiceAddress = decryptionServiceAddress;
        this.decryptionServicePort = decryptionServicePort;
    }

    @Override
    public void transmit(VoteMessage[] votes) {
        try {
            var outputStream = new ObjectOutputStream(new DataOutputStream(connection.getOutputStream()));
            outputStream.writeObject(new ElectionResultMessage(votes));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Network error in DecryptionServiceConnector:\n" + e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (IOException e) {
            throw new RuntimeException("Network error in DecryptionServiceConnector:\n" + e);
        }
    }

    @Override
    public void connect() {
        try {
            connection = new Socket(decryptionServiceAddress, decryptionServicePort);
        } catch (IOException e) {
            throw new RuntimeException("Network error in DecryptionServiceConnector:\n" + e);
        }
    }
}
