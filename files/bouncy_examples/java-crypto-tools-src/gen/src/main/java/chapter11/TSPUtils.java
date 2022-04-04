package chapter11;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Date;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.SignerInfoGenerator;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampResponseGenerator;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.tsp.TimeStampTokenGenerator;

public class TSPUtils
{
    /**
     * Create a simple TSP request for the passed in SHA-256 hash.
     *
     * @param sha256hash a SHA-256 hash that we want timestamped.
     * @return a DER encoding of the resulting TSP request.
     */
    public static byte[] createTspRequest(byte[] sha256hash)
        throws IOException
    {
        TimeStampRequestGenerator reqGen = new TimeStampRequestGenerator();

        return reqGen.generate(TSPAlgorithms.SHA256, sha256hash).getEncoded();
    }

    /**
     * Create a TSP request for the passed in SHA-256 hash which also
     * includes a nonce and, possibly, a request for certificates.
     *
     * @param sha256hash a SHA-256 hash that we want timestamped.
     * @param nonce a nonce associated with this request.
     * @param requestTsaCert if true, authority should send back a
     *                       copy of its certificate.
     * @return a DER encoding of the resulting TSP request.
     */
    public static byte[] createNoncedTspRequest(
        byte[] sha256hash, BigInteger nonce, boolean requestTsaCert)
        throws IOException
    {
        TimeStampRequestGenerator reqGen = new TimeStampRequestGenerator();

        reqGen.setCertReq(requestTsaCert);

        return reqGen.generate(TSPAlgorithms.SHA256, sha256hash, nonce).getEncoded();
    }

    /**
     * Create a TSP response for the passed in byte encoded TSP request.
     *
     * @param tsaSigningKey TSA signing key.
     * @param tsaSigningCert copy of TSA verification certificate.
     * @param serialNumber response serial number
     * @param tsaPolicy  time stamp
     * @param encRequest byte encoding of the TSP request.
     */
    public static byte[] createTspResponse(
        PrivateKey tsaSigningKey, X509Certificate tsaSigningCert,
        BigInteger serialNumber, ASN1ObjectIdentifier tsaPolicy, byte[] encRequest)
        throws TSPException, OperatorCreationException,
               GeneralSecurityException, IOException
    {
        AlgorithmIdentifier digestAlgorithm =
                            new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
        DigestCalculatorProvider digProvider =
              new JcaDigestCalculatorProviderBuilder().setProvider("BCFIPS").build();

        TimeStampRequest tspRequest = new TimeStampRequest(encRequest);

        SignerInfoGenerator tsaSigner = new JcaSimpleSignerInfoGeneratorBuilder()
                .build("SHA256withRSA", tsaSigningKey, tsaSigningCert);
        TimeStampTokenGenerator tsTokenGen = new TimeStampTokenGenerator(
                            tsaSigner, digProvider.get(digestAlgorithm), tsaPolicy);

        // client has requested a copy of the signing certificate
        if (tspRequest.getCertReq())
        {
            tsTokenGen.addCertificates(
                new JcaCertStore(Collections.singleton(tsaSigningCert)));
        }

        TimeStampResponseGenerator tsRespGen = new TimeStampResponseGenerator(
                                                 tsTokenGen, TSPAlgorithms.ALLOWED);

        return tsRespGen.generate(tspRequest, serialNumber, new Date()).getEncoded();
    }

    /**
     * Verify that the passed in response can be verified by the TSA certificate,
     *
     * @param tsaCertificate the certificate for the TSA that generated the response.
     * @param encResponse a ASN.1 binary encoding of a time-stamp response.
     * @return true if the response can be verified, an exception is thrown if
     *         validation fails.
     */
    public static boolean verifyTspResponse(
        X509Certificate tsaCertificate,
        byte[] encResponse)
        throws IOException, TSPException, OperatorCreationException
    {
        TimeStampResponse tsResp = new TimeStampResponse(encResponse);
        TimeStampToken tsToken = tsResp.getTimeStampToken();

        // validate will throw an exception if there is an issue
        tsToken.validate(new JcaSimpleSignerInfoVerifierBuilder()
                                .setProvider("BCFIPS")
                                .build(tsaCertificate));

        return true;
    }
}
