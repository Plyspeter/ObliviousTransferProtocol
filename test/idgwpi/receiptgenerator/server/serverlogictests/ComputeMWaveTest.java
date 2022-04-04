package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ComputeMWaveTest {

    @Test
    @DisplayName("ComputeMWave returns the expected byte[]")
    public void ReturnsExpected(){
        RGServerLogicECOT serverLogic = new RGServerLogicECOT();
        var input1 = new byte[]{42, 37, 45};
        var input2 = Constants.getGroup().getG().multiply(BigInteger.TEN);
        var expected = new byte[]{41, -21, -37, 71, 78, 7, 16, -68, 19, 115, 55, 57, -5, -49, 15, 21, -112, 104, -32, 73, 106, 54, 109, 29, 68, 105, 27, 44, -79, 41, -17, 87, 18};

        var actual = serverLogic.computeMWave(input1, input2);

        assertArrayEquals(expected, actual);
    }

    // ----- Without selective failures -----


    @Test
    @DisplayName("ComputeMWave returns the expected byte[]")
    public void ReturnsExpectedWOSF() {
        RGServerLogicECOT serverLogic = new RGServerLogicECOT();
        var input1 = new byte[] { 42, 37, 45 };
        var input2 = new byte[] { 10, 60, -10 };
        var expected = new byte[] { 32, 25, -37 };

        var actual = serverLogic.computeMWave(input1, input2);

        assertArrayEquals(expected, actual);
    }
}
