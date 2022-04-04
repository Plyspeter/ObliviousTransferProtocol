package idgwpi.receiptgenerator.server;

import idgwpi.globals.Provider;
import org.bouncycastle.math.ec.ECPoint;

public class RGServerECOTWithSF implements IRGServer {
    private IRGServerCommunicatorECOTWithSF serverTransmitter;
    private IRGServerLogicECOT serverLogic;

    private ECPoint message0;
    private ECPoint message1;
    private ECPoint pk0;
    private ECPoint pk1;
    private ECPoint p0Point;
    private ECPoint p1Point;

    private byte[] s;

    private byte[] p0_mark;
    private byte[] p1_mark;

    private byte[] p0_mark2;
    private byte[] chr;

    public void setup(IRGServerCommunicator serverTransmitter, IRGServerLogic serverLogic, ECPoint[] messages){
        if (messages.length != 2)
            throw new RuntimeException("Wrong number of messages given to RGServerWithSF");
        this.serverLogic = (IRGServerLogicECOT) serverLogic;
        this.serverTransmitter = (IRGServerCommunicatorECOTWithSF) serverTransmitter;
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
    }

    public void step2(){
        var q = serverLogic.computeQ(s);
        pk1 = serverLogic.computePK1(q, pk0);

        var p0 = serverLogic.sampleP();
        var p1 = serverLogic.sampleP();
        p0Point = serverLogic.pToECPoint(p0);
        p1Point = serverLogic.pToECPoint(p1);

        p0_mark = serverLogic.computePMark(pk0, p0Point);
        p1_mark = serverLogic.computePMark(pk1, p1Point);

        p0_mark2 = serverLogic.computePMark2(p0_mark);
        var p1_mark2 = serverLogic.computePMark2(p1_mark);

        var ch = serverLogic.computeCH(p0_mark2, p1_mark2);

        var ct0 = serverLogic.computeCipherText(pk0, p0Point);
        var ct1 = serverLogic.computeCipherText(pk1, p1Point);

        serverTransmitter.transmitStep2(ch, ct0, ct1);
    }

    public void step4(){
        if(!serverLogic.verifyCHR(chr, p0_mark2))
            serverLogic.abort("Alice: CHR not equal p''0");
        var p0_wave = serverLogic.computePWave(pk0, p0Point);
        var p1_wave = serverLogic.computePWave(pk1, p1Point);
        var m0_wave = serverLogic.computeMWave(p0_wave, message0);
        var m1_wave = serverLogic.computeMWave(p1_wave, message1);

        serverTransmitter.transmitStep4(m0_wave, m1_wave, p0_mark, p1_mark);
    }

    public void receiveStep1(byte[] s, ECPoint pk0) {
        this.s = s;
        this.pk0 = pk0;
    }

    public void receiveStep3(byte[] chr) {
        this.chr = chr;
    }
}
