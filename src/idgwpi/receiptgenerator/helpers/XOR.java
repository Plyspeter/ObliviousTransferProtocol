package idgwpi.receiptgenerator.helpers;

/**
 * This class contains methods to compute XOR in java, since it is not
 * a part of the standard java library.
 *
 * It does so by comparing byte arrays with the java XOR operator.
 * Example: 7 XOR 9 = 0111 XOR 1001 = 1110 = 14
 *
 * Remember that these are specialized methods, and might not work in all cases
 * outside of this project!
 */
public class XOR {

    /**
     * Computes the exclusive OR operation on between each element in the
     * two arrays of equal size.
     *
     * @param a A input byte array of the same size as b
     * @param b A input byte array of the same size as a
     * @return A byte array with the result of the XOR operation.
     */
    public static byte[] compute(byte[] a, byte[] b) {
        if (a.length != b.length)
            throw new RuntimeException("Tried to compute XOR of two arrays of different sizes");

        // Array to store the result of the computation
        var c = new byte[a.length];

        // Compute XOR on each element in the two arrays and save them to result.
        for (int i = 0; i < a.length; i++)
            c[i] = (byte) (a[i] ^ b[i]);

        return c;
    }

    /**
     * Computes the exclusive OR operation between two byte arrays, where
     * one is a scramble that can be reused and the other is a message that
     * should have no length change.
     *
     * @param scramble A byte array of scramble, with no message.
     * @param input A byte array of a message, that needs to stay a fixed size.
     * @return A byte array with the result of the XOR operation.
     */
    public static byte[] computeInput(byte[] scramble, byte[] input) {
        // If the scramble array is long enough to scramble without repetition.
        if (input.length == scramble.length)
            return compute(scramble, input);
        else if (input.length < scramble.length) {
            var c = new byte[input.length];
            System.arraycopy(scramble, 0, c, 0, input.length);
            return compute(c, input);
        }

        // Create a new scramble array of the same length as the message.
        var c = new byte[input.length];

        // Copy over from the scramble array, until the same size as the message array
        for(int i = 0; i <= (input.length - scramble.length); i += scramble.length)
            System.arraycopy(scramble, 0, c, i, scramble.length);

        // Fill the last part of the scramble array, when the reminder is less then the scramble array length.
        var rem = input.length % scramble.length;
        System.arraycopy(scramble, 0, c, c.length - rem, rem);

        // Use the normal compute method, since arrays now have the same size.
        return compute(c, input);
    }
}
