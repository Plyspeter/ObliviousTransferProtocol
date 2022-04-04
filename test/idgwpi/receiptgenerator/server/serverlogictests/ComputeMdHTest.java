package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ComputeMdHTest {

    @Test
    @DisplayName("Compute MdH with d = 0")
    public void computeWithd0() {
        var logic = new RGServerLogicECOT();
        var m0H = new byte[] { 25, 91, -25, -64, 84, 10 };
        var m1H = new byte[] { 55, 74, -85, 69, 105, 0 };
        var d = 0;

        var res = logic.computeMdH(d, m0H, m1H);

        assertArrayEquals(m0H, res);
    }

    @Test
    @DisplayName("Compute MdH with d = 1")
    public void computeWithd1() {
        var logic = new RGServerLogicECOT();
        var m0H = new byte[] { 25, 91, -25, -64, 84, 10 };
        var m1H = new byte[] { 55, 74, -85, 69, 105, 0 };
        var d = 1;

        var res = logic.computeMdH(d, m0H, m1H);

        assertArrayEquals(m1H, res);
    }
}