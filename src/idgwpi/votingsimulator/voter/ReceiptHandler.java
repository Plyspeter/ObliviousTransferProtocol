package idgwpi.votingsimulator.voter;

import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.helpers.Connection;
import idgwpi.receiptgenerator.client.IRGClient;
import idgwpi.votingsimulator.dtos.Ballot;
import org.bouncycastle.math.ec.ECPoint;

public class ReceiptHandler implements IReceiptHandler {
    private IRGClient receiptGeneratorClient;
    private ECPoint pointChoice;
    private int choice;

    @Override
    public void setup(IRGClient receiptGeneratorClient) {
        this.receiptGeneratorClient = receiptGeneratorClient;
    }

    @Override
    public void getReceipt(Connection connection, int choice) {
        this.choice = choice;
        var communicator = Provider.getClientCommunicator();
        communicator.setup(connection);
        receiptGeneratorClient.setup(communicator, Provider.getClientLogic(), choice);

        var pointChoiceByte = receiptGeneratorClient.getReceipt();

        pointChoice = Constants.getGroup().getCurve().decodePoint(pointChoiceByte);
    }

    @Override
    public boolean verifyReceipt(Ballot ballot) {
        return ballot.getPointChoices()[choice].equals(pointChoice);
    }
}
