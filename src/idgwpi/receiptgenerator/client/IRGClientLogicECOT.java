package idgwpi.receiptgenerator.client;

import idgwpi.receiptgenerator.cryptosystem.*;
import idgwpi.receiptgenerator.dtos.CipherText;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

public interface IRGClientLogicECOT extends IRGClientLogic{

    void setup(ICryptoSystem cryptoSystem);

    /**
     * Get keyGenerator and generate a pair of keys (pkc, skc) ←$ KG(1^k)
     */
    IKeyGenerator getKeyGenerator();

    /**
     * Sample a random binary string s ←$ {0,1}^k
     */
    byte[] sampleRandomBinaryString();

    /**
     * Compute q ← Hkey(s)
     */
    ECPoint computeQ(byte[] s);

    /**
     * Compute pk1MinusC such that pk0 * pk1 = q, i.e. pk1MinusC = q dot pkc^-1
     */
    ECPoint computePK1MinusC(ECPoint q, ECPoint pkc);

    /**
     * Get CipherText based on choice
     */
    CipherText getCTC(int choice, CipherText ct0, CipherText ct1);

    /**
     * Compute pc ← Dec(sk, ctc)
     */
    ECPoint computePC(ECPoint pkc, BigInteger sk, CipherText ctc);

    /**
     * Compute p'c ← Hch(pkc,po)
     */
    byte[] computePCMark(ECPoint pkc, ECPoint pc);

    /**
     * Compute p''c ← Hch(p'0)
     */
    byte[] computePCMark2(byte[] pc_mark);

    /**
     * Compute chr ← p''c ⊕ (c · ch)
     */
    byte[] computeCHR(int choice, byte[] pc_mark2, byte[] ch);

    /**
     * Check p'c from Alice equals p'c computed locally
     */
    boolean verifyPCMark(int choice, byte[] pc_mark, byte[] p0_mark, byte[] p1_mark);

    void abort(String s);

    /**
     * Compute p''1−c ← HCh(p'1−c)
     */
    byte[] computeP1MinusCMark2(int choice, byte[] p0_mark, byte[] p1_mark);

    /**
     * Check ch = p'' ⊕ p''1
     */
    boolean verifyCH(byte[] ch, byte[] p1MinusC_mark2, byte[] pc_mark2);

    /**
     * Compute p~c ← HPad(pkc, pc)
     */
    byte[] computePCWave(ECPoint pkc, ECPoint pc);

    /**
     * Obtain mc ← m~c ⊕ p~c
     */
    byte[] obtainMessageC(int choice, byte[] pc_wave, byte[] m0_wave, byte[] m1_wave);

    // ---------- Without Selective Failure methods ----------------

    /**
     * Sample a random bit c' <- {0, 1}
     */
    int sampleRandomBit();

    /**
     * Compute pk_0 from choice.
     */
    ECPoint computePk0(int choice, ECPoint pkc, ECPoint pk1MinusC);

    /**
     * Compute pk_1 from choice.
     */
    ECPoint computePk1(int choice, ECPoint pkc, ECPoint pk1MinusC);

    /**
     * Verify that the CipherText ct is equal to the encrypted point p.
     * ct <- ENC(pk, p)
     */
    boolean verifyCT(CipherText ct, ECPoint p, ECPoint pk);

    /**
     * Calculate d <- c ⊕ c'
     */
    int calculateD(int c, int c_mark);


}
