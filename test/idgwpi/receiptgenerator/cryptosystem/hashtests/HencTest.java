package idgwpi.receiptgenerator.cryptosystem.hashtests;

import idgwpi.globals.Constants;
import idgwpi.TestHelpers;
import idgwpi.receiptgenerator.cryptosystem.Hash;
import idgwpi.receiptgenerator.cryptosystem.IHashLogic;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HencTest {
    private Hash hash;
    private BigInteger order;
    private ECPoint ec1;
    private byte[] midResult;
    private IHashLogic hashLogic;

    @BeforeEach
    public void setup(){
        hash = new Hash();
        hashLogic = mock(IHashLogic.class);
        hash.setup(hashLogic);

        var group = Constants.getGroup();
        order = group.getN();
        ec1 = group.getG().multiply(BigInteger.valueOf(new Random().nextInt(2000000) + 200000));
        var ecArray = new ECPoint[]{ec1};

        var prefix = "HENC".getBytes();
        var expectedByteArray = new byte[][]{prefix, ec1.getEncoded(true)};
        midResult = new byte[new Random().nextInt(20) + 6];
        new SecureRandom().nextBytes(midResult);

        var length = ec1.getEncoded(true).length + prefix.length;

        when(hashLogic.computeBinaryStrings(prefix, ecArray)).thenReturn(expectedByteArray);
        when(hashLogic.computeConcatenatedBinaryStringLength(argThat(TestHelpers.getMatcher(expectedByteArray)))).thenReturn(length);
        when(hashLogic.computeConcatenatedBinaryString(argThat(TestHelpers.getMatcher(expectedByteArray)), eq(length))).thenReturn(midResult);
    }

    @RepeatedTest(10)
    @DisplayName("henc returns expected BigInteger")
    public void ReturnsExpectedBigInteger(){
        byte[] result = new byte[new Random().nextInt(20) + 6];
        new SecureRandom().nextBytes(result);
        when(hashLogic.hash(midResult)).thenReturn(result);
        var expected = new BigInteger(1, result).mod(order);

        var actual = hash.henc(ec1, order);

        assertEquals(expected, actual);
    }

    @RepeatedTest(10)
    @DisplayName("henc returns BigInteger mod order")
    public void ReturnsExpectedBigIntegerModOrder(){
        byte[] result = order.add(BigInteger.valueOf(5000)).toByteArray();
        when(hashLogic.hash(midResult)).thenReturn(result);
        var expected = new BigInteger(1, result).mod(order);

        var actual = hash.henc(ec1, order);

        assertEquals(expected, actual);
    }
}
