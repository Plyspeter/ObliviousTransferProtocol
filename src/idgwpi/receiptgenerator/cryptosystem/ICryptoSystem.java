package idgwpi.receiptgenerator.cryptosystem;

import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public interface ICryptoSystem {

    void setup(ICryptoSystemLogic cryptoSystemLogic, IHash hash);
    CipherText encrypt(ECPoint publicKey, ECPoint message);
    ECPoint decrypt(ECPoint publicKey, BigInteger secretKey, CipherText cipherText);

    ECPoint hkey(byte[] s);

    byte[] hch(ECPoint pkc, ECPoint pc);

    byte[] hch(byte[] pc_mark);

    byte[] hpad(ECPoint pkc, ECPoint pc);

    IKeyGenerator getKeyGenerator();
}
