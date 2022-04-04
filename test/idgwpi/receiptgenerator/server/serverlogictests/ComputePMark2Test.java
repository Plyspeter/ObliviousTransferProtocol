package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ComputePMark2Test {
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
    @DisplayName("ComputePMark2 calls hash method hch")
    public void correctMethodCalled(){
        serverLogic.setup(cryptoSystem);

        serverLogic.computePMark2(input);

        verify(cryptoSystem, times(1)).hch(input);
    }

    @Test
    @DisplayName("ComputePMark2 returns the expected byte[]")
    public void ReturnsExpected(){
        var expected = new byte[]{42,42,42};
        serverLogic.setup(cryptoSystem);

        when(cryptoSystem.hch(input)).thenReturn(expected);

        var actual = serverLogic.computePMark2(input);

        assertArrayEquals(expected, actual);
    }
}
