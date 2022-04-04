package idgwpi.receiptgenerator.helpers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class XORTest {

    @Test
    @DisplayName("Compute two array will return the exclusive OR of the arrays")
    public void computeArrays() {
        var a = new byte[] {3, 7, 5, 2, 8};
        var b = new byte[] {2, 9, 1, 6, 8};

        var expected = new byte[] {1, 14, 4, 4, 0};
        var result = XOR.compute(a, b);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Compute two arrays always returns the same output")
    public void computeIdenticalOutput() {
        var a = new byte[] {3, 7, 5, 2, 8};
        var b = new byte[] {2, 9, 1, 6, 8};

        var res1 = XOR.compute(a, b);
        var res2 = XOR.compute(a, b);

        assertArrayEquals(res1, res2);
    }

    @Test
    @DisplayName("Compute two arrays returns the same output, regardless of order")
    public void computeArraysWithDifferentOrders() {
        var a = new byte[] {3, 7, 5, 2, 8};
        var b = new byte[] {2, 9, 1, 6, 8};

        var res1 = XOR.compute(a, b);
        var res2 = XOR.compute(b, a);

        assertArrayEquals(res1, res2);
    }

    @Test
    @DisplayName("Compute two identical arrays returns only 0's")
    public void computeEqualArrays() {
        var a = new byte[] {6, 7, 2, 8, 5};

        var expected = new byte[] {0, 0, 0, 0, 0};
        var result = XOR.compute(a, a);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Compute returns the original message")
    public void computeBackToOriginal() {
        var a = new byte[] {3, 7, 5, 2, 8};
        var b = new byte[] {2, 9, 1, 6, 8};

        var c = XOR.compute(a, b);

        assertArrayEquals(a, XOR.compute(b, c));
        assertArrayEquals(b, XOR.compute(a, c));
    }

    @Test
    @DisplayName("Compute of different size arrays will throw exception")
    public void computeDifferentArraySizes() {
        var a = new byte[] {1, 2, 3};
        var b = new byte[] {1, 2};
        var expected = "Tried to compute XOR of two arrays of different sizes";

        var actual1 = assertThrows(RuntimeException.class, () -> XOR.compute(a, b));
        var actual2 = assertThrows(RuntimeException.class, () -> XOR.compute(b, a));

        assertEquals(expected, actual1.getMessage());
        assertEquals(expected, actual2.getMessage());
    }

    @Test
    @DisplayName("Compute message returns the XOR of the two arrays")
    public void computeMessage() {
        var a = new byte[] {3, 7, 5};
        var b = new byte[] {2, 9, 1, 6, 8};

        var expected = new byte[] { 1, 14, 4, 5, 15};
        var result = XOR.computeInput(a, b);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Compute message of same size arrays equals compute XOR")
    public void computeMessageOfEqualArraySize() {
        var a = new byte[] {3, 7, 5, 2, 8};
        var b = new byte[] {2, 9, 1, 6, 8};

        var res1 = XOR.computeInput(a, b);
        var res2 = XOR.compute(a, b);

        assertArrayEquals(res1, res2);
    }

    @Test
    @DisplayName("Compute message where message is shorter then scramble array")
    public void computeMessageOfSmallMessage() {
        var a = new byte[] {3, 7, 5, 2, 8};
        var b = new byte[] {2, 9, 1};

        var expected = new byte[] {1, 14, 4};
        var result = XOR.computeInput(a, b);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Compute long message with over double scramble")
    public void computeMessageWithOverDoubleScramble() {
        var a = new byte[] {3, 7, 5};
        var b = new byte[] {2, 9, 1, 6, 8, 5, 11};

        var expected = new byte[] { 1, 14, 4, 5, 15, 0, 8};
        var result = XOR.computeInput(a, b);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Compute long message with double scramble")
    public void computeMessageWithDoubleScramble() {
        var a = new byte[] {3, 7, 5};
        var b = new byte[] {2, 9, 1, 6, 8, 5};

        var expected = new byte[] { 1, 14, 4, 5, 15, 0};
        var result = XOR.computeInput(a, b);

        assertArrayEquals(expected, result);
    }

    @Test
    @DisplayName("Compute message can return original message")
    public void computeMessageBackToOriginalMessage() {
        var a = new byte[20];
        var b = "Test";

        var random = new Random();
        random.nextBytes(a);

        var result = XOR.computeInput(a, b.getBytes());
        var original = XOR.computeInput(a, result);
        var s = new String(original);

        assertEquals(b, s);
    }
}
