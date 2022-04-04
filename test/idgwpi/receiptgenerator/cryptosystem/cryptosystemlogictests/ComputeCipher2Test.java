package idgwpi.receiptgenerator.cryptosystem.cryptosystemlogictests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.cryptosystem.CryptoSystemLogic;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ComputeCipher2Test {
    private final static X9ECParameters group = Constants.getGroup();

    @ParameterizedTest
    @MethodSource("BigIntegers")
    @DisplayName("ComputeCipher1 returns expected Point given specific BigInteger")
    public void ReturnsExpectedPoint(BigInteger input){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var pk = group.getG().multiply(BigInteger.valueOf(666));
        var ms = group.getG().multiply(BigInteger.valueOf(197567));
        var expected = ms.add(pk.multiply(input));

        var actual = cryptoSystemLogic.computeCipher2(pk, ms, input);

        assertTrue(expected.equals(actual));
    }

    @ParameterizedTest
    @MethodSource("BigIntegers")
    @DisplayName("ComputeCipher1 returns valid ECPoint given specific BigInteger")
    public void ReturnsValidPoint(BigInteger input){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var pk = group.getG().multiply(BigInteger.valueOf(666));
        var ms = group.getG().multiply(BigInteger.valueOf(197567));

        var actual = cryptoSystemLogic.computeCipher2(pk, ms, input);

        assertTrue(actual.isValid());
    }

    @ParameterizedTest
    @MethodSource({"BigIntegers"})
    @DisplayName("ComputeCipher1 returns different ECPoints with different random input")
    public void ReturnsDifferentPointPerPK(BigInteger input){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var pk = group.getG().multiply(BigInteger.valueOf(666));
        var ms = group.getG().multiply(BigInteger.valueOf(197567));

        var ecPoint1 = cryptoSystemLogic.computeCipher2(pk, ms, input);
        var ecPoint2 = cryptoSystemLogic.computeCipher2(pk, ms, input.add(randomBigInt()));

        assertFalse(ecPoint1.equals(ecPoint2));
    }

    @ParameterizedTest
    @MethodSource({"BigIntegers"})
    @DisplayName("ComputeCipher1 returns different ECPoints with different publickey input")
    public void ReturnsDifferentPointPerMs(BigInteger input){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var ms = group.getG().multiply(BigInteger.valueOf(197567));
        var r = BigInteger.valueOf(42);

        var ecPoint1 = cryptoSystemLogic.computeCipher2(group.getG().multiply(input), ms, r);
        var ecPoint2 = cryptoSystemLogic.computeCipher2(group.getG().multiply(input.add(randomBigInt())), ms, r);

        assertFalse(ecPoint1.equals(ecPoint2));
    }

    @ParameterizedTest
    @MethodSource({"BigIntegers"})
    @DisplayName("ComputeCipher1 returns different ECPoints with different message input")
    public void ReturnsDifferentPointPerRandom(BigInteger input){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var pk = group.getG().multiply(BigInteger.valueOf(666));
        var r = BigInteger.valueOf(42);

        var ecPoint1 = cryptoSystemLogic.computeCipher2(pk, group.getG().multiply(input), r);
        var ecPoint2 = cryptoSystemLogic.computeCipher2(pk, group.getG().multiply(input.add(randomBigInt())), r);

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
