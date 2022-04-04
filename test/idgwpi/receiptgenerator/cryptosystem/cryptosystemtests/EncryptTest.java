package idgwpi.receiptgenerator.cryptosystem.cryptosystemtests;

import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.cryptosystem.*;
import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.Random;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EncryptTest {
    private static X9ECParameters group;
    private static CryptoSystem cryptoSystem;
    private static ICryptoSystemLogic cryptoSystemLogic;
    private static ECPoint message;
    private static BigInteger randomness;

    @BeforeAll
    public static void initial(){
        group = Constants.getGroup();
        cryptoSystem = new CryptoSystem();
    }

    @BeforeEach
    public void setupEach(){
        cryptoSystemLogic = mock(ICryptoSystemLogic.class);
        cryptoSystem.setup(cryptoSystemLogic, Provider.getHash());
        randomness = BigInteger.valueOf(new Random().nextInt(2000000) + 20000);
    }

    @ParameterizedTest
    @MethodSource("BigIntegers")
    @DisplayName("Encrypt returns expected ciphers in ciphertext given different secretKeys")
    public void ReturnsExpectedPointsPerSK(BigInteger secretKey){
        var pk = group.getG().multiply(secretKey);
        message = group.getG().multiply(BigInteger.valueOf(666));

        var expected = setupMock(pk);

        var actual = cryptoSystem.encrypt(pk, message);

        assertTrue(expected.getCipher1().equals(actual.getCipher1()) && expected.getCipher2().equals(actual.getCipher2()));
    }

    @ParameterizedTest
    @MethodSource("BigIntegers")
    @DisplayName("Encrypt returns expected ciphers in ciphertext given different messages")
    public void ReturnsExpectedPointsPerMS(BigInteger messageMod){
        var pk = group.getG().multiply(BigInteger.valueOf(666));
        message = group.getG().multiply(messageMod);

        var expected = setupMock(pk);

        var actual = cryptoSystem.encrypt(pk, message);

        assertTrue(expected.getCipher1().equals(actual.getCipher1()) && expected.getCipher2().equals(actual.getCipher2()));
    }

    private CipherText setupMock(ECPoint pk){
        var c1 = group.getG().multiply(randomness);
        var c2 = message.add(pk.multiply(randomness));
        when(cryptoSystemLogic.computeRandomness(message)).thenReturn(randomness);
        when(cryptoSystemLogic.computeCipher1(randomness)).thenReturn(c1);
        when(cryptoSystemLogic.computeCipher2(pk, message, randomness)).thenReturn(c2);
        return new CipherText(c1,c2);
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
}
