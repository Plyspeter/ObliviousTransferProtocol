package idgwpi.receiptgenerator.client.clientWithoutSelectiveFailuresTests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.client.RGClientECOTWithoutSF;
import idgwpi.receiptgenerator.client.IRGClientLogicECOT;
import idgwpi.receiptgenerator.client.IRGClientCommunicatorECOTWithoutSF;
import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class Step5Test {
    private static RGClientECOTWithoutSF client;
    private static BigInteger sk;
    private static ECPoint pkc;
    private static ECPoint pk1MinusC;
    private static int choice;
    private static int c_mark;
    private static int d;
    private static byte[] ch;
    private static ECPoint p0;
    private static ECPoint p1;
    private static ECPoint pcH;
    private static CipherText ct0;
    private static CipherText ct1;
    private static CipherText ct0H;
    private static CipherText ct1H;
    private static byte[] m0_wave;
    private static byte[] m1_wave;
    private static byte[] pc_wave;
    private static byte[] mH_c;
    private static byte[] p0_mark;
    private static byte[] p1_mark;
    private static byte[] p0_mark2;
    private static byte[] p1_mark2;

    private IRGClientCommunicatorECOTWithoutSF clientTransmitter;
    private IRGClientLogicECOT clientLogic;
    private static boolean doNotAbort;

    private static String errorMessageVerifyCT0;
    private static String errorMessageVerifyCT1;
    private static String errorMessageVerifyCH;

    @BeforeAll
    public static void initial(){
        client = new RGClientECOTWithoutSF();
        choice = 0;
        c_mark = 0;

        var group = Constants.getGroup().getG();

        sk = BigInteger.valueOf(197302375488L);
        pkc = group.multiply(sk);
        pk1MinusC = group.multiply(BigInteger.valueOf(1000));

        p0 = group.multiply(BigInteger.valueOf(23953));
        p1 = group.multiply(BigInteger.valueOf(95462));
        pcH = group.multiply(BigInteger.valueOf(56294));

        ct0 = new CipherText(group.multiply(BigInteger.valueOf(47865)), group.multiply(BigInteger.valueOf(862456)));
        ct1 = new CipherText(group.multiply(BigInteger.valueOf(78678)), group.multiply(BigInteger.valueOf(175786)));

        ch = new byte[] { 59, -4, -69, -128, 50, 0 };

        ct0H = new CipherText(group.multiply(BigInteger.valueOf(12942)), group.multiply(BigInteger.valueOf(138423)));
        ct1H = new CipherText(group.multiply(BigInteger.valueOf(65892)), group.multiply(BigInteger.valueOf(195862)));

        pc_wave = new byte[] { 90, 27, 30, -88, 57, 45 };
        m0_wave = new byte[] { -95, 123, -103, 54, 0, 0 };
        m1_wave = new byte[] { 98, 2, 3, 44, -15, -78 };
        mH_c = new byte[] { 94, -64, -9, -28, -50, 40 };
        p0_mark = new byte[] { 0, 57, 30, -88, 1, 45 };
        p1_mark = new byte[] { 90, 2, 56, -88, 57, 45 };
        p0_mark2 = new byte[] { 2, 27, 30, -42, 57, 0 };
        p1_mark2 = new byte[] { 90, 27, 1, -88, 12, 45 };

        d = 0;

        errorMessageVerifyCT0 = "Bob: Could not verify CT0";
        errorMessageVerifyCT1 = "Bob: Could not verify CT1";
        errorMessageVerifyCH = "Bob: Could not verify CH";

        doNotAbort = true;

        client.receiveStep4(m0_wave, m1_wave, p0.getEncoded(true), p1.getEncoded(true));

        try {
            setupField("ch", ch);
            setupField("c_mark", c_mark);

            setupField("pkc", pkc);
            setupField("pk1MinusC", pk1MinusC);
            setupField("sk", sk);

            setupField("ct0", ct0);
            setupField("ct1", ct1);
            setupField("ct0H", ct0H);
            setupField("ct1H", ct1H);
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

    @Test
    @DisplayName("Step5 calls clientLogic.computePk0")
    public void callsComputePk0() {
        setupSuccess();
        verify(clientLogic, times(1)).computePk0(c_mark, pkc, pk1MinusC);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.computePk1")
    public void callsComputePk1() {
        setupSuccess();
        verify(clientLogic, times(1)).computePk1(c_mark, pkc, pk1MinusC);
    }

    @Test
    @DisplayName("Step 5 calls clientLogic.verifyCT")
    public void callsVerifyCT() {
        setupSuccess();
        verify(clientLogic, times(1)).verifyCT(ct0, p0, pkc);
        verify(clientLogic, times(1)).verifyCT(ct1, p1, pk1MinusC);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.verifyCH, with correct input")
    public void callsVerifyCH(){
        setupSuccess();
        verify(clientLogic, times(1)).verifyCH(ch, p0_mark2, p1_mark2);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.getCTC")
    public void callsGetCTC() {
        setupSuccess();
        verify(clientLogic, times(1)).getCTC(c_mark, ct0H, ct1H);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.computePC")
    public void callsComputePC() {
        setupSuccess();
        verify(clientLogic, times(1)).computePC(pkc, sk, ct0H);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.computePCWave, with correct input")
    public void callsComputePCWave(){
        setupSuccess();
        verify(clientLogic, times(1)).computePCWave(pkc, pcH);
    }

    @Test
    @DisplayName("Step5 calls clientLogic.obtainMessageC, with correct input")
    public void callsObtainMessageC(){
        setupSuccess();
        verify(clientLogic, times(1)).obtainMessageC(c_mark, pc_wave, m0_wave, m1_wave);
    }

    @Test
    @DisplayName("Step 5 calls clientLogic.calculateD")
    public void callsCalculateD() {
        setupSuccess();
        verify(clientLogic, times(1)).calculateD(choice, c_mark);
    }

    @Test
    @DisplayName("Step5 does not call clientLogic.abort after verifyCT, with correct input")
    public void doesNotCallAbortVerifyCT0(){
        setupSuccess();
        verify(clientLogic, times(0)).abort(errorMessageVerifyCT0);
    }

    @Test
    @DisplayName("Step5 does not call clientLogic.abort after verifyCT, with correct input")
    public void doesNotCallAbortVerifyCT1(){
        setupSuccess();
        verify(clientLogic, times(0)).abort(errorMessageVerifyCT1);
    }

    @Test
    @DisplayName("Step5 does not call clientLogic.abort after verifyCH, with correct input")
    public void doesNotCallAbortVerifyCH(){
        setupSuccess();
        verify(clientLogic, times(0)).abort(errorMessageVerifyCH);
    }

    @Test
    @DisplayName("Step5 aborts when failing to verify CT0 with incorrect p0")
    public void callsAbortWithIncorrectCT0() {
        var p0_wrong = Constants.getGroup().getG().multiply(BigInteger.valueOf(195385));
        try {
            setupField("p0", p0_wrong);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }

        setupBasic();
        when(clientLogic.verifyCT(ct0, p0_wrong, pkc)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyCT0);
        resetReflections();
    }

    @Test
    @DisplayName("Step5 aborts when failing to verify CT1 with incorrect p1")
    public void callsAbortWithIncorrectCT1() {
        var p1_wrong = Constants.getGroup().getG().multiply(BigInteger.valueOf(15962));
        try {
            setupField("p1", p1_wrong);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }

        setupBasic();
        when(clientLogic.verifyCT(ct1, p1_wrong, pk1MinusC)).thenReturn(false);
        client.setup(clientTransmitter, clientLogic, choice);
        client.step5();

        verify(clientLogic, times(1)).abort(errorMessageVerifyCT1);
        resetReflections();
    }

    @Test
    @DisplayName("Step5 calls clientLogic.abort, when failing verifyCH with incorrect CH")
    public void callsAbortWithIncorrectCH(){
        var wrongCH = new byte[]{ -2, -3, -121, 97, -37, 0 };
        try {
            setupField("ch", wrongCH);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }

        setupBasic();
        when(clientLogic.verifyCH(wrongCH, p0_mark2, p1_mark2)).thenReturn(false);
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
        clientTransmitter = mock(IRGClientCommunicatorECOTWithoutSF.class);
        clientLogic = mock(IRGClientLogicECOT.class);


        when(clientLogic.computePCMark(pkc, p0)).thenReturn(p0_mark);
        when(clientLogic.computePCMark(pk1MinusC, p1)).thenReturn(p1_mark);
        when(clientLogic.computePCMark2(p0_mark)).thenReturn(p0_mark2);
        when(clientLogic.computePCMark2(p1_mark)).thenReturn(p1_mark2);
        when(clientLogic.computePk0(c_mark, pkc, pk1MinusC)).thenReturn(pkc);
        when(clientLogic.computePk1(c_mark, pkc, pk1MinusC)).thenReturn(pk1MinusC);
        when(clientLogic.verifyCT(ct0, p0, pkc)).thenReturn(doNotAbort);
        when(clientLogic.verifyCT(ct1, p1, pk1MinusC)).thenReturn(doNotAbort);
        when(clientLogic.verifyCH(ch, p0_mark2, p1_mark2)).thenReturn(doNotAbort);
        when(clientLogic.getCTC(c_mark, ct0H, ct1H)).thenReturn(ct0H);
        when(clientLogic.computePC(pkc, sk, ct0H)).thenReturn(pcH);
        when(clientLogic.computePCWave(pkc, pcH)).thenReturn(pc_wave);
        when(clientLogic.obtainMessageC(c_mark, pc_wave, m0_wave, m1_wave)).thenReturn(mH_c);
        when(clientLogic.calculateD(choice, c_mark)).thenReturn(d);
    }

    private void resetReflections(){
        try {
            setupField("ch", ch);
            setupField("p0", p0);
            setupField("p1", p1);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
    }
}