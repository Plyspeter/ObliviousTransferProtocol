package idgwpi.receiptgenerator.client;

import idgwpi.receiptgenerator.cryptosystem.*;
import idgwpi.globals.Constants;
import idgwpi.globals.Provider;
import idgwpi.receiptgenerator.dtos.CipherText;
import idgwpi.receiptgenerator.helpers.XOR;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

public class RGClientLogicECOT implements IRGClientLogicECOT {
    private ICryptoSystem cryptoSystem;

    @Override
    public void setup(ICryptoSystem cryptoSystem) {
        this.cryptoSystem = cryptoSystem;
        cryptoSystem.setup(Provider.getCryptoSystemLogic(), Provider.getHash());
    }

    /**
     * Get keyGenerator and generate a pair of keys (pkc, skc) ←$ KG(1^k)
     */
    @Override
    public IKeyGenerator getKeyGenerator() {
        return cryptoSystem.getKeyGenerator();
    }

    /**
     * Sample a random binary string s ←$ {0,1}^k
     */
    @Override
    public byte[] sampleRandomBinaryString() {
        var SRandom = new SecureRandom();
        var s = new byte[Constants.getSecurityParameter()/8];
        SRandom.nextBytes(s);
        return s;
    }

    /**
     * Compute q ← Hkey(s)
     */
    @Override
    public ECPoint computeQ(byte[] s) {
        return cryptoSystem.hkey(s);
    }

    /**
     * Compute pk1MinusC such that pk0 * pk1 = q, i.e. pk1MinusC = q dot pkc^-1
     */
    @Override
    public ECPoint computePK1MinusC(ECPoint q, ECPoint pkc) {
        return q.subtract(pkc);
    }

    /**
     * Get CipherText based on choice
     */
    @Override
    public CipherText getCTC(int choice, CipherText ct0, CipherText ct1) {
        return (choice == 0) ? ct0 : ct1;
    }

    /**
     * Compute pc ← Dec(sk, ctc)
     */
    @Override
    public ECPoint computePC(ECPoint pkc, BigInteger sk, CipherText ctc) {
        return cryptoSystem.decrypt(pkc, sk, ctc);
    }

    /**
     * Compute p'c ← Hch(pkc,po)
     */
    @Override
    public byte[] computePCMark(ECPoint pkc, ECPoint pc) {
        return cryptoSystem.hch(pkc, pc);
    }

    /**
     * Compute p''c ← Hch(p'0)
     */
    @Override
    public byte[] computePCMark2(byte[] pc_mark) {
        return cryptoSystem.hch(pc_mark);
    }

    /**
     * Compute chr ← p''c ⊕ (c · ch)
     */
    @Override
    public byte[] computeCHR(int choice, byte[] pc_mark2, byte[] ch) {
        return (choice == 0) ? pc_mark2 : XOR.compute(pc_mark2, ch);
    }

    /**
     * Check p'c from Alice equals p'c computed locally
     */
    @Override
    public boolean verifyPCMark(int choice, byte[] pc_mark, byte[] p0_mark, byte[] p1_mark) {
        return Arrays.equals(pc_mark, (choice == 0) ? p0_mark : p1_mark);
    }

    @Override
    public void abort(String s) {
        throw new RuntimeException(s);
    }

    /**
     * Compute p''1−c ← HCh(p'1−c)
     */
    @Override
    public byte[] computeP1MinusCMark2(int choice, byte[] p0_mark, byte[] p1_mark) {
        return cryptoSystem.hch((choice == 1) ? p0_mark : p1_mark);
    }

    /**
     * Check ch = p'' ⊕ p''1
     */
    @Override
    public boolean verifyCH(byte[] ch, byte[] p1MinusC_mark2, byte[] pc_mark2) {
        return Arrays.equals(ch, XOR.compute(p1MinusC_mark2, pc_mark2));
    }

    /**
     * Compute p~c ← HPad(pkc, pc)
     */
    @Override
    public byte[] computePCWave(ECPoint pkc, ECPoint pc) {
        return cryptoSystem.hpad(pkc, pc);
    }

    /**
     * Obtain mc ← m~c ⊕ p~c
     */
    @Override
    public byte[] obtainMessageC(int choice, byte[] pc_wave, byte[] m0_wave, byte[] m1_wave) {
        return (choice == 0) ? XOR.computeInput(pc_wave, m0_wave) : XOR.computeInput(pc_wave, m1_wave);
    }

    // ---------- Without Selective Failure methods ----------------

    /**
     * Sample a random bit c' <- {0, 1}
     */
    @Override
    public int sampleRandomBit() {
        var SRandom = new SecureRandom();
        return SRandom.nextInt(2);
    }

    /**
     * Compute pk_0 from choice.
     */
    @Override
    public ECPoint computePk0(int choice, ECPoint pkc, ECPoint pk1MinusC) {
        return (choice == 0) ? pkc : pk1MinusC;
    }

    /**
     * Compute pk_1 from choice.
     */
    @Override
    public ECPoint computePk1(int choice, ECPoint pkc, ECPoint pk1MinusC) {
        return (choice == 1) ? pkc : pk1MinusC;
    }

    /**
     * Verify that the CipherText ct is equal to the encrypted point p.
     * ct <- ENC(pk, p)
     */
    @Override
    public boolean verifyCT(CipherText ct, ECPoint p, ECPoint pk) {
        var enc = cryptoSystem.encrypt(pk, p);
        return ct.equals(enc);
    }

    /**
     * Calculate d <- c ⊕ c'
     */
    @Override
    public int calculateD(int c, int c_mark) {
        if ((c < 0 || c > 1) || (c_mark < 0 || c_mark > 1))
            throw new RuntimeException("Invalid input for calculation of d!");

        return c ^ c_mark;
    }
}
