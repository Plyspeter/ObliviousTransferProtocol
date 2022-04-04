package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComputePK1Test {
    private static X9ECParameters group;
    private static ECPoint g;

    @BeforeAll
    public static void initial(){
        group = Constants.getGroup();
        g = group.getG();
    }

    @ParameterizedTest
    @MethodSource("ecPoints")
    @DisplayName("ComputePK1 returns expected ECPoint given specific ECPoints")
    public void ReturnsExpected(ECPoint q, ECPoint pk0, ECPoint expected){
        var serverLogic = new RGServerLogicECOT();
        var actual = serverLogic.computePK1(q, pk0);

        assertTrue(expected.equals(actual));
    }

    private static Stream<Arguments> ecPoints(){
        return Stream.of(
                Arguments.of(g.multiply(BigInteger.valueOf(42)), g.multiply(BigInteger.valueOf(666)),
                        g.multiply(BigInteger.valueOf(42)).subtract(g.multiply(BigInteger.valueOf(666)))),
                Arguments.of(g.multiply(BigInteger.valueOf(666)), g.multiply(BigInteger.valueOf(42)),
                        g.multiply(BigInteger.valueOf(666)).subtract(g.multiply(BigInteger.valueOf(42)))),
                Arguments.of(g, g.multiply(BigInteger.valueOf(666)),
                        g.subtract(g.multiply(BigInteger.valueOf(666)))),
                Arguments.of(g.multiply(BigInteger.valueOf(666)), g,
                        g.multiply(BigInteger.valueOf(666)).subtract(g)),
                Arguments.of(g.multiply(group.getN()), g.multiply(BigInteger.valueOf(42)),
                        g.multiply(group.getN()).subtract(g.multiply(BigInteger.valueOf(42)))),
                Arguments.of(g.multiply(BigInteger.valueOf(42)), g.multiply(group.getN()),
                        g.multiply(BigInteger.valueOf(42)).subtract(g.multiply(group.getN())))
        );
    }
}
