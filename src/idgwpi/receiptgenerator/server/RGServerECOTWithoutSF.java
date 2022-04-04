package idgwpi.receiptgenerator.server;

import idgwpi.globals.Provider;
import org.bouncycastle.math.ec.ECPoint;

import java.util.Arrays;

public class RGServerECOTWithoutSF implements IRGServer {

    //Setup
    private IRGServerCommunicatorECOTWithoutSF serverTransmitter;
    private IRGServerLogicECOT serverLogic;
    private ECPoint message0;
    private ECPoint message1;

    //Step 1
    private ECPoint pk0;
    private byte[] s;

    //Step 2
    private ECPoint pk1;
    private ECPoint p0Point;
    private ECPoint p1Point;
    private ECPoint p0HPoint;
    private ECPoint p1HPoint;
    private byte[] p0_mark2;

    //Step 3
    private byte[] chr;

    //Step 4
    private byte[] m0H;
    private byte[] m1H;

    //Step 5
    private int d;

    @Override
    public void setup(IRGServerCommunicator serverTransmitter, IRGServerLogic serverLogic, ECPoint[] messages){
        if (messages.length != 2)
            throw new RuntimeException("Wrong number of messages given to RGServerWithoutSF");
        this.serverLogic = (IRGServerLogicECOT) serverLogic;
        this.serverTransmitter = (IRGServerCommunicatorECOTWithoutSF) serverTransmitter;
        this.message0 = messages[0];
        this.message1 = messages[1];
        this.serverLogic.setup(Provider.getCryptoSystem());
    }

    @Override
    public void generateReceipt() {
        var step1 = serverTransmitter.receiveStep1();
        receiveStep1(step1.getS(), step1.getPk0());
        step2();
        var step3 = serverTransmitter.receiveStep3();
        receiveStep3(step3.getChr());
        step4();
        var step5 = serverTransmitter.receiveStep5();
        receiveStep5(step5.getD());
        step6();
    }

    public void step2(){
        var q = serverLogic.computeQ(s);
        pk1 = serverLogic.computePK1(q, pk0);

        var p0 = serverLogic.sampleP();
        var p1 = serverLogic.sampleP();
        var p0_H = serverLogic.sampleP();
        var p1_H = serverLogic.sampleP();
        p0Point = serverLogic.pToECPoint(p0);
        p1Point = serverLogic.pToECPoint(p1);
        p0HPoint = serverLogic.pToECPoint(p0_H);
        p1HPoint = serverLogic.pToECPoint(p1_H);

        var p0_mark = serverLogic.computePMark(pk0, p0Point);
        var p1_mark = serverLogic.computePMark(pk1, p1Point);

        p0_mark2 = serverLogic.computePMark2(p0_mark);
        var p1_mark2 = serverLogic.computePMark2(p1_mark);

        var ch = serverLogic.computeCH(p0_mark2, p1_mark2);

        var ct0 = serverLogic.computeCipherText(pk0, p0Point);
        var ct1 = serverLogic.computeCipherText(pk1, p1Point);
        var ct0H = serverLogic.computeCipherText(pk0, p0HPoint);
        var ct1H = serverLogic.computeCipherText(pk1, p1HPoint);

        serverTransmitter.transmitStep2(ch, ct0, ct1, ct0H, ct1H);
    }

    public void step4(){
        if(!serverLogic.verifyCHR(chr, p0_mark2))
            serverLogic.abort("Alice: CHR not equal p''0");

        var p0_wave = serverLogic.computePWave(pk0, p0HPoint);
        var p1_wave = serverLogic.computePWave(pk1, p1HPoint);

        m0H = serverLogic.sampleRandomBinaryString(message0.getEncoded(true).length);
        m1H = serverLogic.sampleRandomBinaryString(message1.getEncoded(true).length);

        var m0_wave = serverLogic.computeMWave(p0_wave, m0H);
        var m1_wave = serverLogic.computeMWave(p1_wave, m1H);

        serverTransmitter.transmitStep4(m0_wave, m1_wave, p0Point, p1Point);
    }

    public void step6() {
        var mdH =  serverLogic.computeMdH(d, m0H, m1H);
        var mdH2 = serverLogic.computeM1dH(d, m0H, m1H);

        var m0_mark = serverLogic.computeMMark(mdH, message0);
        var m1_mark = serverLogic.computeMMark(mdH2, message1);

        serverTransmitter.transmitStep6(m0_mark, m1_mark);
    }

    public void receiveStep1(byte[] s, ECPoint pk0) {
        this.s = s;
        this.pk0 = pk0;
    }

    public void receiveStep3(byte[] chr) {
        this.chr = chr;
    }

    public void receiveStep5(int d) {
        this.d = d;
    }
}
