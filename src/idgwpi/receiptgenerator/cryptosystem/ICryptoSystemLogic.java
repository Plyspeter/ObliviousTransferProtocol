package idgwpi.receiptgenerator.cryptosystem;

import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public interface ICryptoSystemLogic {

    void setup(IHash hash);
    BigInteger computeRandomness(ECPoint message);
    ECPoint computeCipher1(BigInteger randomness);
    ECPoint computeCipher2(ECPoint publicKey, ECPoint message, BigInteger randomness);
    ECPoint extractMessage(BigInteger secretKey, CipherText cipherText);
}
