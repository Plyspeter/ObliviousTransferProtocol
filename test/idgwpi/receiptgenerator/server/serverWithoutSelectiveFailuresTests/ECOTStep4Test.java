package idgwpi.receiptgenerator.server.serverWithoutSelectiveFailuresTests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.IRGServerLogicECOT;
import idgwpi.receiptgenerator.server.IRGServerCommunicatorECOTWithoutSF;
import idgwpi.receiptgenerator.server.RGServerECOTWithoutSF;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.*;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class ECOTStep4Test {
    private static RGServerECOTWithoutSF server;
    private static IRGServerCommunicatorECOTWithoutSF serverTransmitter;
    private static IRGServerLogicECOT serverLogic;
    private static ECPoint message0;
    private static ECPoint message1;
    private static ECPoint pk0;
    private static ECPoint pk1;
    private static ECPoint p0Point;
    private static ECPoint p1Point;
    private static ECPoint p0HPoint;
    private static ECPoint p1HPoint;
    private static byte[] s;
    private static byte[] p0_mark2;
    private static byte[] chr;
    private static byte[]  p0_wave;
    private static byte[]  p1_wave;
    private static byte[]  m0H;
    private static byte[]  m1H;
    private static byte[]  m0_wave;
    private static byte[]  m1_wave;
    private static boolean doNotAbort;
    private static String errorMessageVerifyCHR;
    private static int m0Length;

    @BeforeAll
    public static void initial(){
        server = new RGServerECOTWithoutSF();
        var group = Constants.getGroup().getG();
        message0 = group.multiply(BigInteger.valueOf(2756237L));
        message1 = group.multiply(BigInteger.valueOf(6546841L));
        m0Length = message0.getEncoded(true).length;
        s = new byte[]{59,-4,-69,-128,50,0};
        pk0 = group.multiply(BigInteger.valueOf(98465168L));
        pk1 = group.multiply(BigInteger.valueOf(2354887L));
        p0Point = group.multiply(BigInteger.valueOf(656540841L));
        p1Point = group.multiply(BigInteger.valueOf(6931843156863L));
        p0HPoint = group.multiply(BigInteger.valueOf(159435895L));
        p1HPoint = group.multiply(BigInteger.valueOf(1957682358L));
        p0_mark2 = new byte[]{0,2,0,88,57,-45};
        chr = new byte[]{90,27,30,-88,57,45};
        p0_wave = new byte[]{9,-4,-69,-128,50,0};
        p1_wave = new byte[]{59,-44,-69,127,-50,0};
        m0H = new byte[] { 25, 91, -25, -64, 84, 10 };
        m1H = new byte[] { 55, 74, -85, 69, 105, 0 };
        m0_wave = new byte[]{0,127,3,-8,5,-45};
        m1_wave = new byte[]{94,-64,-9,-28,-50,40};
        doNotAbort = true;
        errorMessageVerifyCHR = "Alice: CHR not equal p''0";
    }

    @BeforeEach
    public void setup(){
        serverTransmitter = mock(IRGServerCommunicatorECOTWithoutSF.class);
        serverLogic = mock(IRGServerLogicECOT.class);

        when(serverLogic.verifyCHR(chr, p0_mark2)).thenReturn(doNotAbort);
        when(serverLogic.computePWave(pk0, p0HPoint)).thenReturn(p0_wave);
        when(serverLogic.computePWave(pk1, p1HPoint)).thenReturn(p1_wave);
        when(serverLogic.sampleRandomBinaryString(m0Length)).thenReturn(m0H, m1H);
        when(serverLogic.computeMWave(p0_wave, m0H)).thenReturn(m0_wave);
        when(serverLogic.computeMWave(p1_wave, m1H)).thenReturn(m1_wave);

        server.setup(serverTransmitter, serverLogic, new ECPoint[]{message0, message1});
        server.receiveStep1(s, pk0);
        server.receiveStep3(chr);

        try {
            setupField("pk1", pk1);
            setupField("p0Point", p0Point);
            setupField("p1Point", p1Point);
            setupField("p0_mark2", p0_mark2);
            setupField("p0HPoint", p0HPoint);
            setupField("p1HPoint", p1HPoint);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    private static void setupField(String name, Object obj) throws NoSuchFieldException, IllegalAccessException {
        var field = server.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(server, obj);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.verifyCHR, with correct input")
    public void callsVerifyCHR(){
        server.step4();
        verify(serverLogic, times(1)).verifyCHR(chr, p0_mark2);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.computePWave - pk0, with correct input")
    public void callsComputePWaveWithPK0(){
        server.step4();
        verify(serverLogic, times(1)).computePWave(pk0, p0HPoint);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.computePWave - pk1, with correct input")
    public void callsComputePWaveWithPK1(){
        server.step4();
        verify(serverLogic, times(1)).computePWave(pk1, p1HPoint);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.sampleRandomBinaryString, with correct input")
    public void callsSampleRandomBinaryString() {
        server.step4();
        verify(serverLogic, times(2)).sampleRandomBinaryString(m0Length);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.computeMWave - p0_wave, with correct input")
    public void callsComputeMWaveWithP0Wave(){
        server.step4();
        verify(serverLogic, times(1)).computeMWave(p0_wave, m0H);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.computeMWave - p1_wave, with correct input")
    public void callsComputeMWaveWithP1Wave(){
        server.step4();
        verify(serverLogic, times(1)).computeMWave(p1_wave, m1H);
    }

    @Test
    @DisplayName("Step4 calls serverTransmitter.transmitStep4 - p1_wave, with correct input")
    public void callsTransmitStep4(){
        server.step4();
        verify(serverTransmitter, times(1)).transmitStep4(m0_wave, m1_wave, p0Point, p1Point);
    }

    @Test
    @DisplayName("Step4 does not call serverLogic.abort after verifyCHR, with correct input")
    public void doesNotCallAbort(){
        server.step4();
        verify(serverLogic, times(0)).abort(errorMessageVerifyCHR);
    }

    @Test
    @DisplayName("Step4 call serverLogic.abort after verifyCHR, with faulty chr")
    public void callAbortFaultyCHR(){
        var faultyCHR = new byte[]{-2,-3,-121,97,-37,0};
        try {
            setupField("chr", faultyCHR);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        when(serverLogic.verifyCHR(faultyCHR, p0_mark2)).thenReturn(false);
        server.step4();

        verify(serverLogic, times(1)).abort(errorMessageVerifyCHR);
        resetReflections();
    }

    @Test
    @DisplayName("Step4 call serverLogic.abort after verifyCHR, with faulty p0_mark2")
    public void callAbortFaultyP0Mark2(){
        var faultyP0Mark2 = new byte[]{-2,-3,-121,97,-37,0};
        try {
            setupField("p0_mark2", faultyP0Mark2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        when(serverLogic.verifyCHR(chr, faultyP0Mark2)).thenReturn(false);
        server.step4();

        verify(serverLogic, times(1)).abort(errorMessageVerifyCHR);
        resetReflections();
    }

    private void resetReflections(){
        try {
            setupField("chr", chr);
            setupField("p0_mark2", p0_mark2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
    }
}
