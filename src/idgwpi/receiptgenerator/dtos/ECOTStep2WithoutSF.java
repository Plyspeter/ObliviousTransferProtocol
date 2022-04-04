package idgwpi.receiptgenerator.dtos;

import java.io.Serializable;

public class ECOTStep2WithoutSF implements Serializable {
    private byte[] ch;
    private CipherText ct0;
    private CipherText ct1;
    private final CipherText ct0H;
    private final CipherText ct1H;

    public ECOTStep2WithoutSF(byte[] ch, CipherText ct0, CipherText ct1, CipherText ct0H, CipherText ct1H) {
        this.ch = ch;
        this.ct0 = ct0;
        this.ct1 = ct1;
        this.ct0H = ct0H;
        this.ct1H = ct1H;
    }

    public byte[] getCh() {
        return ch;
    }

    public CipherText getCt0() {
        return ct0;
    }

    public CipherText getCt1() {
        return ct1;
    }

    public CipherText getCt0H() {
        return ct0H;
    }

    public CipherText getCt1H() {
        return ct1H;
    }
}
