package idgwpi.receiptgenerator.server;

import idgwpi.receiptgenerator.dtos.*;
import idgwpi.helpers.Connection;

import java.io.IOException;

public class RGServerCommunicatorECOTWithSF implements IRGServerCommunicatorECOTWithSF {
    private Connection connection;

    @Override
    public void setup(Connection connection) {
        this.connection = connection;
    }

    /**
     * Send (ch, ct0, ct1) to Bob
     */
    @Override
    public void transmitStep2(byte[] ch, CipherText ct0, CipherText ct1) {
        try {
            connection.getOutputStream().writeObject(new ECOTStep2(ch, ct0, ct1));
            connection.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Transmission of step2 failed in RGServerCommunicatorECOTWithSF:\\n" + e);
        }
    }

    /**
     * Send (m~0, m~1, p'0, p'1) to Bob
     */
    @Override
    public void transmitStep4(byte[] m0_wave, byte[] m1_wave, byte[] p0_mark, byte[] p1_mark) {
        try {
            connection.getOutputStream().writeObject(new ECOTStep4(m0_wave, m1_wave, p0_mark, p1_mark));
            connection.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Transmission of step4 failed in RGServerCommunicatorECOTWithSF:\\n" + e);
        }
    }

    @Override
    public ECOTStep1 receiveStep1(){
        try {
            return (ECOTStep1) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("RGServerCommunicatorECOTWithSF connection error in step 1:\n" + e.getMessage());
        }
    }

    @Override
    public ECOTStep3 receiveStep3(){
        try {
            return (ECOTStep3) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("RGServerCommunicatorECOTWithSF connection error in step 3:\n" + e.getMessage());
        }

    }
}
