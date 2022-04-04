package idgwpi.receiptgenerator.dtos;

import java.io.Serializable;

public class ECOTStep4 implements Serializable {
    private byte[] m0_wave;
    private byte[] m1_wave;
    private byte[] p0_mark;
    private byte[] p1_mark;

    public ECOTStep4(byte[] m0_wave, byte[] m1_wave, byte[] p0_mark, byte[] p1_mark) {
        this.m0_wave = m0_wave;
        this.m1_wave = m1_wave;
        this.p0_mark = p0_mark;
        this.p1_mark = p1_mark;
    }

    public byte[] getM0_wave() {
        return m0_wave;
    }

    public byte[] getM1_wave() {
        return m1_wave;
    }

    public byte[] getP0_mark() {
        return p0_mark;
    }

    public byte[] getP1_mark() {
        return p1_mark;
    }
}
