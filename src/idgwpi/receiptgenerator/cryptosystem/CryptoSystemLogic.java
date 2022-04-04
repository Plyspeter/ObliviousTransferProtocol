package idgwpi.receiptgenerator.cryptosystem;

import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class CryptoSystemLogic implements ICryptoSystemLogic{
    private X9ECParameters group;
    private IHash hash;

    public void setup(IHash hash){
        group = Constants.getGroup();
        this.hash = hash;
        hash.setup(Provider.getHashLogic());
    }

    public BigInteger computeRandomness(ECPoint message) {
        return hash.henc(message, group.getN());
    }

    public ECPoint computeCipher1(BigInteger randomness){
        var baseEntry = group.getG();

        return baseEntry.multiply(randomness);
    }

    public ECPoint computeCipher2(ECPoint publicKey, ECPoint message, BigInteger randomness){
        var publicKeyWithRandomness = publicKey.multiply(randomness);

        return message.add(publicKeyWithRandomness);
    }

    public ECPoint extractMessage(BigInteger secretKey, CipherText cipherText) {
        var c1MultipliedSecretKey = cipherText.getCipher1().multiply(secretKey);

        return cipherText.getCipher2().subtract(c1MultipliedSecretKey);
    }
}
