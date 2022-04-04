package idgwpi.receiptgenerator.cryptosystem.cryptosystemlogictests;

import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.cryptosystem.CryptoSystemLogic;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ExtractMessageTest {
    private final static X9ECParameters group = SECNamedCurves.getByName("secp256r1");

    @ParameterizedTest
    @MethodSource("BigIntegers")
    @DisplayName("ExtractMessage returns expected Point given specific input")
    public void ReturnsExpectedPoint(BigInteger secretKey){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var p1 = group.getG().multiply(BigInteger.valueOf(666));
        var p2 = group.getG().multiply(BigInteger.valueOf(197567));
        var ct = new CipherText(p1, p2);


        var expected = p2.subtract(p1.multiply(secretKey));

        var actual = cryptoSystemLogic.extractMessage(secretKey, ct);

        assertTrue(expected.equals(actual));
    }

    @ParameterizedTest
    @MethodSource("BigIntegers")
    @DisplayName("ExtractMessage returns valid ECPoint given specific input")
    public void ReturnsValidPoints(BigInteger secretKey){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var p1 = group.getG().multiply(BigInteger.valueOf(666));
        var p2 = group.getG().multiply(BigInteger.valueOf(197567));
        var ct = new CipherText(p1, p2);

        var actual = cryptoSystemLogic.extractMessage(secretKey, ct);

        assertTrue(actual.isValid());
    }

    @ParameterizedTest
    @MethodSource({"BigIntegers"})
    @DisplayName("ExtractMessage returns different ECPoints with different secret key input")
    public void ReturnsDifferentPointsPerSK(BigInteger secretKey){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var p1 = group.getG().multiply(BigInteger.valueOf(666));
        var p2 = group.getG().multiply(BigInteger.valueOf(197567));
        var ct = new CipherText(p1, p2);

        var ecPoint1 = cryptoSystemLogic.extractMessage(secretKey, ct);
        var ecPoint2 = cryptoSystemLogic.extractMessage(secretKey.add(randomBigInt()), ct);

        assertFalse(ecPoint1.equals(ecPoint2));
    }

    @RepeatedTest(10)
    @DisplayName("ExtractMessage returns different ECPoints with different cipher1 input")
    public void ReturnsDifferentPointsPerC1(){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var secretKey = BigInteger.valueOf(42);
        var p1 = group.getG().multiply(BigInteger.valueOf(666));
        var p2 = group.getG().multiply(BigInteger.valueOf(197567));
        var p1a = group.getG().multiply(BigInteger.valueOf(666).add(randomBigInt()));
        var p2a = group.getG().multiply(BigInteger.valueOf(197567));
        var ct = new CipherText(p1, p2);
        var cta = new CipherText(p1a, p2a);

        var ecPoint1 = cryptoSystemLogic.extractMessage(secretKey, ct);
        var ecPoint2 = cryptoSystemLogic.extractMessage(secretKey, cta);

        assertFalse(ecPoint1.equals(ecPoint2));
    }

    @RepeatedTest(10)
    @DisplayName("ExtractMessage returns different ECPoints with different cipher2 input")
    public void ReturnsDifferentPointsPerC2(){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var secretKey = BigInteger.valueOf(42);
        var p1 = group.getG().multiply(BigInteger.valueOf(666));
        var p2 = group.getG().multiply(BigInteger.valueOf(197567));
        var p1a = group.getG().multiply(BigInteger.valueOf(666));
        var p2a = group.getG().multiply(BigInteger.valueOf(197567).add(randomBigInt()));
        var ct = new CipherText(p1, p2);
        var cta = new CipherText(p1a, p2a);

        var ecPoint1 = cryptoSystemLogic.extractMessage(secretKey, ct);
        var ecPoint2 = cryptoSystemLogic.extractMessage(secretKey, cta);

        assertFalse(ecPoint1.equals(ecPoint2));
    }

    @RepeatedTest(10)
    @DisplayName("ExtractMessage returns different ECPoints with different ciphertext input")
    public void ReturnsDifferentPointsPerCT(){
        var cryptoSystemLogic = new CryptoSystemLogic();
        var secretKey = BigInteger.valueOf(42);
        var p1 = group.getG().multiply(BigInteger.valueOf(666));
        var p2 = group.getG().multiply(BigInteger.valueOf(197567));
        var p1a = group.getG().multiply(BigInteger.valueOf(666).add(randomBigInt()));
        var p2a = group.getG().multiply(BigInteger.valueOf(197567).add(randomBigInt()));
        var ct = new CipherText(p1, p2);
        var cta = new CipherText(p1a, p2a);

        var ecPoint1 = cryptoSystemLogic.extractMessage(secretKey, ct);
        var ecPoint2 = cryptoSystemLogic.extractMessage(secretKey, cta);

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
