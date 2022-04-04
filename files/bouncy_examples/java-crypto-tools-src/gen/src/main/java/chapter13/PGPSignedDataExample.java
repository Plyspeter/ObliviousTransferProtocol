package chapter13;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPLiteralData;
import org.bouncycastle.openpgp.PGPLiteralDataGenerator;
import org.bouncycastle.openpgp.PGPOnePassSignature;
import org.bouncycastle.openpgp.PGPOnePassSignatureList;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureList;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentVerifierBuilderProvider;

public class PGPSignedDataExample
{
    public static byte[] createSignedObject(
        int signingAlg, PGPPrivateKey signingKey, byte[] data)
        throws PGPException, IOException
    {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        BCPGOutputStream bcOut = new BCPGOutputStream(bOut);
        PGPSignatureGenerator sGen =
            new PGPSignatureGenerator(
                new JcaPGPContentSignerBuilder(signingAlg, PGPUtil.SHA384)
                         .setProvider("BCFIPS"));
        sGen.init(PGPSignature.BINARY_DOCUMENT, signingKey);

        sGen.generateOnePassVersion(false).encode(bcOut);

        PGPLiteralDataGenerator lGen = new PGPLiteralDataGenerator();
        
        OutputStream lOut = lGen.open(bcOut,
                    PGPLiteralData.BINARY,"_CONSOLE", data.length, new Date());
        for (int i = 0; i != data.length; i++)
        {
            lOut.write(data[i]);
            sGen.update(data[i]);
        }
        lGen.close();

        sGen.generate().encode(bcOut);

        return bOut.toByteArray();
    }

    public static boolean verifySignedObject(PGPPublicKey verifyingKey, byte[] pgpSignedData)
        throws PGPException, IOException
    {
        JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(pgpSignedData);
        PGPOnePassSignatureList onePassList = (PGPOnePassSignatureList)pgpFact.nextObject();
        PGPOnePassSignature ops = onePassList.get(0);
        PGPLiteralData literalData = (PGPLiteralData)pgpFact.nextObject();
        InputStream dIn = literalData.getInputStream();
        ops.init(new JcaPGPContentVerifierBuilderProvider().setProvider("BCFIPS"), verifyingKey);
        int ch;
        while ((ch = dIn.read()) >= 0)
        {
            ops.update((byte)ch);
        }
        PGPSignatureList sigList = (PGPSignatureList)pgpFact.nextObject();
        PGPSignature sig = sigList.get(0);
        return ops.verify(sig);
    }
}
