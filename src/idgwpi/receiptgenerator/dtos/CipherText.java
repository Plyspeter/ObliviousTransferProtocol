package idgwpi.receiptgenerator.dtos;

import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class CipherText implements Serializable {
    private final byte[] cipher1;
    private final byte[] cipher2;

    public CipherText(ECPoint cipher1, ECPoint cipher2) {
        this.cipher1 = cipher1.getEncoded(true);
        this.cipher2 = cipher2.getEncoded(true);
    }

    public ECPoint getCipher1() {
        return Constants.getGroup().getCurve().decodePoint(cipher1);
    }

    public ECPoint getCipher2() {
        return Constants.getGroup().getCurve().decodePoint(cipher2);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof CipherText))
            return false;
        var that = (CipherText) obj;

        return Arrays.equals(that.cipher1, this.cipher1) && Arrays.equals(that.cipher2, this.cipher2);
    }
}
