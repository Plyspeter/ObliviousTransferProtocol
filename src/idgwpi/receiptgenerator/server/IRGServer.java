package idgwpi.receiptgenerator.server;

import org.bouncycastle.math.ec.ECPoint;

public interface IRGServer {
    void setup(IRGServerCommunicator aliceTransmitter, IRGServerLogic serverLogic, ECPoint[] messages);

    void generateReceipt();
}
