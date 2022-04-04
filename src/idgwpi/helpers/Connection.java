package idgwpi.helpers;

import java.io.*;
import java.net.Socket;

public class Connection {
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Socket socket;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        outputStream = new ObjectOutputStream(new DataOutputStream(socket.getOutputStream()));
        inputStream = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void close() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }
}
