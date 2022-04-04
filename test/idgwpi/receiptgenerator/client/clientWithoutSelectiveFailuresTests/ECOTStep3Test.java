package idgwpi.receiptgenerator.client.clientWithoutSelectiveFailuresTests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.client.RGClientECOTWithoutSF;
import idgwpi.receiptgenerator.client.IRGClientLogicECOT;
import idgwpi.receiptgenerator.client.IRGClientCommunicatorECOTWithoutSF;
import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ECOTStep3Test {
    private static RGClientECOTWithoutSF client;
    private IRGClientCommunicatorECOTWithoutSF clientTransmitter;
    private IRGClientLogicECOT clientLogic;
    private static int choice;

    private static int c_mark;
    private static CipherText ct0;
    private static CipherText ct1;
    private static ECPoint pkc;
    private static BigInteger sk;
    private static CipherText ctc;
    private static ECPoint pc;
    private static byte[] pc_mark;
    private static byte[] pc_mark2;
    private static byte[] ch;
    private static byte[] chr;

    @BeforeAll
    public static void initial(){
        client = new RGClientECOTWithoutSF();
        choice = 0;
        c_mark = 0;

        var group = Constants.getGroup().getG();
        ct0 = new CipherText(group.multiply(BigInteger.valueOf(47865)), group.multiply(BigInteger.valueOf(862456)));
        ct1 = new CipherText(group.multiply(BigInteger.valueOf(78678)), group.multiply(BigInteger.valueOf(175786)));
        sk = BigInteger.valueOf(197302375488L);
        pkc = group.multiply(sk);
        ctc = ct0;
        pc = group.multiply(BigInteger.valueOf(4277278965815L));
        pc_mark = new byte[] { 9, -4, -69, -128, 50, 0 };
        pc_mark2 = new byte[] { 59, -44, -69, 127, -50, 0 };
        ch = new byte[] { 59, -4, -69, -128, 50, 0 };
        chr = new byte[] { 24, -14, -99, -18, 70, 7 };

        var ct0H = new CipherText(group.multiply(BigInteger.valueOf(26842)), group.multiply(BigInteger.valueOf(84658)));
        var ct1H = new CipherText(group.multiply(BigInteger.valueOf(95368)), group.multiply(BigInteger.valueOf(232323)));
        client.receiveStep2(ch, ct0, ct1, ct0H, ct1H);

        try {
            setupField("sk", sk);
            setupField("pkc", pkc);
            setupField("c_mark", c_mark);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    private static void setupField(String name, Object obj) throws NoSuchFieldException, IllegalAccessException {
        var field = client.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(client, obj);
    }

    @BeforeEach
    public void setup(){
        clientTransmitter = mock(IRGClientCommunicatorECOTWithoutSF.class);
        clientLogic = mock(IRGClientLogicECOT.class);

        when(clientLogic.getCTC(c_mark, ct0, ct1)).thenReturn(ctc);
        when(clientLogic.computePC(pkc, sk, ctc)).thenReturn(pc);
        when(clientLogic.computePCMark(pkc, pc)).thenReturn(pc_mark);
        when(clientLogic.computePCMark2(pc_mark)).thenReturn(pc_mark2);
        when(clientLogic.computeCHR(c_mark, pc_mark2, ch)).thenReturn(chr);

        client.setup(clientTransmitter, clientLogic, choice);
        client.step3();
    }

    @Test
    @DisplayName("Step3 calls clientLogic.getCTC, with correct input")
    public void callsGetCTC(){
        verify(clientLogic, times(1)).getCTC(c_mark, ct0, ct1);
    }

    @Test
    @DisplayName("Step3 calls clientLogic.computePC, with correct input")
    public void callsComputePC(){
        verify(clientLogic, times(1)).computePC(pkc, sk, ctc);
    }

    @Test
    @DisplayName("Step3 calls clientLogic.computePCMark, with correct input")
    public void callsComputePCMark(){
        verify(clientLogic, times(1)).computePCMark(pkc, pc);
    }

    @Test
    @DisplayName("Step3 calls clientLogic.computePCMark2, with correct input")
    public void callsComputePCMark2(){
        verify(clientLogic, times(1)).computePCMark2(pc_mark);
    }

    @Test
    @DisplayName("Step3 calls clientLogic.computeCHR, with correct input")
    public void callsComputeCHR(){
        verify(clientLogic, times(1)).computeCHR(c_mark, pc_mark2, ch);
    }

    @Test
    @DisplayName("Step3 calls clientTransmitter.transmitStep3, with correct input")
    public void callsTransmitStep3(){
        verify(clientTransmitter, times(1)).transmitStep3(chr);
    }
}