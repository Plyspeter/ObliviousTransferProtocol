package idgwpi.receiptgenerator.dtos;

import java.io.Serializable;

public class ECOTStep2 implements Serializable {
    private byte[] ch;
    private CipherText ct0;
    private CipherText ct1;

    public ECOTStep2(byte[] ch, CipherText ct0, CipherText ct1) {
        this.ch = ch;
        this.ct0 = ct0;
        this.ct1 = ct1;
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
}
