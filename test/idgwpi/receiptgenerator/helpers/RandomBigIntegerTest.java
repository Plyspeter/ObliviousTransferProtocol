package idgwpi.receiptgenerator.helpers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class RandomBigIntegerTest {

    @RepeatedTest(50)
    @DisplayName("Always generates number between 0 and n")
    public void generateTest() {
        var n = BigInteger.valueOf(10);

        var result = RandomBigInteger.generate(n);

        assertTrue(result.intValue() >= 0);
        assertTrue(result.intValue() <= n.intValue());
    }

    @RepeatedTest(5)
    @DisplayName("Generate with n = 0")
    public void generateWithZero() {
        var n = BigInteger.valueOf(0);

        var result = RandomBigInteger.generate(n);

        assertEquals(0, result.intValue());
    }

    @Test
    @DisplayName("Generate with a negative n")
    public void generateWithNegativeN() {
        var n = BigInteger.valueOf(-10);

        assertThrows(RuntimeException.class, () -> RandomBigInteger.generate(n));
    }
}
