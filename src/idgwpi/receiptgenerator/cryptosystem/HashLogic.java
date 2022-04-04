package idgwpi.receiptgenerator.cryptosystem;

import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class HashLogic implements IHashLogic {
    private String hashAlgorithm;
    private String securityProvider;

    public HashLogic(){
        Security.addProvider(Constants.getSecurityProvider());
        hashAlgorithm = Constants.getHashAlgorithm();
        securityProvider = Constants.getSecurityProviderString();
    }

    public byte[][] computeBinaryStrings(byte[] prefixBytes, ECPoint... points) {
        var binaryStrings = new byte[points.length + 1][];
        binaryStrings[0] = prefixBytes;
        for (int i = 1; i < binaryStrings.length; i++){
            binaryStrings[i] = points[i-1].getEncoded(true);
        }

        return binaryStrings;
    }

    public int computeConcatenatedBinaryStringLength(byte[][] binaryStrings) {
        var concatenatedBinaryStringLength = 0;

        for (byte[] binaryString : binaryStrings){
            concatenatedBinaryStringLength+= binaryString.length;
        }

        return concatenatedBinaryStringLength;
    }

    public byte[] computeConcatenatedBinaryString(byte[][] binaryStrings, int length){
        var concatenatedBinaryString = new byte[length];
        var position = 0;

        for (byte[] binaryString : binaryStrings){
            var binaryStringLength = binaryString.length;
            System.arraycopy(binaryString, 0, concatenatedBinaryString, position, binaryStringLength);
            position+=binaryStringLength;
        }

        return concatenatedBinaryString;
    }

    public byte[] hash(byte[] data){
        try {
            var messageDigest = MessageDigest.getInstance(hashAlgorithm, securityProvider);

            messageDigest.update(data);

            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash: Algorithm failure");
        } catch (NoSuchProviderException e) {
            throw new RuntimeException("Failed to hash: Provider failure");
        }
    }
}
