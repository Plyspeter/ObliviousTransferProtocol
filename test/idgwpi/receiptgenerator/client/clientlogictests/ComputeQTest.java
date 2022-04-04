package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import idgwpi.globals.Constants;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ComputeQTest {
    private static RGClientLogicECOT clientLogic;
    private static byte[] input;
    private static ICryptoSystem cryptoSystem;

    @BeforeAll
    public static void initial(){
        clientLogic = new RGClientLogicECOT();
        input = new byte[]{42,42,42};
    }

    @BeforeEach
    public void setup(){
        cryptoSystem = mock(ICryptoSystem.class);
    }

    @Test
    @DisplayName("ComputeQ calls hash method hkey")
    public void correctMethodCalled(){
        clientLogic.setup(cryptoSystem);

        clientLogic.computeQ(input);

        verify(cryptoSystem, times(1)).hkey(input);
    }

    @Test
    @DisplayName("ComputeQ returns the expected ECPoint")
    public void ReturnsExpected(){
        var expected = Constants.getGroup().getG().multiply(BigInteger.valueOf(42));
        clientLogic.setup(cryptoSystem);

        when(cryptoSystem.hkey(input)).thenReturn(expected);

        var actual = clientLogic.computeQ(input);

        assertTrue(expected.equals(actual));
    }
}
