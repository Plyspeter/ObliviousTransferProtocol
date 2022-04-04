package idgwpi.votingsimulator.ballotbox;

import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.server.IRGServer;
import idgwpi.helpers.Connection;
import org.bouncycastle.math.ec.ECPoint;

public class ReceiptGenerator implements IReceiptGenerator {
    private IRGServer receiptGeneratorServer;
    private Connection connection;

    @Override
    public void setup(IRGServer receiptGeneratorServer, Connection connection) {
        this.receiptGeneratorServer = receiptGeneratorServer;
        this.connection = connection;
    }

    @Override
    public void generate(ECPoint[] ecPoints) {
        var communicator = Provider.getServerCommunicator();
        communicator.setup(connection);
        receiptGeneratorServer.setup(communicator, Provider.getServerLogic(), ecPoints);
        receiptGeneratorServer.generateReceipt();
    }
}
