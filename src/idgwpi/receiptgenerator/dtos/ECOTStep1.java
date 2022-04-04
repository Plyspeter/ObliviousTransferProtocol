package idgwpi.receiptgenerator.dtos;

import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;

import java.io.Serializable;

public class ECOTStep1 implements Serializable {
    private byte[] s;
    private byte[] pk0;

    public ECOTStep1(byte[] s, ECPoint pk0) {
        this.s = s;
        this.pk0 = pk0.getEncoded(true);
    }

    public byte[] getS() {
        return s;
    }

    public ECPoint getPk0() {
        return Constants.getGroup().getCurve().decodePoint(pk0);
    }
}
