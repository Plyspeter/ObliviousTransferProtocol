package chapter10;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;

import javax.security.auth.x500.X500PrivateCredential;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import static chapter10.KeyStoreUtils.createSelfSignedCredentials;
/**
 * Basic example of using JKS to store a single private key and self-signed
 * certificate.
 */
public class JKSExample
{
    public static void main(String[] args)
        throws Exception
    {
        X500PrivateCredential cred = createSelfSignedCredentials();

        KeyStore store = KeyStore.getInstance("JKS");

        store.load(null, null);

        store.setKeyEntry("key", cred.getPrivateKey(), "keyPass".toCharArray(),
            new Certificate[] { cred.getCertificate() });

        FileOutputStream fOut = new FileOutputStream("basic.jks");

        store.store(fOut, "storePass".toCharArray());

        fOut.close();
    }
}
