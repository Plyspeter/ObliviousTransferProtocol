package idgwpi.receiptgenerator.client;

public interface IRGClient {

    void setup (IRGClientCommunicator clientTransmitter, IRGClientLogic clientLogic, int choice);

    byte[] getReceipt();
}
