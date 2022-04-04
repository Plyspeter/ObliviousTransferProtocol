package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ComputePMarkTest {
    private static RGServerLogicECOT serverLogic;
    private static ECPoint input1;
    private static ECPoint input2;
    private static ICryptoSystem cryptoSystem;

    @BeforeAll
    public static void initial(){
        var g = Constants.getGroup().getG();
        serverLogic = new RGServerLogicECOT();
        input1 = g.multiply(BigInteger.ONE);
        input2 = g.multiply(BigInteger.TEN);
    }

    @BeforeEach
    public void setup(){
        cryptoSystem = mock(ICryptoSystem.class);
    }

    @Test
    @DisplayName("ComputePMark calls hash method hch")
    public void correctMethodCalled(){
        serverLogic.setup(cryptoSystem);

        serverLogic.computePMark(input1, input2);

        verify(cryptoSystem, times(1)).hch(input1, input2);
    }

    @Test
    @DisplayName("ComputePMark returns the expected byte[]")
    public void returnsExpected(){
        var expected = new byte[]{42,42,42};
        serverLogic.setup(cryptoSystem);

        when(cryptoSystem.hch(input1, input2)).thenReturn(expected);

        var actual = serverLogic.computePMark(input1, input2);

        assertArrayEquals(expected, actual);
    }
}
