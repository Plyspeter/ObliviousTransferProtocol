package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ComputePCMark2Test {
    private static RGClientLogicECOT clientLogic;
    private static byte[] input;
    private static ICryptoSystem cryptoSystem;

    @BeforeAll
    public static void initial(){
        clientLogic = new RGClientLogicECOT();
        cryptoSystem = mock(ICryptoSystem.class);
        input = new byte[]{42,42,42};
    }

    @BeforeEach
    public void setup(){
        cryptoSystem = mock(ICryptoSystem.class);
    }

    @Test
    @DisplayName("ComputePCMark2 calls hash method hch")
    public void correctMethodCalled(){
        clientLogic.setup(cryptoSystem);

        clientLogic.computePCMark2(input);

        verify(cryptoSystem, times(1)).hch(input);
    }

    @Test
    @DisplayName("ComputePCMark2 returns the expected byte[]")
    public void ReturnsExpected(){
        var expected = new byte[]{42,42,42};
        clientLogic.setup(cryptoSystem);

        when(cryptoSystem.hch(input)).thenReturn(expected);

        var actual = clientLogic.computePCMark2(input);

        assertArrayEquals(expected, actual);
    }
}
