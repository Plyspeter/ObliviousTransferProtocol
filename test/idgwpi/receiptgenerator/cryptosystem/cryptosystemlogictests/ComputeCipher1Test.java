package idgwpi.receiptgenerator.cryptosystem.cryptosystemlogictests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.cryptosystem.CryptoSystemLogic;
import idgwpi.receiptgenerator.cryptosystem.IHash;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ComputeCipher1Test {
    private static CryptoSystemLogic cryptoSystemLogic = new CryptoSystemLogic();
    private static X9ECParameters group;

    @BeforeAll
    public static void initial(){
        cryptoSystemLogic = new CryptoSystemLogic();
        group = Constants.getGroup();

        cryptoSystemLogic.setup(mock(IHash.class));
    }

    @ParameterizedTest
    @MethodSource("BigIntegers")
    @DisplayName("ComputeCipher1 returns expected Point given specific BigInteger")
    public void ReturnsExpectedPoint(BigInteger input){
        var expected = group.getG().multiply(input);

        var actual = cryptoSystemLogic.computeCipher1(input);

        assertTrue(expected.equals(actual));
    }

    @ParameterizedTest
    @MethodSource("BigIntegers")
    @DisplayName("ComputeCipher1 returns valid ECPoint given specific BigInteger")
    public void ReturnsValidPoint(BigInteger input){
        var actual = cryptoSystemLogic.computeCipher1(input);

        assertTrue(actual.isValid());
    }

    @ParameterizedTest
    @MethodSource({"BigIntegers"})
    @DisplayName("ComputeCipher1 returns different ECPoints with different input")
    public void ReturnsDifferentPoint(BigInteger input){
        var ecPoint1 = cryptoSystemLogic.computeCipher1(input);
        var ecPoint2 = cryptoSystemLogic.computeCipher1(input.add(randomBigInt()));

        assertFalse(ecPoint1.equals(ecPoint2));
    }

    private static Stream<Arguments> BigIntegers() {
        return Stream.of(
                Arguments.of(BigInteger.ONE),
                Arguments.of(BigInteger.TWO),
                Arguments.of(BigInteger.TEN),
                Arguments.of(BigInteger.ZERO),
                Arguments.of(group.getN()),
                Arguments.of(group.getN().add(BigInteger.ONE))
        );
    }

    private BigInteger randomBigInt(){
        return BigInteger.valueOf(new Random().nextInt(10000000) + 1);
    }
}
