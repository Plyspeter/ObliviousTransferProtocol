package idgwpi.receiptgenerator.server;

import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.cryptosystem.ICryptoSystem;
import org.bouncycastle.math.ec.ECPoint;

public interface IRGServerLogicECOT extends IRGServerLogic {

    void setup(ICryptoSystem cryptoSystem);

    /**
     * Compute q ← Hkey(s)
     */
    ECPoint computeQ(byte[] s);

    /**
     * Compute pk1 such that pk0 * pk1 = q
     */
    ECPoint computePK1(ECPoint q, ECPoint pk0);

    /**
     * Sample p ←$ {0,1}^k
     */
    byte[] sampleP();

    /**
     * Convert p into EC points
     */
    ECPoint pToECPoint(byte[] p);

    /**
     * Compute p' ← Hch(pk, p)
     */
    byte[] computePMark(ECPoint pk, ECPoint pPoint);

    /**
     * Compute p'' ← Hch(p')
     */
    byte[] computePMark2(byte[] p_mark);

    /**
     * Compute ch ← p''0 ⊕ p''1
     */
    byte[] computeCH(byte[] p0_mark2, byte[] p1_mark2);

    /**
     * Compute ct ← Enc(pk, p)
     */
    CipherText computeCipherText(ECPoint pk, ECPoint pPoint);

    /**
     * Verify chr = p''0
     */
    boolean verifyCHR(byte[] chr, byte[] p0_mark2);

    void abort(String s);

    /**
     * computes p~ ← HPad(pk, p)
     */
    byte[] computePWave(ECPoint pk, ECPoint pPoint);

    /**
     * computes m~ ← p~ ⊕ m
     */
    byte[] computeMWave(byte[] p_wave, ECPoint message);

    // ---------- Without Selective Failure methods ----------------

    /**
     * Sample a random binary string ^m ←$ {0,1}^y
     */
    byte[] sampleRandomBinaryString(int length);

    /**
     * compute m~ <- p~ ⊕ m^
     */
    byte[] computeMWave(byte[] p_wave, byte[] m0H);

    /**
     * Computes the mH input that is equal to d
     */
    byte[] computeMdH(int d, byte[] m0H, byte[] m1H);

    /**
     * Computes the mH input that is equal to 1-d
     */
    byte[] computeM1dH(int d, byte[] m0H, byte[] m1H);

    /**
     * computes m~ ← p~ ⊕ m
     */
    byte[] computeMMark(byte[] p_wave, ECPoint message);
}
