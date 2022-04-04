package idgwpi.receiptgenerator.server.serverWithoutSelectiveFailuresTests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.server.IRGServerLogicECOT;
import idgwpi.receiptgenerator.server.IRGServerCommunicatorECOTWithoutSF;
import idgwpi.receiptgenerator.server.RGServerECOTWithoutSF;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.mockito.Mockito.*;

public class ECOTStep2Test {
    private static RGServerECOTWithoutSF server;
    private static IRGServerCommunicatorECOTWithoutSF serverTransmitter;
    private static IRGServerLogicECOT serverLogic;
    private static ECPoint message0;
    private static ECPoint message1;
    private static ECPoint q;
    private static ECPoint pk0;
    private static ECPoint pk1;
    private static ECPoint p0Point;
    private static ECPoint p1Point;
    private static ECPoint p0HPoint;
    private static ECPoint p1HPoint;
    private static byte[] s;
    private static byte[] p0;
    private static byte[] p1;
    private static byte[] p0H;
    private static byte[] p1H;
    private static byte[] p0_mark;
    private static byte[] p1_mark;
    private static byte[] p0_mark2;
    private static byte[] p1_mark2;
    private static byte[] ch;
    private static CipherText ct0;
    private static CipherText ct1;
    private static CipherText ct0H;
    private static CipherText ct1H;

    @BeforeAll
    public static void initial(){
        server = new RGServerECOTWithoutSF();
        var group = Constants.getGroup().getG();
        message0 = group.multiply(BigInteger.valueOf(2756237L));
        message1 = group.multiply(BigInteger.valueOf(6546841L));
        s = new byte[]{59,-4,-69,-128,50,0};
        q = group.multiply(BigInteger.valueOf(9843745L));
        pk0 = group.multiply(BigInteger.valueOf(98465168L));
        pk1 = group.multiply(BigInteger.valueOf(2354887L));
        p0 = new byte[]{9,-4,-69,-128,50,0};
        p1 = new byte[]{59,-44,-69,127,-50,0};
        p0H = new byte[] { 23, 95, -24, 0, 15, -17 };
        p1H = new byte[] { 74, 16, -18, 127, 35, 2 };
        p0Point = group.multiply(BigInteger.valueOf(656540841L));
        p1Point = group.multiply(BigInteger.valueOf(6931843156863L));
        p0HPoint = group.multiply(BigInteger.valueOf(159435895L));
        p1HPoint = group.multiply(BigInteger.valueOf(1957682358L));
        p0_mark = new byte[]{-95,123,-103,54,0,0};
        p1_mark = new byte[]{98,2,3,44,-15,-78};
        p0_mark2 = new byte[]{0,2,0,88,57,-45};
        p1_mark2 = new byte[]{1,1,1,37,22,-15};
        ch = new byte[]{51,12,91,37,122,-115};

        ct0 = new CipherText(pk0, p0Point);
        ct1 = new CipherText(pk1, p1Point);
        ct0H = new CipherText(pk0, p0HPoint);
        ct1H = new CipherText(pk1, p1HPoint);
    }

