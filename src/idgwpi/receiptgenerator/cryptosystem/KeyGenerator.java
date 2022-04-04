package idgwpi.receiptgenerator.cryptosystem;

import idgwpi.receiptgenerator.helpers.RandomBigInteger;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public class KeyGenerator implements IKeyGenerator {

    private BigInteger sk;
    private ECPoint pk;

    /**
     * Generates a public and private key, based on the cryptosystem PKE
     * in chapter 1.2 - (pkc, skc) ←$ KG(1^k)
     */
    public void generate(X9ECParameters group) {
        // sk <- Zp (Integer between 0 and p)
        sk = RandomBigInteger.generate(group.getN());

        // pk = g^sk (The sk'th point in the generator)
        pk = group.getG().multiply(sk);
    }

    public BigInteger getSk() {
        if (sk == null)
            throw new RuntimeException("Tried to get secret key without generating");

        return sk;
    }

    public ECPoint getPk() {
        if (pk == null)
            throw new RuntimeException("Tried to get public key without generating");

        return pk;
    }
}
