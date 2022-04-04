package idgwpi.receiptgenerator.client;

import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Arrays;

public class RGClientECOTWithoutSF implements IRGClient {

    //Setup
    private IRGClientCommunicatorECOTWithoutSF clientCommunicator;
    private IRGClientLogicECOT clientLogic;
    private int choice;

    //Step 1
    private int c_mark;
    private ECPoint pkc;
    private BigInteger sk;
    private ECPoint pk1MinusC;

    //Step 2
    private byte[] ch;
    private CipherText ct0;
    private CipherText ct1;
    private CipherText ct0H;
    private CipherText ct1H;

    //Step 3

    //Step 4
    private byte[] m0_wave;
    private byte[] m1_wave;
    private ECPoint p0;
    private ECPoint p1;

    //Step 5
    private byte[] mH_c;

    //Step 6
    private byte[] m0_mark;
    private byte[] m1_mark;

    @Override
    public void setup (IRGClientCommunicator clientCommunicator, IRGClientLogic clientLogic, int choice){
        this.clientLogic = (IRGClientLogicECOT) clientLogic;
        this.clientCommunicator = (IRGClientCommunicatorECOTWithoutSF) clientCommunicator;
        this.choice = choice;
        this.clientLogic.setup(Provider.getCryptoSystem());
    }

    @Override
    public byte[] getReceipt() {
        step1();

        var step2 = clientCommunicator.receiveStep2();
        receiveStep2(step2.getCh(), step2.getCt0(), step2.getCt1(), step2.getCt0H(), step2.getCt1H());

        step3();

        var step4 = clientCommunicator.receiveStep4();
        receiveStep4(step4.getM0_wave(), step4.getM1_wave(), step4.getP0_mark(), step4.getP1_mark());

        step5();

        var step6 = clientCommunicator.receiveStep6();
        receiveStep6(step6.getM0_mark(), step6.getM1_mark());

        return step7();
    }

    public void step1() {
        c_mark = clientLogic.sampleRandomBit();

        var keyGenerator = clientLogic.getKeyGenerator();
        pkc = keyGenerator.getPk();
        sk = keyGenerator.getSk();

        var s = clientLogic.sampleRandomBinaryString();
        var q = clientLogic.computeQ(s);
        pk1MinusC = clientLogic.computePK1MinusC(q, pkc);

        clientCommunicator.transmitStep1(c_mark, s, pkc, pk1MinusC);
    }

    public void step3() {
        var ctc = clientLogic.getCTC(c_mark, ct0, ct1);
        var pc = clientLogic.computePC(pkc, sk, ctc);
        var pc_mark = clientLogic.computePCMark(pkc, pc);
        var pc_mark2 = clientLogic.computePCMark2(pc_mark);
        var chr = clientLogic.computeCHR(c_mark, pc_mark2, ch);

        clientCommunicator.transmitStep3(chr);
    }

    public void step5(){
        var pk0 = clientLogic.computePk0(c_mark, pkc, pk1MinusC);
        if(!clientLogic.verifyCT(ct0, p0, pk0))
            clientLogic.abort("Bob: Could not verify CT0");

        var pk1 = clientLogic.computePk1(c_mark, pkc, pk1MinusC);
        if(!clientLogic.verifyCT(ct1, p1, pk1))
            clientLogic.abort("Bob: Could not verify CT1");

        var p0_mark = clientLogic.computePCMark(pk0, p0);
        var p1_mark = clientLogic.computePCMark(pk1, p1);

        var p0_mark2 = clientLogic.computePCMark2(p0_mark);
        var p1_mark2 = clientLogic.computePCMark2(p1_mark);

        if (!clientLogic.verifyCH(ch, p0_mark2, p1_mark2))
            clientLogic.abort("Bob: Could not verify CH");

        var ctcH = clientLogic.getCTC(c_mark, ct0H, ct1H);
        var pcH = clientLogic.computePC(pkc, sk, ctcH);
        var pc_wave = clientLogic.computePCWave(pkc, pcH);
        mH_c = clientLogic.obtainMessageC(c_mark, pc_wave, m0_wave, m1_wave);

        var d = clientLogic.calculateD(choice, c_mark);

        clientCommunicator.transmitStep5(d);
    }

    public byte[] step7() {
        return clientLogic.obtainMessageC(choice, mH_c, m0_mark, m1_mark);
    }

    public void receiveStep2(byte[] ch, CipherText ct0, CipherText ct1, CipherText ct0H, CipherText ct1H) {
        this.ch = ch;
        this.ct0 = ct0;
        this.ct1 = ct1;
        this.ct0H = ct0H;
        this.ct1H = ct1H;
    }

    public void receiveStep4(byte[] m0_wave, byte[] m1_wave, byte[] p0, byte[] p1) {
        this.m0_wave = m0_wave;
        this.m1_wave = m1_wave;
        this.p0 = Constants.getGroup().getCurve().decodePoint(p0);
        this.p1 = Constants.getGroup().getCurve().decodePoint(p1);
    }

    public void receiveStep6(byte[] m0_mark, byte[] m1_mark) {
        this.m0_mark = m0_mark;
        this.m1_mark = m1_mark;
    }
}
