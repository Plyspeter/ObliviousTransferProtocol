package idgwpi.receiptgenerator.client;

import idgwpi.receiptgenerator.dtos.ECOTStep2;
import idgwpi.receiptgenerator.dtos.ECOTStep4;
import org.bouncycastle.math.ec.ECPoint;

public interface IRGClientCommunicatorECOTWithSF extends IRGClientCommunicator {

    /**
     * Send (s, pk0) to Alice
     */
    void transmitStep1(int choice, byte[] s, ECPoint pkc, ECPoint pkcMinus1 );

    /**
     * Send (chr) to Alice
     */
    void transmitStep3(byte[] chr);

    ECOTStep2 receiveStep2();

    ECOTStep4 receiveStep4();
}
