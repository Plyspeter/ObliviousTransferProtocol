package idgwpi.votingsimulator.voter;

import idgwpi.helpers.Connection;
import idgwpi.receiptgenerator.client.IRGClient;
import idgwpi.votingsimulator.dtos.Ballot;

import java.io.IOException;

public interface IReceiptHandler {
    void setup(IRGClient receiptGeneratorClient);

    void getReceipt(Connection connection, int choice) throws IOException, ClassNotFoundException;

    boolean verifyReceipt(Ballot ballot);
}
