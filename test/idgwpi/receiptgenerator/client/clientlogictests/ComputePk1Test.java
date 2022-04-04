package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.globals.Constants;
import idgwpi.receiptgenerator.helpers.RandomBigInteger;
import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ComputePk1Test {

    @RepeatedTest(10)
    @DisplayName("Compute pk0 with choice = 0")
    public void computeWithChoiceZero() {
        var logic = new RGClientLogicECOT();
        var n = Constants.getGroup().getN();

        var pk0 = Constants.getGroup().getG().multiply(RandomBigInteger.generate(n));
        var pk1 = Constants.getGroup().getG().multiply(RandomBigInteger.generate(n));

        var result = logic.computePk1(0, pk0, pk1);

        assertArrayEquals(pk1.getEncoded(true), result.getEncoded(true));
    }

    @RepeatedTest(10)
    @DisplayName("Compute pk0 with choice = 1")
    public void computeWithChoiceOne() {
        var logic = new RGClientLogicECOT();
        var n = Constants.getGroup().getN();

        var pk0 = Constants.getGroup().getG().multiply(RandomBigInteger.generate(n));
        var pk1 = Constants.getGroup().getG().multiply(RandomBigInteger.generate(n));

        var result = logic.computePk1(1, pk0, pk1);

        assertArrayEquals(pk0.getEncoded(true), result.getEncoded(true));
    }
}