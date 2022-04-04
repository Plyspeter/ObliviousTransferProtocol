package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import idgwpi.globals.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SampleRandomBinaryStringTest {

    @Test
    @DisplayName("SampleRandomBinaryStringTest returns byte[] of security parameter length")
    public void returnByteArrayOfSPLength(){
        var clientLogic = new RGClientLogicECOT();
        var expected = Constants.getSecurityParameter()/8;

        var actual = clientLogic.sampleRandomBinaryString().length;

        assertEquals(expected, actual);
    }

    @RepeatedTest(10)
    @DisplayName("SampleRandomBinaryStringTest returns unique binary strings")
    public void returnsUnique(){
        var clientLogic = new RGClientLogicECOT();
        var firstResult = clientLogic.sampleRandomBinaryString();
        var secondResult = clientLogic.sampleRandomBinaryString();
        var thirdResult = clientLogic.sampleRandomBinaryString();
        var fourthResult = clientLogic.sampleRandomBinaryString();

        assertFalse(
                Arrays.equals(firstResult, secondResult) &&
                        Arrays.equals(firstResult, thirdResult) &&
                        Arrays.equals(firstResult, fourthResult) &&
                        Arrays.equals(secondResult, thirdResult) &&
                        Arrays.equals(secondResult, fourthResult) &&
                        Arrays.equals(thirdResult, fourthResult)
        );
    }
}
