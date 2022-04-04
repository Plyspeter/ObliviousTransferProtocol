package chapter10;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;

import javax.security.auth.x500.X500PrivateCredential;

import org.bouncycastle.crypto.util.PBKDFConfig;
import org.bouncycastle.crypto.util.ScryptConfig;
import org.bouncycastle.jcajce.BCFKSLoadStoreParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import static chapter10.KeyStoreUtils.createSelfSignedCredentials;
/**
 * Example of using BCFKS to store a single private key and self-signed
 * certificate, using scrypt for the key store protection.
 */
public class BCFKSwithScryptExample
{
    public static void main(String[] args)
        throws Exception
    {
        X500PrivateCredential cred = createSelfSignedCredentials();

        // specify scrypt parameters
        PBKDFConfig config = new ScryptConfig.Builder(1024, 8, 1)
                                                        .withSaltLength(20).build();

        KeyStore store = KeyStore.getInstance("BCFKS", "BC");

        // initialize empty store to use scrypt
        store.load(new BCFKSLoadStoreParameter.Builder()
                                        .withStorePBKDFConfig(config)
                                        .build());

        store.setKeyEntry("key", cred.getPrivateKey(), "keyPass".toCharArray(),
                                    new Certificate[] { cred.getCertificate() });

        FileOutputStream fOut = new FileOutputStream("scrypt.fks");

        store.store(fOut, "storePass".toCharArray());

        fOut.close();
    }
}
