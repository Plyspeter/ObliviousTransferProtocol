package idgwpi;

import idgwpi.receiptgenerator.cryptosystem.CryptoSystem;
import idgwpi.receiptgenerator.cryptosystem.CryptoSystemLogic;
import idgwpi.receiptgenerator.cryptosystem.Hash;
import idgwpi.receiptgenerator.cryptosystem.KeyGenerator;
import idgwpi.globals.Constants;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class ManualExperimentation {
    private static X9ECParameters g = Constants.getGroup();

    public static void main(String[] args){
        //basicUnderstanding();
        //testCryptoSystem();
        generateByteArrays(new BigInteger[] {
                BigInteger.ONE, BigInteger.TWO, BigInteger.TEN, BigInteger.ZERO, g.getN(), g.getN().add(BigInteger.ONE)
        });
    }

    private static void generateByteArrays(BigInteger[] numbers){
        var hash = new Hash();

        for (BigInteger number : numbers) {
            System.out.println(Arrays.toString(hash.henc(g.getG().multiply(number), g.getN()).toByteArray()));
        }
    }

    private static void basicUnderstanding(){
        var keyGen = new KeyGenerator();
        var cryptoSystemLogic = new CryptoSystemLogic();
        keyGen.generate(g);

        var pk = keyGen.getPk();
        var sk = keyGen.getSk();
        var m = g.getG().multiply(BigInteger.valueOf(1337));

        var SRandom = new SecureRandom();
        var s = new byte[256];
        SRandom.nextBytes(s);

        // Compute HKey(s) -> q
        //var q = (new BigInteger(1, compute(s)));

        var inf = g.getG().multiply(g.getCurve().getOrder());
        var start = g.getG();
        var t1 = start.multiply(BigInteger.valueOf(3248)).multiply(BigInteger.valueOf(5000));
        var t2 = start.multiply(BigInteger.valueOf(5000)).multiply(BigInteger.valueOf(3248));

        System.out.println("Twice = pk Add pk: " + pk.twice().equals(pk.add(pk)));
        System.out.println("Twice sub pk = pk: " + pk.twice().subtract(pk).equals(pk));
        System.out.println("TwicePlus neg pk = pk: " + pk.twicePlus(pk.negate()).equals(pk));
        System.out.println("Infinity point: " + inf.isInfinity());
        System.out.println("pk sub pk = infinity: " + pk.subtract(pk).equals(inf));
        System.out.println("pk add infinity = pk: " + pk.add(inf).equals(pk));
        System.out.println("infinity add pk = pk: " + inf.add(pk).equals(pk));
        System.out.println("infinity add infinity = infinity: " + inf.add(inf).equals(inf));
        System.out.println("Twice infinity = infinity: " + inf.twice().equals(inf));
        System.out.println("pk Mult 3 = pk Add pk Add pk: " + pk.multiply(BigInteger.valueOf(3)).equals(pk.add(pk.add(pk))));
        System.out.println("start mult 3 mult 5 = start mult 3 mult 5: " + t1.equals(t2));
        System.out.println("pk Mult 3 xCoord /= pk Add pk Add pk xCoord: " + !pk.multiply(BigInteger.valueOf(3)).getXCoord().equals(pk.add(pk.add(pk)).getXCoord()));
        System.out.println("pk Mult 3 xCoord normalized = pk Add pk Add pk xCoord normalized: " + pk.multiply(BigInteger.valueOf(3)).normalize().getXCoord().equals(pk.add(pk.add(pk)).normalize().getXCoord()));


        var r = cryptoSystemLogic.computeRandomness(m);
        var g1 = g.getG();
        var g2 = g.getG();

        print("g1", g1);
        print("g2", g2);



        var grsk = g1.multiply(r).multiply(BigInteger.valueOf(5000));
        var gskr = g2.multiply(BigInteger.valueOf(5000)).multiply(r);
        print("grsk", grsk);
        print("gskr", gskr);
        System.out.println(grsk.equals(gskr));
    }

    private static void testCryptoSystem(){
        var keyGen = new KeyGenerator();
        var cryptoSystem = new CryptoSystem();
        keyGen.generate(g);

        var pk = keyGen.getPk();
        var sk = keyGen.getSk();
        var m = g.getG().multiply(BigInteger.valueOf(1337));

        var ct = cryptoSystem.encrypt(pk, m);
        var mDot = cryptoSystem.decrypt(pk, sk, ct);
        System.out.println("Messages are equal: " + m.equals(mDot));
        print("m", m);
        print("mDot", mDot);
    }

    private static void print(String prefix, ECPoint point){
        System.out.println(prefix + ":     (" + point.getXCoord().toBigInteger() + " , " + point.getYCoord().toBigInteger() + ")");
    }

    //Advanced log2 of biginteger https://stackoverflow.com/questions/6827516/logarithm-for-biginteger
    public static final double LOG_2 = Math.log(2.0);
    private static final int MAX_DIGITS_2 = 977; // ~ MAX_DIGITS_10 * LN(10)/LN(2)

    public static double logBigInteger(BigInteger val) {
        if (val.signum() < 1)
            return val.signum() < 0 ? Double.NaN : Double.NEGATIVE_INFINITY;
        int blex = val.bitLength() - MAX_DIGITS_2; // any value in 60..1023 works here
        if (blex > 0)
            val = val.shiftRight(blex);
        double res = Math.log(val.doubleValue());
        return blex > 0 ? res + blex * LOG_2 : res;
    }
}
