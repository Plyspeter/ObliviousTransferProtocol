package idgwpi.receiptgenerator.client.clienttests;

import idgwpi.receiptgenerator.client.RGClientECOTWithSF;
import idgwpi.receiptgenerator.client.IRGClientLogicECOT;
import idgwpi.receiptgenerator.client.IRGClientCommunicatorECOTWithSF;
import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.globals.Constants;
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
    private static RGClientECOTWithSF client;
    private static BigInteger sk;
    private static ECPoint pkc;
    private static int choice;
    private static byte[] ch;
    private static CipherText ct0;
    private static CipherText ct1;
    private static CipherText ctc;
    private static ECPoint pc;
    private static byte[] pc_mark;
    private static byte[] pc_mark2;
    private static byte[] chr;
    private IRGClientCommunicatorECOTWithSF clientTransmitter;
    private IRGClientLogicECOT clientLogic;

    @BeforeAll
    public static void initial(){
        client = new RGClientECOTWithSF();
        var group = Constants.getGroup().getG();
        sk = BigInteger.valueOf(197302375488L);
        pkc = group.multiply(sk);
        choice = 0;
        ch = new byte[]{59,-4,-69,-128,50,0};
        ct0 = new CipherText(group.multiply(BigInteger.valueOf(47865)), group.multiply(BigInteger.valueOf(862456)));
        ct1 = new CipherText(group.multiply(BigInteger.valueOf(78678)), group.multiply(BigInteger.valueOf(175786)));
        ctc = ct0;
        pc = group.multiply(BigInteger.valueOf(4277278965815L));
        pc_mark = new byte[]{9,-4,-69,-128,50,0};
        pc_mark2 = new byte[]{59,-44,-69,127,-50,0};
        chr = new byte[]{24,-14,-99,-18,70,7};

        client.receiveStep2(ch,ct0,ct1);
        try {
            var skField = client.getClass().getDeclaredField("sk");
            var pkcField = client.getClass().getDeclaredField("pkc");
            skField.setAccessible(true);
            pkcField.setAccessible(true);
            skField.set(client, sk);
            pkcField.set(client, pkc);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @BeforeEach
    public void setup(){
        clientTransmitter = mock(IRGClientCommunicatorECOTWithSF.class);
        clientLogic = mock(IRGClientLogicECOT.class);

        when(clientLogic.getCTC(choice, ct0, ct1)).thenReturn(ctc);
        when(clientLogic.computePC(pkc, sk, ctc)).thenReturn(pc);
        when(clientLogic.computePCMark(pkc, pc)).thenReturn(pc_mark);
        when(clientLogic.computePCMark2(pc_mark)).thenReturn(pc_mark2);
        when(clientLogic.computeCHR(choice, pc_mark2, ch)).thenReturn(chr);

        client.setup(clientTransmitter, clientLogic, choice);
        client.step3();
    }

    @Test
    @DisplayName("Step3 calls clientLogic.getCTC, with correct input")
    public void callsGetCTC(){
        verify(clientLogic, times(1)).getCTC(choice, ct0, ct1);
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
        verify(clientLogic, times(1)).computeCHR(choice, pc_mark2, ch);
    }

    @Test
    @DisplayName("Step3 calls clientTransmitter.transmitStep3, with correct input")
    public void callsTransmitStep3(){
        verify(clientTransmitter, times(1)).transmitStep3(chr);
    }

    @Test
    @DisplayName("Step3 stores pc in field variable")
    public void pcStored(){
        try {
            var pcField = client.getClass().getDeclaredField("pc");
            pcField.setAccessible(true);

            assertTrue(pc.equals((ECPoint) pcField.get(client)));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Step3 stores pc_mark in field variable")
    public void pc_markStored(){
        try {
            var pc_markField = client.getClass().getDeclaredField("pc_mark");
            pc_markField.setAccessible(true);

            assertArrayEquals(pc_mark, (byte[]) pc_markField.get(client));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Step3 stores pc_mark2 in field variable")
    public void pc_mark2Stored(){
        try {
            var pc_mark2Field = client.getClass().getDeclaredField("pc_mark2");
            pc_mark2Field.setAccessible(true);

            assertArrayEquals(pc_mark2, (byte[]) pc_mark2Field.get(client));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }
}
