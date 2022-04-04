package idgwpi.votingsimulator.ballotbox;

import idgwpi.receiptgenerator.server.IRGServer;
import idgwpi.helpers.Connection;
import org.bouncycastle.math.ec.ECPoint;

public interface IReceiptGenerator {
    void setup(IRGServer receiptGeneratorServer, Connection connection);

    void generate(ECPoint[] ecPoints);
}