    @BeforeEach
    public void setup(){
        serverTransmitter = mock(IRGServerCommunicatorECOTWithoutSF.class);
        serverLogic = mock(IRGServerLogicECOT.class);

        when(serverLogic.computeQ(s)).thenReturn(q);
        when(serverLogic.computePK1(q, pk0)).thenReturn(pk1);
        when(serverLogic.sampleP()).thenReturn(p0, p1, p0H, p1H);
        when(serverLogic.pToECPoint(p0)).thenReturn(p0Point);
        when(serverLogic.pToECPoint(p1)).thenReturn(p1Point);
        when(serverLogic.pToECPoint(p0H)).thenReturn(p0HPoint);
        when(serverLogic.pToECPoint(p1H)).thenReturn(p1HPoint);
        when(serverLogic.computePMark(pk0, p0Point)).thenReturn(p0_mark);
        when(serverLogic.computePMark(pk1, p1Point)).thenReturn(p1_mark);
        when(serverLogic.computePMark2(p0_mark)).thenReturn(p0_mark2);
        when(serverLogic.computePMark2(p1_mark)).thenReturn(p1_mark2);
        when(serverLogic.computeCH(p0_mark2, p1_mark2)).thenReturn(ch);
        when(serverLogic.computeCipherText(pk0, p0Point)).thenReturn(ct0);
        when(serverLogic.computeCipherText(pk1, p1Point)).thenReturn(ct1);
        when(serverLogic.computeCipherText(pk0, p0HPoint)).thenReturn(ct0H);
        when(serverLogic.computeCipherText(pk1, p1HPoint)).thenReturn(ct1H);

        server.setup(serverTransmitter, serverLogic, new ECPoint[]{message0, message1});
        server.receiveStep1(s, pk0);
        server.step2();
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computeQ, with correct input")
    public void callsComputeQ(){
        verify(serverLogic, times(1)).computeQ(s);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computePK1, with correct input")
    public void callsComputePK1(){
        verify(serverLogic, times(1)).computePK1(q, pk0);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.sampleP two times, with correct input")
    public void callsSampleP(){
        verify(serverLogic, times(4)).sampleP();
    }

    @Test
    @DisplayName("Step2 calls serverLogic.pToECPoint - p0, with correct input")
    public void callsPToECPointWithP0(){
        verify(serverLogic, times(1)).pToECPoint(p0);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.pToECPoint - p1, with correct input")
    public void callsPToECPointWithP1(){
        verify(serverLogic, times(1)).pToECPoint(p1);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.pToECPoint - p0H, with correct input")
    public void callsPToECPointWithP0H(){
        verify(serverLogic, times(1)).pToECPoint(p0H);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.pToECPoint - p1H, with correct input")
    public void callsPToECPointWithP1H(){
        verify(serverLogic, times(1)).pToECPoint(p1H);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computePMark - pk0, with correct input")
    public void callsComputePMarkWithPK0(){
        verify(serverLogic, times(1)).computePMark(pk0, p0Point);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computePMark - pk1, with correct input")
    public void callsComputePMarkWithPK1(){
        verify(serverLogic, times(1)).computePMark(pk1, p1Point);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computePMark - p0_mark, with correct input")
    public void callsComputePMark2WithP0Mark(){
        verify(serverLogic, times(1)).computePMark2(p0_mark);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computePMark - p1_mark, with correct input")
    public void callsComputePMark2WithP1Mark(){
        verify(serverLogic, times(1)).computePMark2(p1_mark);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computeCH, with correct input")
    public void callsComputeCH(){
        verify(serverLogic, times(1)).computeCH(p0_mark2, p1_mark2);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computeCipherText - pk0, with correct input")
    public void callsComputeCipherTextWithPK0(){
        verify(serverLogic, times(1)).computeCipherText(pk0, p0Point);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computeCipherText - pk1, with correct input")
    public void callsComputeCipherTextWithPK1(){
        verify(serverLogic, times(1)).computeCipherText(pk1, p1Point);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computeCipherText - pk0H, with correct input")
    public void callsComputeCipherTextWithPK0H(){
        verify(serverLogic, times(1)).computeCipherText(pk0, p0HPoint);
    }

    @Test
    @DisplayName("Step2 calls serverLogic.computeCipherText - pk1H, with correct input")
    public void callsComputeCipherTextWithPK1H(){
        verify(serverLogic, times(1)).computeCipherText(pk1, p1HPoint);
    }

    @Test
    @DisplayName("Step2 calls serverTransmitter.transmitStep2, with correct input")
    public void callsTransmitStep2(){
        verify(serverTransmitter, times(1)).transmitStep2(ch, ct0, ct1, ct0H, ct1H);
    }
}
