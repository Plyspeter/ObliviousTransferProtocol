package idgwpi.receiptgenerator.cryptosystem;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public interface IKeyGenerator {

    void generate(X9ECParameters group);
    BigInteger getSk();
    ECPoint getPk();
}
