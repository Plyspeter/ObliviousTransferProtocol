package idgwpi.receiptgenerator.client.clienttests;

import idgwpi.receiptgenerator.client.RGClientECOTWithSF;
import idgwpi.receiptgenerator.client.IRGClientLogicECOT;
import idgwpi.receiptgenerator.client.IRGClientCommunicatorECOTWithSF;
import idgwpi.receiptgenerator.cryptosystem.IKeyGenerator;
import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.*;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ECOTStep1Test {
    private static RGClientECOTWithSF client;
    private static BigInteger sk;
    private static ECPoint pkc;
    private static int choice;
    private static byte[] s;
    private static ECPoint q;
    private static ECPoint pk1MinusC;
    private IKeyGenerator keyGenerator;
    private IRGClientCommunicatorECOTWithSF clientTransmitter;
    private IRGClientLogicECOT clientLogic;

    @BeforeAll
    public static void initial(){
        client = new RGClientECOTWithSF();
        var group = Constants.getGroup().getG();
        sk = BigInteger.valueOf(197302375488L);
        pkc = group.multiply(sk);
        choice = 0;
        s = new byte[]{15,-6,8,-57};
        q = group.multiply(BigInteger.valueOf(59523));
        pk1MinusC = group.multiply(BigInteger.valueOf(1589));
    }

    @BeforeEach
    public void setup(){
        clientTransmitter = mock(IRGClientCommunicatorECOTWithSF.class);
        clientLogic = mock(IRGClientLogicECOT.class);
        keyGenerator = mock(IKeyGenerator.class);

        when(keyGenerator.getPk()).thenReturn(pkc);
        when(keyGenerator.getSk()).thenReturn(sk);

        when(clientLogic.getKeyGenerator()).thenReturn(keyGenerator);
        when(clientLogic.sampleRandomBinaryString()).thenReturn(s);
        when(clientLogic.computeQ(s)).thenReturn(q);
        when(clientLogic.computePK1MinusC(q, pkc)).thenReturn(pk1MinusC);

        client.setup(clientTransmitter, clientLogic, choice);
        client.step1();
    }

    @Test
    @DisplayName("Step1 calls clientLogic.getKeyGenerator")
    public void callsGetKeyGenerator(){
        verify(clientLogic, times(1)).getKeyGenerator();
    }

    @Test
    @DisplayName("Step1 calls keyGenerator.getPk")
    public void callsGetPk(){
        verify(keyGenerator, times(1)).getPk();
    }

    @Test
    @DisplayName("Step1 calls keyGenerator.getSk")
    public void callsGetSk(){
        verify(keyGenerator, times(1)).getSk();
    }

    @Test
    @DisplayName("Step1 calls clientLogic.sampleRandomBinaryString")
    public void callsSampleRandomBinaryString(){
        verify(clientLogic, times(1)).sampleRandomBinaryString();
    }

    @Test
    @DisplayName("Step1 calls clientLogic.computeQ, with correct input")
    public void callsComputeQ(){
        verify(clientLogic, times(1)).computeQ(s);
    }

    @Test
    @DisplayName("Step1 calls clientLogic.computePK1MinusC, with correct input")
    public void callsComputePK1MinusC(){
        verify(clientLogic, times(1)).computePK1MinusC(q, pkc);
    }

    @Test
    @DisplayName("Step1 calls clientTransmitter.transmitStep1, with correct input")
    public void usesCorrectVariables(){
        verify(clientTransmitter, times(1)).transmitStep1(choice, s, pkc, pk1MinusC);
    }

    @Test
    @DisplayName("Step1 stores secretKey in field variable")
    public void secretKeyStored(){
        try {
            var skField = client.getClass().getDeclaredField("sk");
            skField.setAccessible(true);

            assertEquals(sk, skField.get(client));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Step1 stores publicKey in field variable")
    public void publicKeyStored(){
        try {
            var pkField = client.getClass().getDeclaredField("pkc");
            pkField.setAccessible(true);

            assertTrue(pkc.equals((ECPoint) pkField.get(client)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }
}
