package chapter13;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Date;

import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPDigestCalculatorProviderBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyEncryptorBuilder;

public class BasicKeyRingExample
{
    public static byte[][] generateKeyRing(String identity, char[] passphrase)
        throws GeneralSecurityException, PGPException, IOException
    {
//        KeyPair dsaKp = Dsa.generateKeyPair();
//        KeyPair rsaKp = Rsa.generateKeyPair();
//        PGPKeyPair dsaKeyPair = new JcaPGPKeyPair(PGPPublicKey.DSA, dsaKp, new Date());
//        PGPKeyPair rsaKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, rsaKp, new Date());
//        PGPDigestCalculator sha1Calc = new JcaPGPDigestCalculatorProviderBuilder().build()
//            .get(HashAlgorithmTags.SHA1);
//        PGPKeyRingGenerator keyRingGen =
//            new PGPKeyRingGenerator(
//                PGPSignature.POSITIVE_CERTIFICATION, dsaKeyPair, identity, sha1Calc,null,null,
//                new JcaPGPContentSignerBuilder(dsaKeyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA384),
//                new JcePBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256, sha1Calc)
//                    .setProvider("BCFIPS").build(passphrase));
//        keyRingGen.addSubKey(rsaKeyPair);
//        // create an encoding of the secret key ring
//        ByteArrayOutputStream secretOut = new ByteArrayOutputStream();
//        keyRingGen.generateSecretKeyRing().encode(secretOut);
//        secretOut.close();
//        // create an encoding of the public key ring
//        ByteArrayOutputStream publicOut = new ByteArrayOutputStream();
//        keyRingGen.generatePublicKeyRing().encode(publicOut);
//        publicOut.close();
//        return new byte [][]{secretOut.toByteArray(), publicOut.toByteArray()};
        return null;
    }
}
