package idgwpi.receiptgenerator.helpers;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * This class contains a method where you are able to generate random
 * big integers, within a range, since it is not a part of the
 * standard java library.
 *
 * The class generates a random value based on the number of bits in the
 * max bound given.
 *
 * An example of this could be, if n = 5, then the bit length is 3.
 * Therefore we generate a new number with a bit length of 3, however
 * since 6 and 7 also have a bit length of 3, we run until be find a
 * number that has both the same bit length and is lower then the bound.
 */
public class RandomBigInteger {

    /**
     * Generate a big integer between 0 (inclusive) and n (inclusive)
     *
     * @param n That maximum bound for the generated number.
     * @return A big integer between 0 and n.
     */
    public static BigInteger generate(BigInteger n) {
        // Fail if n is negative
        if (n.compareTo(BigInteger.ZERO) < 0)
            throw new RuntimeException("Can't generate a random number with a negative n value");

        var random = new SecureRandom();

        // Generate a new random integer with the same number of bits as n.
        var result = new BigInteger(n.bitLength(), random);

        // If the generated number is larger then the maximum bound,
        // compute the result, until a value is found.
        while(result.compareTo(n) > 0)
            result = new BigInteger(n.bitLength(), random);

        return result;
    }

}
