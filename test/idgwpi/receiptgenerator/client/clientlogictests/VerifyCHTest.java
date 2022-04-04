package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class VerifyCHTest {
    @ParameterizedTest
    @MethodSource("inputAndResults")
    @DisplayName("VerifyCH returns expected boolean given specific input")
    public void returnsExpected(byte[] ch, byte[] p1MinusC_mark2, byte[] pc_mark2, boolean expected){
        var clientLogic = new RGClientLogicECOT();

        var actual = clientLogic.verifyCH(ch, p1MinusC_mark2, pc_mark2);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> inputAndResults(){
        return Stream.of(
                Arguments.of(new byte[]{0,0,0,0,0}, new byte[]{127,127,127,127,127}, new byte[]{127,127,127,127,127}, true),
                Arguments.of(new byte[]{-128,-128,-128,-128,-128}, new byte[]{127,127,127,127,127}, new byte[]{-128,-128,-128,-128,-128}, false)
        );
    }
}
