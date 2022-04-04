package idgwpi.receiptgenerator.client.clientWithoutSelectiveFailuresTests;

import idgwpi.receiptgenerator.client.RGClientECOTWithoutSF;
import idgwpi.receiptgenerator.client.IRGClientLogicECOT;
import idgwpi.receiptgenerator.client.IRGClientCommunicatorECOTWithoutSF;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class Step7Test {

    private static RGClientECOTWithoutSF client;
    private static IRGClientLogicECOT logic;

    private static int choice;
    private static byte[] mH_c;
    private static byte[] m0_mark;
    private static byte[] m1_mark;

    private static byte[] message;

    @BeforeAll
    public static void init() {
        client = new RGClientECOTWithoutSF();

        choice = 0;
        mH_c = new byte[] { 116, 26, -74, -56, 51 };
        m0_mark = new byte[] { 118, 26, 28, 86, 120 };
        m1_mark = new byte[] { 17, 47, -112, -71, 13 };

        message = new byte[] {2, 0, -86, -98, 75};

        try {
            setupField("choice", choice);
            setupField("mH_c", mH_c);
            setupField("m0_mark", m0_mark);
            setupField("m1_mark", m1_mark);
        } catch ( NoSuchFieldException | IllegalAccessException e) {
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
    public void setup() {
        var transmitter = mock(IRGClientCommunicatorECOTWithoutSF.class);
        logic = mock(IRGClientLogicECOT.class);

        when(logic.obtainMessageC(choice, mH_c, m0_mark, m1_mark)).thenReturn(message);

        client.setup(transmitter, logic, choice);
        client.step7();
    }

    @Test
    @DisplayName("Step7 calls clientLogic.obtainMessageC")
    public void callsObtainMessageC() {
        verify(logic, times(1)).obtainMessageC(choice, mH_c, m0_mark, m1_mark);
    }
}