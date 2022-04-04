package idgwpi.receiptgenerator.cryptosystem.hashlogic;

import idgwpi.receiptgenerator.cryptosystem.HashLogic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HashTest {
    private static HashLogic hashLogic;

    @BeforeAll
    public static void initial(){
        hashLogic = new HashLogic();

    }

    @ParameterizedTest
    @MethodSource("ByteArrays")
    @DisplayName("Hash returns expected byte array")
    public void ReturnsExpectedByteArray(byte[] input, byte[] expected){
        var actual = hashLogic.hash(input);

        System.out.println(Arrays.toString(actual));

        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("Hash returns different results for different arrays.")
    public void ReturnsDifferentByteArray(){
        var arrays = ByteArrays().collect(Collectors.toList());
        var results = new byte[14][];
        int i = 0;

        for(Arguments args : arrays){
            results[i++] = hashLogic.hash((byte[]) args.get()[0]);
            results[i++] = hashLogic.hash((byte[]) args.get()[1]);
        }

        for(i = 0; i < results.length; i++){
            for(int j = 0; j < results.length; j++){
                if(j == i) continue;
                assertNotEquals(results[i], results[j]);
            }
        }
    }

    @Test
    @DisplayName("Hash throws expected error if hash algorithm is wrong")
    public void ThrowsExpectedErrorAlgo(){
        try {
            var hashLogic = new HashLogic();
            var hashAlgorithmField = hashLogic.getClass().getDeclaredField("hashAlgorithm");
            hashAlgorithmField.setAccessible(true);
            hashAlgorithmField.set(hashLogic, "non-existent hash algorithm");

            var exception = assertThrows(RuntimeException.class, () -> hashLogic.hash(new byte[]{42, 66}));
            assertEquals("Failed to hash: Algorithm failure", exception.getMessage());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Hash throws expected error if provider is wrong")
    public void ThrowsExpectedErrorProv(){
        try {
            var hashLogic = new HashLogic();

            var securityProviderField = hashLogic.getClass().getDeclaredField("securityProvider");
            securityProviderField.setAccessible(true);

            securityProviderField.set(hashLogic, "non-existent provider");

            var exception = assertThrows(RuntimeException.class, () -> hashLogic.hash(new byte[]{42, 66}));
            assertEquals("Failed to hash: Provider failure", exception.getMessage());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }

    private static Stream<Arguments> ByteArrays() {
        return Stream.of(
                Arguments.of(
                        new byte[]{0, -58, 127, 54, -101, 77, 43, -109, 86, 73, -29, 65, 60, -78, -46, 13, 119, 101, -106, -123, -102, -99, -82, -45, -124, -3, -74, -12, 111, 19, 63, 88, 71},
                        new byte[]{-88, 45, 40, 79, -1, -44, 103, -50, -5, 38, -79, 58, -87, 63, 75, -82, 99, 123, -54, -61, 126, 69, -108, -22, 22, 85, -56, -94, 34, -76, -29, 67}
                        ),
                Arguments.of(
                        new byte[]{118, 83, -19, -4, 85, -52, 81, 80, 70, -21, -18, 38, -59, -55, -86, -67, -89, 127, 7, 123, 56, 73, -94, -10, -98, -29, 91, -61, 49, 43, 127, -108},
                        new byte[]{65, 76, 59, -97, 101, -11, 90, -60, 56, -4, 50, 58, -52, 10, -108, -61, -80, -78, -40, -63, -12, 77, 34, 67, 69, -8, -19, 78, 71, 118, -83, -37}
                        ),
                Arguments.of(
                        new byte[]{88, 112, -27, 2, -59, 63, -70, 98, -107, 86, -100, 26, -2, -70, -120, 111, -86, -27, 72, -60, 125, -27, 35, -63, 24, -100, -11, -38, 39, -116, 92, -106},
                        new byte[]{125, 23, 74, -64, 14, 84, -26, 66, -51, 88, 8, -101, 59, -69, -33, -56, -37, 43, -93, 30, -98, -108, -10, -128, 58, 74, -127, 55, -89, -75, 47, 113}
                        ),
                Arguments.of(
                        new byte[]{0, -42, -6, 59, -73, 38, 115, 89, -128, 35, 76, -69, -2, -29, -15, -53, 127, 26, 98, 39, 28, 50, 117, -81, -8, 61, 114, -25, 86, 60, 118, 92, 36},
                        new byte[]{-32, -124, -17, 105, 4, 39, 127, 122, 47, -94, 70, -39, -79, 74, -18, -83, -50, -46, -43, -105, 78, 110, -96, -117, 123, 25, -84, -98, 17, -69, -104, 50}
                        ),
                Arguments.of(
                        new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        new byte[]{-36, 51, 41, 110, 77, 32, -16, -17, 53, -1, -97, -44, 73, -30, 62, -69, -86, 90, 4, -102, 23, 119, -99, -77, -62, -2, 25, 75, 73, -102, -81, 116}
                        ),
                Arguments.of(
                        new byte[]{-128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128, -128},
                        new byte[]{37, 47, 97, 49, 72, -115, 53, -3, -31, 48, -8, -113, 73, -66, -119, 35, 100, -85, 96, -47, 81, 10, 109, -14, 66, -117, -48, 10, 86, 92, 60, 16}
                        ),
                Arguments.of(
                        new byte[]{127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127, 127},
                        new byte[]{-56, -39, -28, 40, 105, -114, -123, -29, -57, -6, -22, -47, 15, 98, 22, -19, 13, -103, -20, 78, -85, 2, 11, -111, 101, 20, -12, -3, -10, 75, -127, -77}
                        )
        );
    }
}
