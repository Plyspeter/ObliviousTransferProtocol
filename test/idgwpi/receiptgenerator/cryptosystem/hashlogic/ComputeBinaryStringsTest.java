package idgwpi.receiptgenerator.cryptosystem.hashlogic;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.cryptosystem.HashLogic;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ComputeBinaryStringsTest {
    private static HashLogic hashLogic;
    private static byte[] prefix;
    private static X9ECParameters group;

    @BeforeAll
    public static void setup(){
        hashLogic = new HashLogic();
        prefix = "PREFIX".getBytes();
        group = Constants.getGroup();
    }

    @RepeatedTest(10)
    @DisplayName("ComputeBinaryStrings returns expected byte arrays for 1 ecpoint")
    public void ReturnsExpectedByteArraysPer1EC(){
        var numberOfPoints = 1;
        var input = generateECPoints(numberOfPoints);

        var actual = hashLogic.computeBinaryStrings(prefix, input);

        assertArrayEquals(prefix, actual[0]);

        for (int i = 1; i < actual.length; i++){
            assertArrayEquals(input[i-1].getEncoded(true), actual[i]);
        }
    }

    @RepeatedTest(10)
    @DisplayName("ComputeBinaryStrings returns expected byte arrays for 2 ecpoints")
    public void ReturnsExpectedByteArraysPer2EC(){
        var numberOfPoints = 2;
        var input = generateECPoints(numberOfPoints);

        var actual = hashLogic.computeBinaryStrings(prefix, input);

        assertArrayEquals(prefix, actual[0]);

        for (int i = 1; i < actual.length; i++){
            assertArrayEquals(input[i-1].getEncoded(true), actual[i]);
        }
    }

    @RepeatedTest(10)
    @DisplayName("ComputeBinaryStrings returns expected byte arrays for 0 ecpoints")
    public void ReturnsExpectedByteArraysPer0EC(){
        var numberOfPoints = 0;
        var input = generateECPoints(numberOfPoints);

        var actual = hashLogic.computeBinaryStrings(prefix, input);

        assertArrayEquals(prefix, actual[0]);

        for (int i = 1; i < actual.length; i++){
            assertArrayEquals(input[i-1].getEncoded(true), actual[i]);
        }
    }

    @RepeatedTest(10)
    @DisplayName("ComputeBinaryStrings returns expected byte arrays for X ecpoints")
    public void ReturnsExpectedByteArraysPerXEC(){
        var numberOfPoints = new Random().nextInt(10) + 1;
        var input = generateECPoints(numberOfPoints);

        var actual = hashLogic.computeBinaryStrings(prefix, input);

        assertArrayEquals(prefix, actual[0]);

        for (int i = 1; i < actual.length; i++){
            assertArrayEquals(input[i-1].getEncoded(true), actual[i]);
        }
    }

    private ECPoint[] generateECPoints(int numberOfPoints){
        var ecpoints = new ECPoint[numberOfPoints];

        for (int i = 0; i < numberOfPoints; i++){
            ecpoints[i] = group.getG().multiply(BigInteger.valueOf(new Random().nextInt(2000000)));
        }

        return ecpoints;
    }
}
