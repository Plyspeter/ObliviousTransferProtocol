package idgwpi.receiptgenerator.cryptosystem.hashlogic;

import idgwpi.receiptgenerator.cryptosystem.HashLogic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ComputeConcatenatedBinaryStringLengthTest {
    private static int arrayStandardLength;
    private static SecureRandom SRandom;
    private static HashLogic hashLogic;
    private static int prefixLength;

    @BeforeAll
    public static void setup(){
        arrayStandardLength = 256/8;
        SRandom = new SecureRandom();
        hashLogic = new HashLogic();
        prefixLength = "PREFIX".getBytes().length;
    }

    @RepeatedTest(10)
    @DisplayName("ComputeConcatenatedBinaryStringLength returns expected length for 1 array")
    public void ReturnsExpectedLength1Arrays(){
        var numberOfArrays = 1;
        var input = generateByteArrays(numberOfArrays);
        var expected = arrayStandardLength * (numberOfArrays-1) + prefixLength;

        var actual = hashLogic.computeConcatenatedBinaryStringLength(input);

        assertEquals(expected, actual);
    }

    @RepeatedTest(10)
    @DisplayName("ComputeConcatenatedBinaryStringLength returns expected length for 2 arrays")
    public void ReturnsExpectedLength2Arrays(){
        var numberOfArrays = 2;
        var input = generateByteArrays(numberOfArrays);
        var expected = arrayStandardLength * (numberOfArrays-1) + prefixLength;

        var actual = hashLogic.computeConcatenatedBinaryStringLength(input);

        assertEquals(expected, actual);
    }


    @RepeatedTest(10)
    @DisplayName("ComputeConcatenatedBinaryStringLength returns expected length for 3 arrays")
    public void ReturnsExpectedLength3Arrays(){
        var numberOfArrays = 3;
        var input = generateByteArrays(numberOfArrays);
        var expected = arrayStandardLength * (numberOfArrays-1) + prefixLength;

        var actual = hashLogic.computeConcatenatedBinaryStringLength(input);

        assertEquals(expected, actual);
    }

    @RepeatedTest(10)
    @DisplayName("ComputeConcatenatedBinaryStringLength returns expected length for X arrays")
    public void ReturnsExpectedLengthXArrays(){
        var numberOfArrays = new Random().nextInt(10) + 1;
        var input = generateByteArrays(numberOfArrays);
        var expected = arrayStandardLength * (numberOfArrays-1) + prefixLength;

        var actual = hashLogic.computeConcatenatedBinaryStringLength(input);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("ComputeConcatenatedBinaryStringLength returns expected length for 0 arrays")
    public void ReturnsExpectedLength0Arrays(){
        var numberOfArrays = 0;
        var input = generateByteArrays(numberOfArrays);
        var expected = arrayStandardLength * numberOfArrays;

        var actual = hashLogic.computeConcatenatedBinaryStringLength(input);

        assertEquals(expected, actual);
    }

    private byte[][] generateByteArrays(int numberOfArrays){
        var result = new byte[numberOfArrays][];
        var i = 0;

        if (numberOfArrays != 0){
            result[i++] = "PREFIX".getBytes();
        }

        while (i < numberOfArrays){
            var array = new byte[arrayStandardLength];
            SRandom.nextBytes(array);
            result[i++] = array;
        }

        return result;
    }
}
