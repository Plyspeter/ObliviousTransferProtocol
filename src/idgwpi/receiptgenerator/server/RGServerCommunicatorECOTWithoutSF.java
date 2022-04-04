package idgwpi.receiptgenerator.server;

import idgwpi.helpers.Connection;
import idgwpi.receiptgenerator.dtos.*;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;

public class RGServerCommunicatorECOTWithoutSF implements IRGServerCommunicatorECOTWithoutSF {
    private Connection connection;

    @Override
    public void setup(Connection connection) {
        this.connection = connection;
    }

    /**
     * Send (ch, ct0, ct1) to Bob
     */
    @Override
    public void transmitStep2(byte[] ch, CipherText ct0, CipherText ct1, CipherText ct0H, CipherText ct1H) {
        try {
            connection.getOutputStream().writeObject(new ECOTStep2WithoutSF(ch, ct0, ct1, ct0H, ct1H));
            connection.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Transmission of step2 in RGServerCommunicatorECOTWithoutSF failed:\\n" + e);
        }
    }

    /**
     * Send (m~0, m~1, p0, p1) to Bob
     */
    @Override
    public void transmitStep4(byte[] m0_wave, byte[] m1_wave, ECPoint p0, ECPoint p1) {
        try {
            connection.getOutputStream().writeObject(new ECOTStep4(m0_wave, m1_wave, p0.getEncoded(true), p1.getEncoded(true)));
            connection.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Transmission of step4 in RGServerCommunicatorECOTWithoutSF failed:\\n" + e);
        }
    }

    /**
     * Send (m_0', m_1') to Bob
     */
    @Override
    public void transmitStep6(byte[] m0_mark, byte[] m1_mark) {
        try {
            connection.getOutputStream().writeObject(new ECOTStep6WithoutSF(m0_mark, m1_mark));
            connection.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("Transmission of step6 in RGServerCommunicatorECOTWithoutSF failed:\\n" + e);
        }
    }

    @Override
    public ECOTStep1 receiveStep1(){
        try {
            return (ECOTStep1) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("RGServerCommunicatorECOTWithoutSF connection error in step 1:\n" + e.getMessage());
        }
    }

    @Override
    public ECOTStep3 receiveStep3() {
        try {
            return (ECOTStep3) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("RGServerCommunicatorECOTWithoutSF connection error in step 3:\n" + e.getMessage());
        }

    }

    @Override
    public ECOTStep5WithoutSF receiveStep5() {
        try {
            return (ECOTStep5WithoutSF) connection.getInputStream().readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("RGServerCommunicatorECOTWithoutSF connection error in step 5:\n" + e.getMessage());
        }

    }
}
