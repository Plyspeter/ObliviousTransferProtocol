package idgwpi.receiptgenerator.server.serverWithoutSelectiveFailuresTests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.IRGServerLogicECOT;
import idgwpi.receiptgenerator.server.IRGServerCommunicatorECOTWithoutSF;
import idgwpi.receiptgenerator.server.RGServerECOTWithoutSF;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class Step6Test {

    private static RGServerECOTWithoutSF server;
    private static IRGServerCommunicatorECOTWithoutSF transmitter;
    private static IRGServerLogicECOT logic;

    private static ECPoint message0;
    private static ECPoint message1;

    private static int d;
    private static byte[] m0H;
    private static byte[] m1H;
    private static byte[] m0_mark;
    private static byte[] m1_mark;

    @BeforeAll
    public static void init() {
        server = new RGServerECOTWithoutSF();
        var group = Constants.getGroup().getG();
        message0 = group.multiply(BigInteger.valueOf(2756237L));
        message1 = group.multiply(BigInteger.valueOf(6546841L));

        d = 0;
        m0H = new byte[] { 25, 91, -25, -64, 84, 10 };
        m1H = new byte[] { 55, 74, -85, 69, 105, 0 };
        m0_mark = new byte[] { 118, 26, 28, 86, 120 };
        m1_mark = new byte[] { 17, 47, -112, -71, 13 };

        server.receiveStep5(d);

        try {
            setupField("m0H", m0H);
            setupField("m1H", m1H);
        } catch ( IllegalAccessException | NoSuchFieldException e ) {
            e.printStackTrace();
            fail();
        }
    }

    private static void setupField(String name, Object obj) throws NoSuchFieldException, IllegalAccessException {
        var field = server.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(server, obj);
    }

    @BeforeEach
    public void setup() {
        transmitter = mock(IRGServerCommunicatorECOTWithoutSF.class);
        logic = mock(IRGServerLogicECOT.class);

        when(logic.computeMdH(d, m0H, m1H)).thenReturn(m0H);
        when(logic.computeM1dH(d, m0H, m1H)).thenReturn(m1H);
        when(logic.computeMMark(m0H, message0)).thenReturn(m0_mark);
        when(logic.computeMMark(m1H, message1)).thenReturn(m1_mark);

        server.setup(transmitter, logic, new ECPoint[]{message0, message1});
        server.step6();
    }

    @Test
    @DisplayName("Step6 calls serverLogic.computeMdH")
    public void callsComputeMdH(){
        verify(logic, times(1)).computeMdH(d, m0H, m1H);
    }

    @Test
    @DisplayName("Step6 calls serverLogic.computeM1dH")
    public void callsComputeM1dH(){
        verify(logic, times(1)).computeM1dH(d, m0H, m1H);
    }

    @Test
    @DisplayName("Step6 calls serverLogic.computeMMark with 0 as input")
    public void callsComputeMMark0(){
        verify(logic, times(1)).computeMMark(m0H, message0);
    }

    @Test
    @DisplayName("Step6 calls serverLogic.computeMMark with 1 as input")
    public void callsComputeMMark1(){
        verify(logic, times(1)).computeMMark(m1H, message1);
    }

    @Test
    @DisplayName("Step6 calls serverTransmitter.transmitStep6")
    public void callsTransmitStep4(){
        verify(transmitter, times(1)).transmitStep6(m0_mark, m1_mark);
    }
}