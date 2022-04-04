package idgwpi.receiptgenerator.cryptosystem;

import org.bouncycastle.math.ec.ECPoint;

public interface IHashLogic {

    byte[][] computeBinaryStrings(byte[] prefixBytes, ECPoint... points);
    int computeConcatenatedBinaryStringLength(byte[][] binaryStrings);
    byte[] computeConcatenatedBinaryString(byte[][] binaryStrings, int length);
    byte[] hash(byte[] data);
}
