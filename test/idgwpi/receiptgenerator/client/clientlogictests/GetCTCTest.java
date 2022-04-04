package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.globals.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetCTCTest {
    @ParameterizedTest
    @MethodSource("inputs")
    @DisplayName("GetCTC returns expected CipherText given specific choice")
    public void ReturnsExpected(int choice){
        var group = Constants.getGroup();
        var g = group.getG();
        var ct1 = new CipherText(g.multiply(BigInteger.valueOf(42)), g.multiply(BigInteger.valueOf(666)));
        var ct2 = new CipherText(g.multiply(BigInteger.ONE), g.multiply(group.getN()));
        var clientLogic = new RGClientLogicECOT();
        CipherText expected;
        if(choice == 0)
            expected = ct1;
        else
            expected = ct2;

        var actual = clientLogic.getCTC(choice, ct1, ct2);

        assertTrue(expected.getCipher1().equals(actual.getCipher1()) &&
                        expected.getCipher2().equals(actual.getCipher2()));
    }

    private static Stream<Arguments> inputs(){
        return Stream.of(
                Arguments.of(1),
                Arguments.of(0)
        );
    }
}
