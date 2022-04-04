package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VerifyPCMarkTest {

    @ParameterizedTest
    @MethodSource("inputAndResults")
    @DisplayName("VerifyPCMarkTest returns expected boolean given specific input")
    public void returnsExpected(int choice, byte[] pc_mark, byte[] p0_mark, byte[] p1_mark, boolean expected){
        var clientLogic = new RGClientLogicECOT();

        var actual = clientLogic.verifyPCMark(choice, pc_mark, p0_mark, p1_mark);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> inputAndResults(){
        return Stream.of(
                Arguments.of(1, new byte[]{12,12,12,12,12}, new byte[]{0,0,0,0,0}, new byte[]{12,12,12,12,12}, true),
                Arguments.of(0, new byte[]{12,12,12,12,12}, new byte[]{0,0,0,0,0}, new byte[]{12,12,12,12,12}, false),
                Arguments.of(1, new byte[]{127,127,127,127,127}, new byte[]{127,127,127,127,-127}, new byte[]{127,127,127,127,127}, true),
                Arguments.of(0, new byte[]{127,127,127,127,127}, new byte[]{127,127,127,127,-127}, new byte[]{127,127,127,127,127}, false),
                Arguments.of(1, new byte[]{25,6,-68,54,-42}, new byte[]{-25,-6,68,-54,42}, new byte[]{25,6,-68,54,-42}, true),
                Arguments.of(0, new byte[]{25,6,-68,54,-42}, new byte[]{-25,-6,68,-54,42}, new byte[]{25,6,-68,54,-42}, false),
                Arguments.of(1, new byte[]{-128,-128,-128,-128,-128}, new byte[]{127,127,127,127,127}, new byte[]{-128,-128,-128,-128,-128}, true),
                Arguments.of(0, new byte[]{-128,-128,-128,-128,-128}, new byte[]{127,127,127,127,127}, new byte[]{-128,-128,-128,-128,-128}, false)
        );
    }
}
