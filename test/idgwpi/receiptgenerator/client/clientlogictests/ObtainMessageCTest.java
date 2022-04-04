package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ObtainMessageCTest {
    @ParameterizedTest
    @MethodSource("inputsAndResults")
    @DisplayName("ObtainMessageC returns expected byte[] given specific input")
    public void returnsExpected(int choice, byte[] pc_wave, byte[] m0_wave, byte[] m1_wave, byte[] expected){
        var clientLogic = new RGClientLogicECOT();

        var actual = clientLogic.obtainMessageC(choice, pc_wave, m0_wave, m1_wave);

        assertArrayEquals(expected, actual);
    }

    private static Stream<Arguments> inputsAndResults(){
        return Stream.of(
                Arguments.of(0, new byte[]{0,0,0,0,0}, new byte[]{127,127,127,127,127},
                        new byte[]{-128,-128,-128,-128,-128}, new byte[]{127,127,127,127,127}),
                Arguments.of(1, new byte[]{0,0,0,0,0}, new byte[]{127,127,127,127,127},
                        new byte[]{-128,-128,-128,-128,-128}, new byte[]{-128,-128,-128,-128,-128}),
                Arguments.of(0, new byte[]{127,127,127,127,127}, new byte[]{127,127,127,127,127},
                        new byte[]{-128,-128,-128,-128,-128}, new byte[]{0,0,0,0,0}),
                Arguments.of(1, new byte[]{-128,-128,-128,-128,-128}, new byte[]{127,127,127,127,127},
                        new byte[]{-128,-128,-128,-128,-128}, new byte[]{0,0,0,0,0})
        );
    }
}
