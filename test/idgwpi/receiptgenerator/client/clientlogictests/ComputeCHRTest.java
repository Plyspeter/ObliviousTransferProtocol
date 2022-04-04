package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ComputeCHRTest {
    private static RGClientLogicECOT clientLogic;
    private static byte[] pc_mark2;
    private static byte[] ch;

    @BeforeAll
    public static void initial(){
        clientLogic = new RGClientLogicECOT();
        pc_mark2 = new byte[] {21, 58, -127};
        ch = new byte[] {15, 15, 42};
    }

    @Test
    @DisplayName("ComputeCHR returns pc_mark2 if choice is 0")
    public void returnsExpectedChoice0(){
        var actual = clientLogic.computeCHR(0, pc_mark2, ch);

        assertArrayEquals(pc_mark2, actual);
    }

    @Test
    @DisplayName("ComputeCHR returns pc_mark2 XOR ch if choice is 1")
    public void returnsExpectedChoice1(){
        var expected = new byte[] {26, 53, -85};

        var actual = clientLogic.computeCHR(1, pc_mark2, ch);

        assertArrayEquals(expected, actual);
    }
}
