package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ComputeCipherTextTest {
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
        serverLogic.setup(cryptoSystem);
    }

    @Test
    @DisplayName("ComputeCipherText calls cryptoSystem method encrypt")
    public void correctMethodCalled(){

        serverLogic.computeCipherText(input1, input2);

        verify(cryptoSystem, times(1)).encrypt(input1, input2);
    }

    @Test
    @DisplayName("ComputeCipherText returns the expected byte[]")
    public void returnsExpected(){
        var expected = new CipherText(input1, input2);

        when(cryptoSystem.encrypt(input1, input2)).thenReturn(expected);

        var actual = serverLogic.computeCipherText(input1, input2);

        assertTrue(expected.getCipher1().equals(actual.getCipher1()) &&
                expected.getCipher2().equals(actual.getCipher2()));
    }
}
