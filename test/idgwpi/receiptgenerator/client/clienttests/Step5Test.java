package idgwpi.receiptgenerator.client.clienttests;

import idgwpi.receiptgenerator.client.RGClientECOTWithSF;
import idgwpi.receiptgenerator.client.IRGClientLogicECOT;
import idgwpi.receiptgenerator.client.IRGClientCommunicatorECOTWithSF;
import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class Step5Test {
    private static RGClientECOTWithSF client;
    private static BigInteger sk;
    private static ECPoint pkc;
    private static int choice;
    private static byte[] ch;
    private static ECPoint pc;
    private static byte[] pc_mark;
    private static byte[] pc_mark2;
    private static byte[] m0_wave;
    private static byte[] m1_wave;
    private static byte[] p0_mark;
    private static byte[] p1_mark;
    private static byte[] p1MinusC_mark2;
    private static byte[] pc_wave;
    private static byte[] messageByte;
    private static String errorMessageVerifyPCMark;
    private static String errorMessageVerifyCH;
    private IRGClientCommunicatorECOTWithSF clientTransmitter;
    private IRGClientLogicECOT clientLogic;
    private static boolean doNotAbort;

    @BeforeAll
    public static void initial(){
        client = new RGClientECOTWithSF();
        var group = Constants.getGroup().getG();
        sk = BigInteger.valueOf(197302375488L);
        pkc = group.multiply(sk);
        choice = 0;
        ch = new byte[]{59,-4,-69,-128,50,0};
        pc = group.multiply(BigInteger.valueOf(4277278965815L));
        pc_mark = new byte[]{9,-4,-69,-128,50,0};
        pc_mark2 = new byte[]{59,-44,-69,127,-50,0};
        m0_wave = new byte[]{-95,123,-103,54,0,0};
        m1_wave = new byte[]{98,2,3,44,-15,-78};
        p0_mark = new byte[]{0,2,0,88,57,-45};
        p1_mark = new byte[]{1,1,1,37,22,-15};
        p1MinusC_mark2 = new byte[]{51,12,91,37,122,-115};
        pc_wave = new byte[]{90,27,30,-88,57,45};
        messageByte = new byte[]{94,-64,-9,-28,-50,40};
        errorMessageVerifyPCMark = "Bob: Alice p'c not equal Bob p'c";
        errorMessageVerifyCH = "Bob: ch not equal p''1-c XOR p''c";
        doNotAbort = true;

        client.receiveStep4(m0_wave, m1_wave, p0_mark, p1_mark);

        try {
            var chField = client.getClass().getDeclaredField("ch");
            chField.setAccessible(true);
            chField.set(client, ch);
            var pkcField = client.getClass().getDeclaredField("pkc");
            pkcField.setAccessible(true);
            pkcField.set(client, pkc);
            var pcField = client.getClass().getDeclaredField("pc");
            pcField.setAccessible(true);
            pcField.set(client, pc);
            var pc_markField = client.getClass().getDeclaredField("pc_mark");
            pc_markField.setAccessible(true);
            pc_markField.set(client, pc_mark);
            var pc_mark2Field = client.getClass().getDeclaredField("pc_mark2");
            pc_mark2Field.setAccessible(true);
            pc_mark2Field.set(client, pc_mark2);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Step5 calls clientLogic.verifyPCMark, with correct input")
    public void callsVerifyPCMark(){
        setupSuccess();
        verify(clientLogic, times(1)).verifyPCMark(choice, pc_mark, p0_mark, p1_mark);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.computeP1MinusCMark2, with correct input")
    public void callsComputeP1MinusCMark2(){
        setupSuccess();
        verify(clientLogic, times(1)).computeP1MinusCMark2(choice, p0_mark, p1_mark);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.verifyCH, with correct input")
    public void callsVerifyCH(){
        setupSuccess();
        verify(clientLogic, times(1)).verifyCH(ch, p1MinusC_mark2, pc_mark2);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.computePCWave, with correct input")
    public void callsComputePCWave(){
        setupSuccess();
        verify(clientLogic, times(1)).computePCWave(pkc, pc);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.obtainMessageC, with correct input")
    public void callsObtainMessageC(){
        setupSuccess();
        verify(clientLogic, times(1)).obtainMessageC(choice, pc_wave, m0_wave, m1_wave);
    }

    @Test
    @DisplayName("Step5 does not call clientLogic.abort after verifyPCMark, with correct input")
    public void doesNotCallAbortVerifyPCMark(){
        setupSuccess();
        verify(clientLogic, times(0)).abort(errorMessageVerifyPCMark);
    }

    @Test
    @DisplayName("Step5 does not call clientLogic.abort after verifyCH, with correct input")
    public void doesNotCallAbortVerifyCH(){
        setupSuccess();
        verify(clientLogic, times(0)).abort(errorMessageVerifyCH);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.abort, when failing verifyPCMark with incorrect choice")
    public void callsAbortWithIncorrectChoice(){
        var wrongChoice = 1;
        setupBasic();
        when(clientLogic.verifyPCMark(wrongChoice, pc_mark, p0_mark, p1_mark)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, wrongChoice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyPCMark);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.abort, when failing verifyPCMark with incorrect pc_mark")
    public void callsAbortWithIncorrectPCMark(){
        var wrongPCMark = new byte[]{-2,-3,-121,97,-37,0};
        try {
            var pc_markField = client.getClass().getDeclaredField("pc_mark");
            pc_markField.setAccessible(true);
            pc_markField.set(client, wrongPCMark);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }

        setupBasic();
        when(clientLogic.verifyPCMark(choice, wrongPCMark, p0_mark, p1_mark)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyPCMark);

        resetReflections();
    }

    @Test
    @DisplayName("Step5 calls clientLogic.abort, when failing verifyPCMark with incorrect p0_mark")
    public void callsAbortWithIncorrectP0Mark(){
        var wrongP0Mark = new byte[]{-2,-3,-121,97,-37,0};
        setupBasic();
        client.receiveStep4(m0_wave, m1_wave, wrongP0Mark, p1_mark);
        when(clientLogic.verifyPCMark(choice, pc_mark, wrongP0Mark, p1_mark)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyPCMark);

        resetReceived();
    }

    @Test
    @DisplayName("Step5 calls clientLogic.abort, when failing verifyPCMark with incorrect p1_mark")
    public void callsAbortWithIncorrectP1Mark(){
        var wrongP1Mark = new byte[]{-2,-3,-121,97,-37,0};
        setupBasic();
        client.receiveStep4(m0_wave, m1_wave, p0_mark, wrongP1Mark);
        when(clientLogic.verifyPCMark(choice, pc_mark, p0_mark, wrongP1Mark)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyPCMark);

        resetReceived();
    }

    @Test
    @DisplayName("Step5 calls clientLogic.abort, when failing verifyCH with incorrect CH")
    public void callsAbortWithIncorrectCH(){
        var wrongCH = new byte[]{-2,-3,-121,97,-37,0};
        try {
            var chField = client.getClass().getDeclaredField("ch");
            chField.setAccessible(true);
            chField.set(client, wrongCH);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }

        setupBasic();
        when(clientLogic.verifyCH(wrongCH, p1MinusC_mark2, pc_mark2)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyCH);

        resetReflections();
    }

    @Test
    @DisplayName("Step5 calls clientLogic.abort, when failing verifyCH with incorrect p1MinusC_mark2")
    public void callsAbortWithIncorrectP1MinusCMark2(){
        var wrongP1Mark = new byte[]{-2,-3,-121,97,-37,0};
        var wrongP1MinusCMark2 = new byte[]{-52,-83,-11,7,37,50};

        client.receiveStep4(m0_wave, m1_wave, p0_mark, wrongP1Mark);
        setupBasic();
        when(clientLogic.computeP1MinusCMark2(choice, p0_mark, wrongP1Mark)).thenReturn(wrongP1MinusCMark2);
        when(clientLogic.verifyCH(ch, wrongP1MinusCMark2, pc_mark2)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyCH);

        resetReceived();
    }

    @Test
    @DisplayName("Step5 calls clientLogic.abort, when failing verifyCH with incorrect pc_mark2")
    public void callsAbortWithIncorrectPCMark2(){
        var wrongPCMark2 = new byte[]{-2,-3,-121,97,-37,0};
        try {
            var pc_mark2Field = client.getClass().getDeclaredField("pc_mark2");
            pc_mark2Field.setAccessible(true);
            pc_mark2Field.set(client, wrongPCMark2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }

        setupBasic();
        when(clientLogic.verifyCH(ch, p1MinusC_mark2, wrongPCMark2)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyCH);

        resetReflections();
    }

    private void setupSuccess(){
        setupBasic();
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();
    }

    private void setupBasic(){
        clientTransmitter = mock(IRGClientCommunicatorECOTWithSF.class);
        clientLogic = mock(IRGClientLogicECOT.class);

        when(clientLogic.verifyPCMark(choice, pc_mark, p0_mark, p1_mark)).thenReturn(doNotAbort);
        when(clientLogic.computeP1MinusCMark2(choice, p0_mark, p1_mark)).thenReturn(p1MinusC_mark2);
        when(clientLogic.verifyCH(ch, p1MinusC_mark2, pc_mark2)).thenReturn(doNotAbort);
        when(clientLogic.computePCWave(pkc, pc)).thenReturn(pc_wave);
        when(clientLogic.obtainMessageC(choice, pc_wave, m0_wave, m1_wave)).thenReturn(messageByte);
    }

    private void resetReflections(){
        try {
            var pc_markField = client.getClass().getDeclaredField("pc_mark");
            pc_markField.setAccessible(true);
            pc_markField.set(client, pc_mark);
            var chField = client.getClass().getDeclaredField("ch");
            chField.setAccessible(true);
            chField.set(client, ch);
            var pc_mark2Field = client.getClass().getDeclaredField("pc_mark2");
            pc_mark2Field.setAccessible(true);
            pc_mark2Field.set(client, pc_mark2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
    }

    private void resetReceived(){
        client.receiveStep4(m0_wave, m1_wave, p0_mark, p1_mark);
    }
}
