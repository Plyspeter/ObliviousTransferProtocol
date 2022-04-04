package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ComputeQTest {
    private static RGServerLogicECOT serverLogic;
    private static byte[] input;
    private static ICryptoSystem cryptoSystem;

    @BeforeAll
    public static void initial(){
        serverLogic = new RGServerLogicECOT();
        cryptoSystem = mock(ICryptoSystem.class);
        input = new byte[]{42,42,42};
    }

    @BeforeEach
    public void setup(){
        cryptoSystem = mock(ICryptoSystem.class);
    }

    @Test
    @DisplayName("ComputeQ calls hash method hkey")
    public void correctMethodCalled(){
        serverLogic.setup(cryptoSystem);

        serverLogic.computeQ(input);

        verify(cryptoSystem, times(1)).hkey(input);
    }

    @Test
    @DisplayName("ComputeQ returns the expected ECPoint")
    public void ReturnsExpected(){
        var expected = Constants.getGroup().getG().multiply(BigInteger.valueOf(42));
        serverLogic.setup(cryptoSystem);

        when(cryptoSystem.hkey(input)).thenReturn(expected);

        var actual = serverLogic.computeQ(input);

        assertTrue(expected.equals(actual));
    }
}
