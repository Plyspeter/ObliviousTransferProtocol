package idgwpi.receiptgenerator.cryptosystem.cryptosystemlogictests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.cryptosystem.CryptoSystemLogic;
import idgwpi.receiptgenerator.cryptosystem.IHash;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigInteger;
import java.util.Random;

public class ComputeRandomnessTest {


    @RepeatedTest(10)
    @DisplayName("ComputeRandomness returns expected BigInteger")
    public void returnsExpectedBigInteger(){
        var hash = mock(IHash.class);

        var group = Constants.getGroup();
        var expected = BigInteger.valueOf(new Random().nextInt(2000000) + 200000);
        var point = group.getG().multiply(expected);

        when(hash.henc(point, group.getN())).thenReturn(expected);

        var cryptoSystemLogic = new CryptoSystemLogic();
        cryptoSystemLogic.setup(hash);

        var actual = cryptoSystemLogic.computeRandomness(point);

        assertEquals(expected, actual);
    }
}
