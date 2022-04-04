package idgwpi.receiptgenerator.server;

import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.dtos.ECOTStep1;
import idgwpi.receiptgenerator.dtos.ECOTStep3;

public interface IRGServerCommunicatorECOTWithSF extends IRGServerCommunicator {

    /**
     * Send (ch, ct0, ct1) to Bob
     */
    void transmitStep2(byte[] ch, CipherText ct0, CipherText ct1);

    /**
     * Send (m~0, m~1, p'0, p'1) to Bob
     */
    void transmitStep4(byte[] m0_wave, byte[] m1_wave, byte[] p0_mark, byte[] p1_mark);

    ECOTStep1 receiveStep1();

    ECOTStep3 receiveStep3();
}
