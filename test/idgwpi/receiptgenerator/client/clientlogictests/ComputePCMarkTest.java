package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ComputePCMarkTest {
    private static RGClientLogicECOT clientLogic;
    private static ECPoint input1;
    private static ECPoint input2;
    private static ICryptoSystem cryptoSystem;

    @BeforeAll
    public static void initial(){
        var g = Constants.getGroup().getG();
        clientLogic = new RGClientLogicECOT();
        input1 = g.multiply(BigInteger.ONE);
        input2 = g.multiply(BigInteger.TEN);
    }

    @BeforeEach
    public void setup(){
        cryptoSystem = mock(ICryptoSystem.class);
    }

    @Test
    @DisplayName("ComputePCMark calls hash method hch")
    public void correctMethodCalled(){
        clientLogic.setup(cryptoSystem);

        clientLogic.computePCMark(input1, input2);

        verify(cryptoSystem, times(1)).hch(input1, input2);
    }

    @Test
    @DisplayName("ComputePCMark returns the expected byte[]")
    public void returnsExpected(){
        var expected = new byte[]{42,42,42};
        clientLogic.setup(cryptoSystem);

        when(cryptoSystem.hch(input1, input2)).thenReturn(expected);

        var actual = clientLogic.computePCMark(input1, input2);

        assertArrayEquals(expected, actual);
    }
}
