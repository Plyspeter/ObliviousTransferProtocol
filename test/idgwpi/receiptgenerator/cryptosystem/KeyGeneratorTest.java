package idgwpi.receiptgenerator.cryptosystem;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class KeyGeneratorTest {
    private static final X9ECParameters group = SECNamedCurves.getByName("secp256r1");

    @Test
    @DisplayName("Normal generation")
    public void normal_generation_no_fault_test() {
        var gen = new KeyGenerator();
        gen.generate(group);

        assertNotNull(gen.getSk());
        assertNotNull(gen.getPk());
    }

    @Test
    @DisplayName("No generation")
    public void no_generation_test() {
        var gen = new KeyGenerator();

        assertThrows(RuntimeException.class, gen::getSk);
        assertThrows(RuntimeException.class, gen::getPk);
    }

    @Test
    @DisplayName("Always different keys")
    public void different_keys_test() {
        var gen = new KeyGenerator();
        gen.generate(group);

        var sk = gen.getSk();
        var pk = gen.getPk();

        gen.generate(group);

        assertNotEquals(sk, gen.getSk());
        assertFalse(pk.equals(gen.getPk()));
    }
}
