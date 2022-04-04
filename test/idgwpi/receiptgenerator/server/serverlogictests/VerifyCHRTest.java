package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerifyCHRTest {
    @ParameterizedTest
    @MethodSource("inputAndResults")
    @DisplayName("VerifyCHR returns expected boolean given specific input")
    public void returnsExpected(byte[] input1, byte[] input2, boolean expected){
        var serverLogic = new RGServerLogicECOT();

        var actual = serverLogic.verifyCHR(input1, input2);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> inputAndResults(){
        return Stream.of(
                Arguments.of(new byte[]{0,0,0,0,0}, new byte[]{0,0,0,0,0}, true),
                Arguments.of(new byte[]{0,0,0,0,0}, new byte[]{0,0,0,0,-1}, false),
                Arguments.of(new byte[]{0,0,0,0,0}, new byte[]{127,127,127,127,127}, false),
                Arguments.of(new byte[]{0,0,0,0,0}, new byte[]{-128,-128,-128,-128,-128}, false),
                Arguments.of(new byte[]{127,127,127,127,127}, new byte[]{127,127,127,127,127}, true),
                Arguments.of(new byte[]{127,127,127,127,127}, new byte[]{-128,-128,-128,-128,-128}, false),
                Arguments.of(new byte[]{-128,-128,-128,-128,-128}, new byte[]{-128,-128,-128,-128,127}, false),
                Arguments.of(new byte[]{0,0,0,0,0}, new byte[]{0,0,0,0,0,0}, false),
                Arguments.of(new byte[]{0,0,0,0,0}, new byte[]{0,0,0,0}, false),
                Arguments.of(new byte[]{25,6,-68,54,-42}, new byte[]{25,6,-68,54,-42}, true),
                Arguments.of(new byte[]{-25,-6,68,-54,42}, new byte[]{25,6,-68,54,-42}, false)
        );
    }
}
