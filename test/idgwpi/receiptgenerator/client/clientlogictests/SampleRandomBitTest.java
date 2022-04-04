package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SampleRandomBitTest {

    @RepeatedTest(30)
    @DisplayName("Bit is always 0 or 1")
    public void testBitRange() {
        var logic = new RGClientLogicECOT();
        var result = logic.sampleRandomBit();

        assertTrue(result == 1 || result == 0);
    }

    @Test
    @DisplayName("Bit can be 0")
    public void randomSampleCanBeZero() {
        var logic = new RGClientLogicECOT();

        while(true) {
            var result = logic.sampleRandomBit();

            if (result == 0)
                break;
        }
    }

    @Test
    @DisplayName("Bit can be 1")
    public void randomSampleCanBeOne() {
        var logic = new RGClientLogicECOT();

        while(true) {
            var result = logic.sampleRandomBit();

            if (result == 1)
                break;
        }
    }
}