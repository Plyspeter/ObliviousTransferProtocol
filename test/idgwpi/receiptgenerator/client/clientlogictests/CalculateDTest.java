package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalculateDTest {

    @Test
    @DisplayName("Calculate D with c = 0 and c' = 0")
    public void calculateWithZeros() {
        var logic = new RGClientLogicECOT();

        var result = logic.calculateD(0, 0);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Calculate D with c = 0 and c' = 1")
    public void calculateWithZeroAndOne() {
        var logic = new RGClientLogicECOT();

        var result = logic.calculateD(0, 1);

        assertEquals(1, result);
    }

    @Test
    @DisplayName("Calculate D with c = 1 and c' = 0")
    public void calculateWithOneAndZero() {
        var logic = new RGClientLogicECOT();

        var result = logic.calculateD(1, 0);

        assertEquals(1, result);
    }

    @Test
    @DisplayName("Calculate D with c = 1 and c' = 1")
    public void calculateWithOnes() {
        var logic = new RGClientLogicECOT();

        var result = logic.calculateD(1, 1);

        assertEquals(0, result);
    }

    @Test
    @DisplayName("Invalid input throw exception")
    public void calculateWithInvalidInput() {
        var logic = new RGClientLogicECOT();

        assertThrows(RuntimeException.class, () -> logic.calculateD(2, 0));
        assertThrows(RuntimeException.class, () -> logic.calculateD(1, -4));
        assertThrows(RuntimeException.class, () -> logic.calculateD(-1, 13));
    }
}
