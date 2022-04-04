package idgwpi.receiptgenerator.client;

import idgwpi.helpers.Connection;
import idgwpi.receiptgenerator.dtos.*;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;

public class RGClientCommunicatorECOTWithoutSF implements IRGClientCommunicatorECOTWithoutSF {
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
            throw new RuntimeException("Transmission of step1 in RGClientCommunicatorECOTWithoutSF failed:\\n" + e);
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
            throw new RuntimeException("Transmission of step3 in RGClientCommunicatorECOTWithoutSF failed:\\n" + e);
        }
    }

    @Override
    public void transmitStep5(int d) {
        try {
            connection.getOutputStream().writeObject(new ECOTStep5WithoutSF(d));
            connection.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Transmission of step5 in RGClientCommunicatorECOTWithoutSF failed:\\n" + e);
        }
    }

    @Override
    public ECOTStep2WithoutSF receiveStep2() {
        try {
            return (ECOTStep2WithoutSF) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Receiving of step2 in RGClientCommunicatorECOTWithoutSF failed:\\n" + e);
        }
    }

    @Override
    public ECOTStep4 receiveStep4() {
        try {
            return (ECOTStep4) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Receiving of step4 in RGClientCommunicatorECOTWithoutSF failed:\\n" + e);
        }
    }

    @Override
    public ECOTStep6WithoutSF receiveStep6() {
        try {
            return (ECOTStep6WithoutSF) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Receiving of step6 in RGClientCommunicatorECOTWithoutSF failed:\\n" + e);
        }
    }
}
