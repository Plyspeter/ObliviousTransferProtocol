package idgwpi.receiptgenerator.cryptosystem;

import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class Hash implements IHash{
    private IHashLogic hashLogic;

    public void setup(IHashLogic hashLogic){
        this.hashLogic = hashLogic;
    }

    public BigInteger henc(ECPoint message, BigInteger order){
        var prefixBytes = "HENC".getBytes();
        var binaryStrings = hashLogic.computeBinaryStrings(prefixBytes, message);
        var concatenatedBinaryStringLength = hashLogic.computeConcatenatedBinaryStringLength(binaryStrings);
        var concatenatedBinaryString = hashLogic.computeConcatenatedBinaryString(binaryStrings, concatenatedBinaryStringLength);

        return new BigInteger(1, hashLogic.hash(concatenatedBinaryString)).mod(order);
    }

    public ECPoint hkey(byte[] random){
        var prefixBytes = "HKEY".getBytes();
        var binaryStrings = new byte[][]{prefixBytes, random};
        var concatenatedBinaryStringLength = hashLogic.computeConcatenatedBinaryStringLength(binaryStrings);
        var concatenatedBinaryString = hashLogic.computeConcatenatedBinaryString(binaryStrings, concatenatedBinaryStringLength);

        var modifier = new BigInteger(1, hashLogic.hash(concatenatedBinaryString));

        return Constants.getGroup().getG().multiply(modifier);
    }

    public byte[] hch(ECPoint key, ECPoint p){
        var prefixBytes = "HCH".getBytes();
        var binaryStrings = hashLogic.computeBinaryStrings(prefixBytes, key, p);
        var concatenatedBinaryStringLength = hashLogic.computeConcatenatedBinaryStringLength(binaryStrings);
        var concatenatedBinaryString = hashLogic.computeConcatenatedBinaryString(binaryStrings, concatenatedBinaryStringLength);

        return hashLogic.hash(concatenatedBinaryString);
    }

    public byte[] hch(byte[] p_mark){
        var prefixBytes = "HCH".getBytes();
        var binaryStrings = new byte[][]{prefixBytes, p_mark};
        var concatenatedBinaryStringLength = hashLogic.computeConcatenatedBinaryStringLength(binaryStrings);
        var concatenatedBinaryString = hashLogic.computeConcatenatedBinaryString(binaryStrings, concatenatedBinaryStringLength);

        return hashLogic.hash(concatenatedBinaryString);
    }

    public byte[] hpad(ECPoint pk, ECPoint p) {
        var prefixBytes = "HPAD".getBytes();
        var binaryStrings = hashLogic.computeBinaryStrings(prefixBytes, pk, p);
        var concatenatedBinaryStringLength = hashLogic.computeConcatenatedBinaryStringLength(binaryStrings);
        var concatenatedBinaryString = hashLogic.computeConcatenatedBinaryString(binaryStrings, concatenatedBinaryStringLength);

        return hashLogic.hash(concatenatedBinaryString);
    }
}
