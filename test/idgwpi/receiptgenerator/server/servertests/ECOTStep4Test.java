package idgwpi.receiptgenerator.server.servertests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.IRGServerLogicECOT;
import idgwpi.receiptgenerator.server.IRGServerCommunicatorECOTWithSF;
import idgwpi.receiptgenerator.server.RGServerECOTWithSF;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ECOTStep4Test {
    private static RGServerECOTWithSF RGServerECOTWithSF;
    private static IRGServerCommunicatorECOTWithSF serverTransmitter;
    private static IRGServerLogicECOT serverLogic;
    private static ECPoint message0;
    private static ECPoint message1;
    private static ECPoint pk0;
    private static ECPoint pk1;
    private static ECPoint p0Point;
    private static ECPoint p1Point;
    private static byte[] s;
    private static byte[] p0_mark;
    private static byte[] p1_mark;
    private static byte[] p0_mark2;
    private static byte[] chr;
    private static byte[]  p0_wave;
    private static byte[]  p1_wave;
    private static byte[]  m0_wave;
    private static byte[]  m1_wave;
    private static boolean doNotAbort;
    private static String errorMessageVerifyCHR;

    @BeforeAll
    public static void initial(){
        RGServerECOTWithSF = new RGServerECOTWithSF();
        var group = Constants.getGroup().getG();
        message0 = group.multiply(BigInteger.valueOf(2756237L));
        message1 = group.multiply(BigInteger.valueOf(6546841L));
        s = new byte[]{59,-4,-69,-128,50,0};
        pk0 = group.multiply(BigInteger.valueOf(98465168L));
        pk1 = group.multiply(BigInteger.valueOf(2354887L));
        p0Point = group.multiply(BigInteger.valueOf(656540841L));
        p1Point = group.multiply(BigInteger.valueOf(6931843156863L));
        p0_mark = new byte[]{-95,123,-103,54,0,0};
        p1_mark = new byte[]{98,2,3,44,-15,-78};
        p0_mark2 = new byte[]{0,2,0,88,57,-45};
        chr = new byte[]{90,27,30,-88,57,45};
        p0_wave = new byte[]{9,-4,-69,-128,50,0};
        p1_wave = new byte[]{59,-44,-69,127,-50,0};
        m0_wave = new byte[]{0,127,3,-8,5,-45};
        m1_wave = new byte[]{94,-64,-9,-28,-50,40};
        doNotAbort = true;
        errorMessageVerifyCHR = "Alice: CHR not equal p''0";
    }

    @BeforeEach
    public void setup(){
        serverTransmitter = mock(IRGServerCommunicatorECOTWithSF.class);
        serverLogic = mock(IRGServerLogicECOT.class);

        when(serverLogic.verifyCHR(chr, p0_mark2)).thenReturn(doNotAbort);
        when(serverLogic.computePWave(pk0, p0Point)).thenReturn(p0_wave);
        when(serverLogic.computePWave(pk1, p1Point)).thenReturn(p1_wave);
        when(serverLogic.computeMWave(p0_wave, message0)).thenReturn(m0_wave);
        when(serverLogic.computeMWave(p1_wave, message1)).thenReturn(m1_wave);

        RGServerECOTWithSF.setup(serverTransmitter, serverLogic, new ECPoint[]{message0, message1});
        RGServerECOTWithSF.receiveStep1(s, pk0);

        try {
            var chrField = RGServerECOTWithSF.getClass().getDeclaredField("chr");
            chrField.setAccessible(true);
            chrField.set(RGServerECOTWithSF, chr);
            var pk1Field = RGServerECOTWithSF.getClass().getDeclaredField("pk1");
            pk1Field.setAccessible(true);
            pk1Field.set(RGServerECOTWithSF, pk1);
            var p0PointField = RGServerECOTWithSF.getClass().getDeclaredField("p0Point");
            p0PointField.setAccessible(true);
            p0PointField.set(RGServerECOTWithSF, p0Point);
            var p1PointField = RGServerECOTWithSF.getClass().getDeclaredField("p1Point");
            p1PointField.setAccessible(true);
            p1PointField.set(RGServerECOTWithSF, p1Point);
            var p0_markField = RGServerECOTWithSF.getClass().getDeclaredField("p0_mark");
            p0_markField.setAccessible(true);
            p0_markField.set(RGServerECOTWithSF, p0_mark);
            var p1_markField = RGServerECOTWithSF.getClass().getDeclaredField("p1_mark");
            p1_markField.setAccessible(true);
            p1_markField.set(RGServerECOTWithSF, p1_mark);
            var p0_mark2Field = RGServerECOTWithSF.getClass().getDeclaredField("p0_mark2");
            p0_mark2Field.setAccessible(true);
            p0_mark2Field.set(RGServerECOTWithSF, p0_mark2);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("Step4 calls serverLogic.verifyCHR, with correct input")
    public void callsVerifyCHR(){
        RGServerECOTWithSF.step4();
        verify(serverLogic, times(1)).verifyCHR(chr, p0_mark2);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.computePWave - pk0, with correct input")
    public void callsComputePWaveWithPK0(){
        RGServerECOTWithSF.step4();
        verify(serverLogic, times(1)).computePWave(pk0, p0Point);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.computePWave - pk1, with correct input")
    public void callsComputePWaveWithPK1(){
        RGServerECOTWithSF.step4();
        verify(serverLogic, times(1)).computePWave(pk1, p1Point);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.computeMWave - p0_wave, with correct input")
    public void callsComputeMWaveWithP0Wave(){
        RGServerECOTWithSF.step4();
        verify(serverLogic, times(1)).computeMWave(p0_wave, message0);
    }

    @Test
    @DisplayName("Step4 calls serverLogic.computeMWave - p1_wave, with correct input")
    public void callsComputeMWaveWithP1Wave(){
        RGServerECOTWithSF.step4();
        verify(serverLogic, times(1)).computeMWave(p1_wave, message1);
    }

    @Test
    @DisplayName("Step4 calls serverTransmitter.transmitStep4 - p1_wave, with correct input")
    public void callsTransmitStep4(){
        RGServerECOTWithSF.step4();
        verify(serverTransmitter, times(1)).transmitStep4(m0_wave, m1_wave, p0_mark, p1_mark);
    }

    @Test
    @DisplayName("Step4 does not call serverLogic.abort after verifyCHR, with correct input")
    public void doesNotCallAbort(){
        RGServerECOTWithSF.step4();
        verify(serverLogic, times(0)).abort(errorMessageVerifyCHR);
    }

    @Test
    @DisplayName("Step4 call serverLogic.abort after verifyCHR, with faulty chr")
    public void callAbortFaultyCHR(){
        var faultyCHR = new byte[]{-2,-3,-121,97,-37,0};
        try {
            var chrField = RGServerECOTWithSF.getClass().getDeclaredField("chr");
            chrField.setAccessible(true);
            chrField.set(RGServerECOTWithSF, faultyCHR);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        when(serverLogic.verifyCHR(faultyCHR, p0_mark2)).thenReturn(false);
        RGServerECOTWithSF.step4();

        verify(serverLogic, times(1)).abort(errorMessageVerifyCHR);
        resetReflections();
    }

    @Test
    @DisplayName("Step4 call serverLogic.abort after verifyCHR, with faulty p0_mark2")
    public void callAbortFaultyP0Mark2(){
        var faultyP0Mark2 = new byte[]{-2,-3,-121,97,-37,0};
        try {
            var p0_mark2Field = RGServerECOTWithSF.getClass().getDeclaredField("p0_mark2");
            p0_mark2Field.setAccessible(true);
            p0_mark2Field.set(RGServerECOTWithSF, faultyP0Mark2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
        when(serverLogic.verifyCHR(chr, faultyP0Mark2)).thenReturn(false);
        RGServerECOTWithSF.step4();

        verify(serverLogic, times(1)).abort(errorMessageVerifyCHR);
        resetReflections();
    }

    private void resetReflections(){
        try {
            var chrField = RGServerECOTWithSF.getClass().getDeclaredField("chr");
            chrField.setAccessible(true);
            chrField.set(RGServerECOTWithSF, chr);
            var p0_mark2Field = RGServerECOTWithSF.getClass().getDeclaredField("p0_mark2");
            p0_mark2Field.setAccessible(true);
            p0_mark2Field.set(RGServerECOTWithSF, p0_mark2);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            fail();
        }
    }
}
