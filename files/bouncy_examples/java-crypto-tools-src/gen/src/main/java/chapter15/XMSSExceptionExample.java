package chapter15;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;

import org.bouncycastle.pqc.jcajce.interfaces.StateAwareSignature;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;
import org.bouncycastle.util.Strings;

import static chapter15.XMSSUtils.generateXMSSKeyPair;

/**
 * Example of use and effect of StateAwareSignature.getUpdatedPrivateKey()
 * with XMSS.
 */
public class XMSSExceptionExample
{
    public static void main(String[] args)
        throws GeneralSecurityException
    {
        byte[] msg = Strings.toByteArray("hello, world!");

        KeyPair kp = generateXMSSKeyPair();

        Signature sig = Signature.getInstance("XMSS-SHA512", "BCPQC");

        StateAwareSignature xmssSig = (StateAwareSignature)sig;

        xmssSig.initSign(kp.getPrivate());

        xmssSig.update(msg, 0, msg.length);

        byte[] s1 = sig.sign();

        //This marks xmssSig as no longer valid.
        PrivateKey xmssKey = xmssSig.getUpdatedPrivateKey();

        //Uncomment this line back in to make the signature object usable
        //xmssSig.initSign(xmssKey);

        System.err.println("xmssSig ready: " + xmssSig.isSigningCapable());
        
        xmssSig.update(msg, 0, msg.length);

        //this line will only work if initSign() has been called.
        byte[] s2 = sig.sign();
    }
}
