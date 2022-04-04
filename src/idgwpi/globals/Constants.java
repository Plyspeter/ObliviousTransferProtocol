package idgwpi.globals;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;

public class Constants {

    private static final X9ECParameters group = SECNamedCurves.getByName("secp256r1");
    private static final Provider securityProvider = new BouncyCastleProvider();
    private static final String hashAlgorithm = "SHA3-256";
    private static final int securityParameter = 256;
    private static final String serverAddress = "localhost";
    private static final int serverPort = 10000;
    private static final String decryptionServiceAddress = "localhost";
    private static final int decryptionServicePort = 12000;
    private static final String[] ballotChoices = new String[] {"yes", "no"};

    public static X9ECParameters getGroup() {
        return group;
    }

    public static Provider getSecurityProvider() {
        return securityProvider;
    }

    public static String getHashAlgorithm() {
        return hashAlgorithm;
    }

    public static String getSecurityProviderString() {
        return securityProvider.getName();
    }

    public static int getSecurityParameter() {
        return securityParameter;
    }

    public static String getServerAddress() {
        return serverAddress;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getDecryptionServiceAddress() {
        return decryptionServiceAddress;
    }

    public static int getDecryptionServicePort() {
        return decryptionServicePort;
    }

    public static String[] getBallotChoices() {
        return ballotChoices;
    }
}
