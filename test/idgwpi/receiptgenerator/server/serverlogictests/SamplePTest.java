package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SamplePTest {

    @Test
    @DisplayName("SampleP returns byte[] of security parameter length")
    public void returnByteArrayOfSPLength(){
        var serverLogic = new RGServerLogicECOT();
        var expected = Constants.getSecurityParameter()/8;

        var actual = serverLogic.sampleP().length;

        assertEquals(expected, actual);
    }

    @RepeatedTest(10)
    @DisplayName("SampleP returns unique binary strings")
    public void returnsUnique(){
        var serverLogic = new RGServerLogicECOT();
        var firstResult = serverLogic.sampleP();
        var secondResult = serverLogic.sampleP();
        var thirdResult = serverLogic.sampleP();
        var fourthResult = serverLogic.sampleP();

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
