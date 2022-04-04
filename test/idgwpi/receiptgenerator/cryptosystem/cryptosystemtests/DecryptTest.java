package idgwpi.receiptgenerator.cryptosystem.cryptosystemtests;

import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.cryptosystem.CryptoSystem;
import idgwpi.receiptgenerator.cryptosystem.ICryptoSystemLogic;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DecryptTest {
    private static X9ECParameters group;
    private static CryptoSystem cryptoSystem;
    private static ICryptoSystemLogic cryptoSystemLogic;
    private static ECPoint messageM666;
    private static ECPoint messageAltered;
    private static BigInteger randomnessM666;
    private static BigInteger randomnessAltered;
    private static ECPoint publicKeyM42;
    private static BigInteger secretKeyM42;

    @BeforeAll
    public static void initial(){
        group = Constants.getGroup();
        randomnessM666 = BigInteger.valueOf(666);
        randomnessAltered = BigInteger.valueOf(667);
        messageM666 = group.getG().multiply(BigInteger.valueOf(666));
        messageAltered = group.getG().multiply(BigInteger.valueOf(667));
        secretKeyM42 = BigInteger.valueOf(42);
        publicKeyM42 = group.getG().multiply(secretKeyM42);
    }

    @BeforeEach
    public void setup(){
        cryptoSystemLogic = mock(ICryptoSystemLogic.class);
        cryptoSystem = new CryptoSystem();
        cryptoSystem.setup(cryptoSystemLogic, Provider.getHash());
    }

    @ParameterizedTest
    @MethodSource({"publicKeysWithSecretKeys"})
    @DisplayName("Decrypt returns expected message with different secret keys")
    public void ReturnsExpectedMessagePerSK(ECPoint publicKey, BigInteger secretKey){
        var cipherText = setupMock(publicKey, messageM666, randomnessM666, secretKey);

        var actual = cryptoSystem.decrypt(publicKey, secretKey, cipherText);

        assertTrue(messageM666.equals(actual));
    }

    @ParameterizedTest
    @MethodSource({"messagesWithRandomness"})
    @DisplayName("Decrypt returns expected message with different messages")
    public void ReturnsExpectedMessagePerMS(ECPoint message, BigInteger randomness){
        var cipherText = setupMock(publicKeyM42, message, randomness, secretKeyM42);

        var actual = cryptoSystem.decrypt(publicKeyM42, secretKeyM42, cipherText);

        assertTrue(message.equals(actual));
    }

    @ParameterizedTest
    @MethodSource({"ciphers"})
    @DisplayName("Decrypt throws RuntimeException when cipher1 is wrong")
    public void ThrowsExpectedExceptionCipher1Wrong(ECPoint cipher){
        var cipherText = setupMock(publicKeyM42, messageM666, randomnessM666, secretKeyM42);
        var alteredCipherText = new CipherText(cipher, cipherText.getCipher2());
        setupAlteredMock(publicKeyM42, alteredCipherText, secretKeyM42);


        var exception = assertThrows(RuntimeException.class, () -> cryptoSystem.decrypt(publicKeyM42, secretKeyM42, alteredCipherText));
        assertEquals("Decrypt: Ciphers not equal", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource({"ciphers"})
    @DisplayName("Decrypt throws RuntimeException when cipher2 is wrong")
    public void ThrowsExpectedExceptionCipher2Wrong(ECPoint cipher){
        var cipherText = setupMock(publicKeyM42, messageM666, randomnessM666, secretKeyM42);
        var alteredCipherText = new CipherText(cipherText.getCipher1(), cipher);
        setupAlteredMock(publicKeyM42, alteredCipherText, secretKeyM42);

        var exception = assertThrows(RuntimeException.class, () -> cryptoSystem.decrypt(publicKeyM42, secretKeyM42, alteredCipherText));
        assertEquals("Decrypt: Ciphers not equal", exception.getMessage());
    }

    @Test
    @DisplayName("Decrypt throws RuntimeException when ciphers are switched")
    public void ThrowsExpectedExceptionCiphersAreSwitched(){
        var cipherText = setupMock(publicKeyM42, messageM666, randomnessM666, secretKeyM42);
        var alteredCipherText = new CipherText(cipherText.getCipher2(), cipherText.getCipher1());
        setupAlteredMock(publicKeyM42, alteredCipherText, secretKeyM42);

        var exception = assertThrows(RuntimeException.class, () -> cryptoSystem.decrypt(publicKeyM42, secretKeyM42, alteredCipherText));
        assertEquals("Decrypt: Ciphers not equal", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource({"publicKeysWithSecretKeys"})
    @DisplayName("Decrypt throws RuntimeException when secret key is wrong")
    public void ThrowsExpectedExceptionSKWrong(ECPoint notUsed, BigInteger secretKey){
        var cipherText = setupMock(publicKeyM42, messageM666, randomnessM666, secretKey);
        setupAlteredMock(publicKeyM42, cipherText, secretKey);

        var exception = assertThrows(RuntimeException.class, () -> cryptoSystem.decrypt(publicKeyM42, secretKey, cipherText));
        assertEquals("Decrypt: Ciphers not equal", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource({"publicKeysWithSecretKeys"})
    @DisplayName("Decrypt throws RuntimeException when public key is wrong")
    public void ThrowsExpectedExceptionPKWrong(ECPoint publicKey, BigInteger notUsed){
        var cipherText = setupMock(publicKey, messageM666, randomnessM666, secretKeyM42);
        setupAlteredMock(publicKey, cipherText, secretKeyM42);

        var exception = assertThrows(RuntimeException.class, () -> cryptoSystem.decrypt(publicKey, secretKeyM42, cipherText));
        assertEquals("Decrypt: Ciphers not equal", exception.getMessage());
    }

    private CipherText setupMock(ECPoint pk, ECPoint message, BigInteger randomness, BigInteger sk){
        var c1 = group.getG().multiply(randomness);
        var c2 = message.add(pk.multiply(randomness));
        var cipherText = new CipherText(c1,c2);
        when(cryptoSystemLogic.extractMessage(sk, cipherText)).thenReturn(message);
        when(cryptoSystemLogic.computeRandomness(message)).thenReturn(randomness);
        when(cryptoSystemLogic.computeCipher1(randomness)).thenReturn(c1);
        when(cryptoSystemLogic.computeCipher2(pk, message, randomness)).thenReturn(c2);
        return cipherText;
    }

    private void setupAlteredMock(ECPoint pk, CipherText cipherText, BigInteger sk){
        var c1 = group.getG().multiply(randomnessAltered);
        var c2 = messageAltered.add(pk.multiply(randomnessAltered));
        when(cryptoSystemLogic.extractMessage(sk, cipherText)).thenReturn(messageAltered);
        when(cryptoSystemLogic.computeRandomness(messageAltered)).thenReturn(randomnessAltered);
        when(cryptoSystemLogic.computeCipher1(randomnessAltered)).thenReturn(c1);
        when(cryptoSystemLogic.computeCipher2(pk, messageAltered, randomnessAltered)).thenReturn(c2);
    }

    private static Stream<Arguments> publicKeysWithSecretKeys() {
        return Stream.of(
                Arguments.of(group.getG().multiply(BigInteger.ONE), BigInteger.ONE),
                Arguments.of(group.getG().multiply(BigInteger.TWO), BigInteger.TWO),
                Arguments.of(group.getG().multiply(BigInteger.TEN), BigInteger.TEN),
                Arguments.of(group.getG().multiply(BigInteger.ZERO), BigInteger.ZERO),
                Arguments.of(group.getG().multiply(group.getN()), group.getN()),
                Arguments.of(group.getG().multiply(group.getN().add(BigInteger.ONE)), group.getN().add(BigInteger.ONE))
        );
    }

    private static Stream<Arguments> messagesWithRandomness() {
        return Stream.of(
                Arguments.of(group.getG().multiply(BigInteger.ONE), new BigInteger(new byte[]{0, -58, 127, 54, -101, 77, 43, -109, 86, 73, -29, 65, 60, -78, -46, 13, 119, 101, -106, -123, -102, -99, -82, -45, -124, -3, -74, -12, 111, 19, 63, 88, 71})),
                Arguments.of(group.getG().multiply(BigInteger.TWO), new BigInteger(new byte[]{118, 83, -19, -4, 85, -52, 81, 80, 70, -21, -18, 38, -59, -55, -86, -67, -89, 127, 7, 123, 56, 73, -94, -10, -98, -29, 91, -61, 49, 43, 127, -108})),
                Arguments.of(group.getG().multiply(BigInteger.TEN), new BigInteger(new byte[]{88, 112, -27, 2, -59, 63, -70, 98, -107, 86, -100, 26, -2, -70, -120, 111, -86, -27, 72, -60, 125, -27, 35, -63, 24, -100, -11, -38, 39, -116, 92, -106})),
                Arguments.of(group.getG().multiply(BigInteger.ZERO), new BigInteger(new byte[]{0, -42, -6, 59, -73, 38, 115, 89, -128, 35, 76, -69, -2, -29, -15, -53, 127, 26, 98, 39, 28, 50, 117, -81, -8, 61, 114, -25, 86, 60, 118, 92, 36})),
                Arguments.of(group.getG().multiply(group.getN()), new BigInteger(new byte[]{0, -42, -6, 59, -73, 38, 115, 89, -128, 35, 76, -69, -2, -29, -15, -53, 127, 26, 98, 39, 28, 50, 117, -81, -8, 61, 114, -25, 86, 60, 118, 92, 36})),
                Arguments.of(group.getG().multiply(group.getN().add(BigInteger.ONE)), new BigInteger(new byte[]{0, -58, 127, 54, -101, 77, 43, -109, 86, 73, -29, 65, 60, -78, -46, 13, 119, 101, -106, -123, -102, -99, -82, -45, -124, -3, -74, -12, 111, 19, 63, 88, 71}))
        );
    }

    private static Stream<Arguments> ciphers() {
        return Stream.of(
                Arguments.of(group.getG().multiply(BigInteger.ONE)),
                Arguments.of(group.getG().multiply(BigInteger.TWO)),
                Arguments.of(group.getG().multiply(BigInteger.TEN)),
                Arguments.of(group.getG().multiply(BigInteger.ZERO)),
                Arguments.of(group.getG().multiply(group.getN())),
                Arguments.of(group.getG().multiply(group.getN().add(BigInteger.ONE)))
        );
    }
}
