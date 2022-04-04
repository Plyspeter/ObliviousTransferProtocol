package idgwpi.receiptgenerator.server;

import idgwpi.receiptgenerator.cryptosystem.*;
import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.helpers.XOR;
import org.bouncycastle.math.ec.ECPoint;

import java.security.SecureRandom;
import java.util.Arrays;

public class RGServerLogicECOT implements IRGServerLogicECOT {
    private ICryptoSystem cryptoSystem;

    @Override
    public void setup(ICryptoSystem cryptoSystem) {
        this.cryptoSystem = cryptoSystem;
        cryptoSystem.setup(Provider.getCryptoSystemLogic(), Provider.getHash());
    }

    /**
     * Compute q ← Hkey(s)
     */
    @Override
    public ECPoint computeQ(byte[] s) {
        return cryptoSystem.hkey(s);
    }

    /**
     * Compute pk1 such that pk0 * pk1 = q
     */
    @Override
    public ECPoint computePK1(ECPoint q, ECPoint pk0) {
        return q.subtract(pk0);
    }

    /**
     * Sample p ←$ {0,1}^k
     */
    @Override
    public byte[] sampleP() {
        var SRandom = new SecureRandom();
        var p = new byte[Constants.getSecurityParameter()/8];
        SRandom.nextBytes(p);
        return p;
    }

    /**
     * Convert p into EC points
     */
    @Override
    public ECPoint pToECPoint(byte[] p) {
        return cryptoSystem.hkey(p);
    }

    /**
     * Compute p' ← Hch(pk, p)
     */
    @Override
    public byte[] computePMark(ECPoint pk, ECPoint pPoint) {
        return cryptoSystem.hch(pk, pPoint);
    }

    /**
     * Compute p'' ← Hch(p')
     */
    @Override
    public byte[] computePMark2(byte[] p_mark) {
        return cryptoSystem.hch(p_mark);
    }

    /**
     * Compute ch ← p''0 ⊕ p''1
     */
    @Override
    public byte[] computeCH(byte[] p0_mark2, byte[] p1_mark2) {
        return XOR.compute(p0_mark2, p1_mark2);
    }

    /**
     * Compute ct ← Enc(pk, p)
     */
    @Override
    public CipherText computeCipherText(ECPoint pk, ECPoint pPoint) {
        return cryptoSystem.encrypt(pk, pPoint);
    }

    /**
     * Verify chr = p''0
     */
    @Override
    public boolean verifyCHR(byte[] chr, byte[] p0_mark2) {
        return Arrays.equals(chr, p0_mark2);
    }

    @Override
    public void abort(String s) {
        throw new RuntimeException(s);
    }

    /**
     * computes p~ ← HPad(pk, p)
     */
    @Override
    public byte[] computePWave(ECPoint pk, ECPoint pPoint) {
        return cryptoSystem.hpad(pk, pPoint);
    }

    /**
     * computes m~ ← p~ ⊕ m
     */
    @Override
    public byte[] computeMWave(byte[] p_wave, ECPoint message) {
        return XOR.computeInput(p_wave, message.getEncoded(true));
    }

    // ---------- Without Selective Failure methods ----------------

    /**
     * Sample a random binary string ^m ←$ {0,1}^y
     */
    @Override
    public byte[] sampleRandomBinaryString(int length) {
        var SRandom = new SecureRandom();
        var s = new byte[length];
        SRandom.nextBytes(s);
        return s;
    }

    /**
     * compute m~ <- p~ ⊕ m^
     */
    @Override
    public byte[] computeMWave(byte[] p_wave, byte[] m0H) {
        return XOR.computeInput(p_wave, m0H);
    }


    /**
     * Computes the mH input that is equal to d
     */
    @Override
    public byte[] computeMdH(int d, byte[] m0H, byte[] m1H) {
        return (d == 0) ? m0H : m1H;
    }

    /**
     * Computes the mH input that is equal to 1-d
     */
    @Override
    public byte[] computeM1dH(int d, byte[] m0H, byte[] m1H) {
        return (d == 1) ? m0H : m1H;
    }

    /**
     * computes m~ ← p~ ⊕ m
     */
    @Override
    public byte[] computeMMark(byte[] p_wave, ECPoint message) {
        return XOR.computeInput(p_wave, message.getEncoded(true));
    }
}
