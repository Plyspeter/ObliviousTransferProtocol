package idgwpi.receiptgenerator.client;

import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class RGClientECOTWithSF implements IRGClient {
    private IRGClientCommunicatorECOTWithSF clientCommunicator;
    private IRGClientLogicECOT clientLogic;
    private int choice;
    private BigInteger sk;

    private ECPoint pkc;

    private CipherText ct0;
    private CipherText ct1;

    private byte[] ch;
    private byte[] m0_wave;
    private byte[] m1_wave;
    private byte[] p0_mark;
    private byte[] p1_mark;
    private byte[] pc_mark;
    private byte[] pc_mark2;
    private ECPoint pc;

    public void setup (IRGClientCommunicator clientCommunicator, IRGClientLogic clientLogic, int choice){
        this.clientLogic = (IRGClientLogicECOT) clientLogic;
        this.clientCommunicator = (IRGClientCommunicatorECOTWithSF) clientCommunicator;
        this.choice = choice;
        this.clientLogic.setup(Provider.getCryptoSystem());
    }

    @Override
    public byte[] getReceipt(){
        step1();

        var step2 = clientCommunicator.receiveStep2();
        receiveStep2(step2.getCh(), step2.getCt0(), step2.getCt1());

        step3();

        var step4 = clientCommunicator.receiveStep4();
        receiveStep4(step4.getM0_wave(), step4.getM1_wave(), step4.getP0_mark(), step4.getP1_mark());

        return step5();
    }

    public void step1(){
        var keyGenerator = clientLogic.getKeyGenerator();
        pkc = keyGenerator.getPk();
        sk = keyGenerator.getSk();

        var s = clientLogic.sampleRandomBinaryString();
        var q = clientLogic.computeQ(s);
        var pk1MinusC = clientLogic.computePK1MinusC(q, pkc);

        clientCommunicator.transmitStep1(choice, s, pkc, pk1MinusC);
    }

    public void step3(){
        var ctc = clientLogic.getCTC(choice, ct0, ct1);
        pc = clientLogic.computePC(pkc, sk, ctc);
        pc_mark = clientLogic.computePCMark(pkc, pc);
        pc_mark2 = clientLogic.computePCMark2(pc_mark);
        var chr = clientLogic.computeCHR(choice, pc_mark2, ch);

        clientCommunicator.transmitStep3(chr);
    }

    public byte[] step5(){
        if(!clientLogic.verifyPCMark(choice, pc_mark, p0_mark, p1_mark))
            clientLogic.abort("Bob: Alice p'c not equal Bob p'c");

        var p1MinusC_mark2 = clientLogic.computeP1MinusCMark2(choice, p0_mark, p1_mark);

        if(!clientLogic.verifyCH(ch, p1MinusC_mark2, pc_mark2))
            clientLogic.abort("Bob: ch not equal p''1-c XOR p''c");

        var pc_wave = clientLogic.computePCWave(pkc, pc);

        return clientLogic.obtainMessageC(choice, pc_wave, m0_wave, m1_wave);
    }

    public void receiveStep2(byte[] ch, CipherText ct0, CipherText ct1) {
        this.ch = ch;
        this.ct0 = ct0;
        this.ct1 = ct1;
    }

    public void receiveStep4(byte[] m0_wave, byte[] m1_wave, byte[] p0_mark, byte[] p1_mark) {
        this.m0_wave = m0_wave;
        this.m1_wave = m1_wave;
        this.p0_mark = p0_mark;
        this.p1_mark = p1_mark;
    }
}
