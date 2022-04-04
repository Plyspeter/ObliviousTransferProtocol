package idgwpi.receiptgenerator.cryptosystem;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public interface IHash {

    void setup(IHashLogic hashLogic);
    BigInteger henc(ECPoint message, BigInteger order);
    ECPoint hkey(byte[] random);
    byte[] hch(ECPoint key, ECPoint p);
    byte[] hch(byte[] p_mark);
    byte[] hpad(ECPoint pk, ECPoint p);
}
