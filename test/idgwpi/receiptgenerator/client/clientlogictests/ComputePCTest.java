package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import idgwpi.receiptgenerator.cryptosystem.IHash;
import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ComputePCTest {
    private static RGClientLogicECOT clientLogic;
    private static ICryptoSystem cryptoSystem;
    private static IHash hash;
    private static ECPoint pkc;
    private static BigInteger sk;
    private static CipherText ctc;
    private static ECPoint g;

    @BeforeAll
    public static void initial(){
        g = Constants.getGroup().getG();
        clientLogic = new RGClientLogicECOT();
        hash = mock(IHash.class);
        pkc = g.multiply(BigInteger.valueOf(666));
        sk = BigInteger.valueOf(42);
        ctc = new CipherText(g.multiply(BigInteger.valueOf(684752)), g.multiply(BigInteger.valueOf(5868)));

    }

    @BeforeEach
    public void setup(){
        cryptoSystem = mock(ICryptoSystem.class);
        clientLogic.setup(cryptoSystem);
    }

    @Test
    @DisplayName("ComputePC calls cryptoSystem method decrypt")
    public void correctMethodCalled(){
        clientLogic.computePC(pkc, sk, ctc);

        verify(cryptoSystem, times(1)).decrypt(pkc, sk, ctc);
    }

    @Test
    @DisplayName("ComputePC returns expected ECPoint")
    public void returnsExpected(){
        var expected = g.multiply(BigInteger.valueOf(2357923461L));

        when(cryptoSystem.decrypt(pkc, sk, ctc)).thenReturn(expected);

        var actual = clientLogic.computePC(pkc, sk, ctc);

        assertTrue(expected.equals(actual));
    }
}
