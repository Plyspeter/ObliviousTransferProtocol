package idgwpi.receiptgenerator.cryptosystem.hashtests;

import idgwpi.globals.Constants;
import idgwpi.TestHelpers;
import idgwpi.receiptgenerator.cryptosystem.Hash;
import idgwpi.receiptgenerator.cryptosystem.IHashLogic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HkeyTest {

    @RepeatedTest(10)
    @DisplayName("Hkey returns expected byte array")
    public void ReturnsExpectedByteArray(){
        var hash = new Hash();
        var hashLogic = mock(IHashLogic.class);
        hash.setup(hashLogic);

        var prefix = "HKEY".getBytes();
        var input = new byte[new Random().nextInt(20) + 6];
        var expectedByteArray = new byte[][]{prefix, input};
        var midResult = new byte[new Random().nextInt(20) + 6];
        var result = new byte[new Random().nextInt(20) + 6];
        new SecureRandom().nextBytes(input);
        new SecureRandom().nextBytes(midResult);
        new SecureRandom().nextBytes(result);
        var expected = Constants.getGroup().getG().multiply(new BigInteger(1, result));

        var length = input.length + prefix.length;

        when(hashLogic.computeConcatenatedBinaryStringLength(argThat(TestHelpers.getMatcher(expectedByteArray)))).thenReturn(length);
        when(hashLogic.computeConcatenatedBinaryString(argThat(TestHelpers.getMatcher(expectedByteArray)), eq(length))).thenReturn(midResult);
        when(hashLogic.hash(midResult)).thenReturn(result);

        var actual = hash.hkey(input);

        assertTrue(expected.equals(actual));
    }
}
