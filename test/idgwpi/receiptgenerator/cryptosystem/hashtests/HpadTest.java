package idgwpi.receiptgenerator.cryptosystem.hashtests;

import idgwpi.globals.Constants;
import idgwpi.TestHelpers;
import idgwpi.receiptgenerator.cryptosystem.Hash;
import idgwpi.receiptgenerator.cryptosystem.IHashLogic;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HpadTest {

    @RepeatedTest(10)
    @DisplayName("HchPoints returns expected byte array")
    public void ReturnsExpectedByteArray(){
        var hash = new Hash();
        var hashLogic = mock(IHashLogic.class);
        hash.setup(hashLogic);

        var group = Constants.getGroup();
        var ec1 = group.getG().multiply(BigInteger.valueOf(new Random().nextInt(2000000) + 200000));
        var ec2 = group.getG().multiply(BigInteger.valueOf(new Random().nextInt(2000000) + 200000));
        var ecArray = new ECPoint[]{ec1, ec2};

        var prefix = "HPAD".getBytes();
        var expectedByteArray = new byte[][]{prefix, ec1.getEncoded(true), ec2.getEncoded(true)};
        var midResult = new byte[new Random().nextInt(20) + 6];
        var expected = new byte[new Random().nextInt(20) + 6];
        new SecureRandom().nextBytes(midResult);
        new SecureRandom().nextBytes(expected);

        var length = ec1.getEncoded(true).length + ec2.getEncoded(true).length + prefix.length;

        when(hashLogic.computeBinaryStrings(prefix, ecArray)).thenReturn(expectedByteArray);
        when(hashLogic.computeConcatenatedBinaryStringLength(argThat(TestHelpers.getMatcher(expectedByteArray)))).thenReturn(length);
        when(hashLogic.computeConcatenatedBinaryString(argThat(TestHelpers.getMatcher(expectedByteArray)), eq(length))).thenReturn(midResult);
        when(hashLogic.hash(midResult)).thenReturn(expected);

        var actual = hash.hpad(ec1, ec2);

        assertEquals(expected, actual);
    }
}
