package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.*;

public class ComputeP1MinusCMark2Test {
    private static RGClientLogicECOT clientLogic;
    private static byte[] input1;
    private static byte[] input2;
    private static ICryptoSystem cryptoSystem;

    @BeforeAll
    public static void initial(){
        clientLogic = new RGClientLogicECOT();
        cryptoSystem = mock(ICryptoSystem.class);
        input1 = new byte[]{42,42,42};
        input2 = new byte[]{66,66,66};
    }

    @BeforeEach
    public void setup(){
        cryptoSystem = mock(ICryptoSystem.class);
    }

    @ParameterizedTest
    @MethodSource("choices")
    @DisplayName("ComputeP1MinusCMark2 calls hash method hch with correct input")
    public void correctMethodCalled(int choice){
        clientLogic.setup(cryptoSystem);

        clientLogic.computeP1MinusCMark2(choice, input1, input2);

        if (choice == 1) {
            verify(cryptoSystem, times(1)).hch(input1);
            verify(cryptoSystem, times(0)).hch(input2);
        }
        else {
            verify(cryptoSystem, times(1)).hch(input2);
            verify(cryptoSystem, times(0)).hch(input1);
        }
    }

    @ParameterizedTest
    @MethodSource("choices")
    @DisplayName("ComputeP1MinusCMark2 returns expected byte[]")
    public void returnsExpected(int choice){
        clientLogic.setup(cryptoSystem);
        var expected1 = new byte[] {12,42,66};
        var expected2 = new byte[] {24,48,-100};

        when(cryptoSystem.hch(input1)).thenReturn(expected1);
        when(cryptoSystem.hch(input2)).thenReturn(expected2);

        var actual = clientLogic.computeP1MinusCMark2(choice, input1, input2);

        if (choice == 1)
            assertArrayEquals(expected1, actual);
        else
            assertArrayEquals(expected2, actual);
    }

    private static Stream<Arguments> choices(){
        return Stream.of(
                Arguments.of(0),
                Arguments.of(1)
        );
    }
}
