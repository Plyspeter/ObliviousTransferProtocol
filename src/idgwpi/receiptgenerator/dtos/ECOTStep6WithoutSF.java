package idgwpi.receiptgenerator.dtos;

import java.io.Serializable;

public class ECOTStep6WithoutSF implements Serializable {
    private final byte[] m0_mark;
    private final byte[] m1_mark;

    public ECOTStep6WithoutSF(byte[] m0_mark, byte[] m1_mark) {
        this.m0_mark = m0_mark;
        this.m1_mark = m1_mark;
    }

    public byte[] getM0_mark() {
        return m0_mark;
    }

    public byte[] getM1_mark() {
        return m1_mark;
    }
}
