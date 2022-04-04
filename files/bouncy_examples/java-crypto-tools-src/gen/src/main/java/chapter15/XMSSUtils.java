package chapter15;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import org.bouncycastle.pqc.jcajce.spec.XMSSParameterSpec;

public class XMSSUtils
{
    /**
     * Generate a XMSS key pair with a tree height of 10, based around SHA-512.
     *
     * @return an XMSS KeyPair
     */
    public static KeyPair generateXMSSKeyPair()
        throws GeneralSecurityException
    {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("XMSS", "BCPQC");

        kpg.initialize(new XMSSParameterSpec(10,XMSSParameterSpec.SHA512));

        return kpg.generateKeyPair();
    }
}
