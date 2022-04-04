package idgwpi.receiptgenerator.client;

import idgwpi.receiptgenerator.dtos.ECOTStep1;
import idgwpi.receiptgenerator.dtos.ECOTStep2;
import idgwpi.receiptgenerator.dtos.ECOTStep3;
import idgwpi.receiptgenerator.dtos.ECOTStep4;
import idgwpi.helpers.Connection;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;

public class RGClientCommunicatorECOTWithSF implements IRGClientCommunicatorECOTWithSF {
    private Connection connection;

    @Override
    public void setup(Connection connection) {
        this.connection = connection;
    }

    /**
     * Send (s, pk0) to Alice
     */
    @Override
    public void transmitStep1(int choice, byte[] s, ECPoint pkc, ECPoint pkcMinus1) {
        try {
            if ((choice == 0)) {
                connection.getOutputStream().writeObject(new ECOTStep1(s, pkc));
            } else {
                connection.getOutputStream().writeObject(new ECOTStep1(s, pkcMinus1));
            }
            connection.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Transmission of step1 in RGClientCommunicatorECOTWS failed:\\n" + e);
        }
    }

    /**
     * Send (chr) to Alice
     */
    @Override
    public void transmitStep3(byte[] chr) {
        try {
            connection.getOutputStream().writeObject(new ECOTStep3(chr));
            connection.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Transmission of step3 in RGClientCommunicatorECOTWS failed:\\n" + e);
        }
    }

    @Override
    public ECOTStep2 receiveStep2() {
        try {
            return (ECOTStep2) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Receiving of step3 in RGClientCommunicatorECOTWS failed:\\n" + e);
        }
    }

    @Override
    public ECOTStep4 receiveStep4() {
        try {
            return (ECOTStep4) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Receiving of step3 in RGClientCommunicatorECOTWS failed:\\n" + e);
        }
    }
}
