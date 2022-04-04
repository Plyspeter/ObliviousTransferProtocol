package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
public class ComputeCHTest {
    @Test
    @DisplayName("ComputeCH returns the expected byte[]")
    public void ReturnsExpected(){
        RGServerLogicECOT serverLogic = new RGServerLogicECOT();
        var input1 = new byte[]{42, 37, 45};
        var input2 = new byte[]{66, 66, 66};
        var expected = new byte[]{104, 103, 111};

        var actual = serverLogic.computeCH(input1, input2);

        assertArrayEquals(expected, actual);
    }
}
