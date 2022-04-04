package idgwpi.receiptgenerator.cryptosystem;

import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class CryptoSystem implements ICryptoSystem{
    private ICryptoSystemLogic cryptoSystemLogic;
    private IHash hash;

    public void setup(ICryptoSystemLogic cryptoSystemLogic, IHash hash){
        this.cryptoSystemLogic = cryptoSystemLogic;
        this.hash = hash;
        hash.setup(Provider.getHashLogic());
        cryptoSystemLogic.setup(hash);
    }

    public CipherText encrypt(ECPoint publicKey, ECPoint message){
        var randomness = cryptoSystemLogic.computeRandomness(message);
        var c1 = cryptoSystemLogic.computeCipher1(randomness);
        var c2 = cryptoSystemLogic.computeCipher2(publicKey, message, randomness);

        return new CipherText(c1, c2);
    }

    public ECPoint decrypt(ECPoint publicKey, BigInteger secretKey, CipherText cipherText){
        var message = cryptoSystemLogic.extractMessage(secretKey, cipherText);
        var randomness = cryptoSystemLogic.computeRandomness(message);
        var c1 = cryptoSystemLogic.computeCipher1(randomness);
        var c2 = cryptoSystemLogic.computeCipher2(publicKey, message, randomness);

        if (c1.equals(cipherText.getCipher1()) && c2.equals(cipherText.getCipher2()))
            return message;
        throw new RuntimeException("Decrypt: Ciphers not equal");
    }

    @Override
    public ECPoint hkey(byte[] s) {
        return hash.hkey(s);
    }

    @Override
    public byte[] hch(ECPoint pkc, ECPoint pc) {
        return hash.hch(pkc, pc);
    }

    @Override
    public byte[] hch(byte[] pc_mark) {
        return hash.hch(pc_mark);
    }

    @Override
    public byte[] hpad(ECPoint pkc, ECPoint pc) {
        return hash.hpad(pkc, pc);
    }

    @Override
    public IKeyGenerator getKeyGenerator() {
        var keyGenerator = new KeyGenerator();
        keyGenerator.generate(Constants.getGroup());
        return keyGenerator;
    }
}
