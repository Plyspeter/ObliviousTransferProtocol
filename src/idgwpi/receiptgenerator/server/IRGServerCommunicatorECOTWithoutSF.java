package idgwpi.receiptgenerator.server;

import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.dtos.ECOTStep1;
import idgwpi.receiptgenerator.dtos.ECOTStep3;
import idgwpi.receiptgenerator.dtos.ECOTStep5WithoutSF;
import org.bouncycastle.math.ec.ECPoint;

public interface IRGServerCommunicatorECOTWithoutSF extends IRGServerCommunicator {

    /**
     * Send (ch, ct0, ct1) to Bob
     */
    void transmitStep2(byte[] ch, CipherText ct0, CipherText ct1, CipherText ct0H, CipherText ct1H);

    /**
     * Send (m~0, m~1, p0, p1) to Bob
     */
    void transmitStep4(byte[] m0_wave, byte[] m1_wave, ECPoint p0, ECPoint p1);

    /**
     * Send (m_0', m_1') to Bob
     */
    void transmitStep6(byte[] m0_mark, byte[] m1_mark);

    ECOTStep1 receiveStep1();

    ECOTStep3 receiveStep3();

    ECOTStep5WithoutSF receiveStep5();
}
