package idgwpi.receiptgenerator.client;

import idgwpi.receiptgenerator.dtos.ECOTStep2WithoutSF;
import idgwpi.receiptgenerator.dtos.ECOTStep4;
import idgwpi.receiptgenerator.dtos.ECOTStep6WithoutSF;
import org.bouncycastle.math.ec.ECPoint;

public interface IRGClientCommunicatorECOTWithoutSF extends IRGClientCommunicator {

    /**
     * Send (s, pk0) to Alice
     */
    void transmitStep1(int choice, byte[] s, ECPoint pkc, ECPoint pkcMinus1 );

    /**
     * Send (chr) to Alice
     */
    void transmitStep3(byte[] chr);

    void transmitStep5(int d);

    ECOTStep2WithoutSF receiveStep2();

    ECOTStep4 receiveStep4();

    ECOTStep6WithoutSF receiveStep6();
}
