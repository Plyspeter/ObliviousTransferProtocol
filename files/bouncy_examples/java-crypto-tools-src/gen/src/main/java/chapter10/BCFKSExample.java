package chapter10;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500PrivateCredential;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.util.PBKDFConfig;
import org.bouncycastle.crypto.util.ScryptConfig;
import org.bouncycastle.jcajce.BCFKSStoreParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import static chapter10.KeyStoreUtils.createSelfSignedCredentials;
import static chapter6.EcDsaUtils.generateECKeyPair;
import static chapter8.JcaX509Certificate.createTrustAnchor;
/**
 * Basic example of using BCFKS to store a single private key and self-signed
 * certificate.
 */
public class BCFKSExample
{
    public static void main(String[] args)
        throws Exception
    {
        X500PrivateCredential cred = createSelfSignedCredentials();

        KeyStore store = KeyStore.getInstance("BCFKS", "BC");

        store.load(null, null);

        store.setKeyEntry("key", cred.getPrivateKey(), "keyPass".toCharArray(),
            new Certificate[] { cred.getCertificate() });

        FileOutputStream fOut = new FileOutputStream("basic.fks");

        store.store(fOut, "storePass".toCharArray());

        fOut.close();
    }
}
