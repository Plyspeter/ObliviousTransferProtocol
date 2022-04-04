package idgwpi.votingsimulator.dtos;

import idgwpi.globals.Constants;
import org.bouncycastle.math.ec.ECPoint;

import java.io.Serializable;

public class Ballot  implements Serializable {
    private byte[][] pointChoices;
    private String[] stringChoices;

    public Ballot(ECPoint[] pointChoices, String[] stringChoices){
        this.pointChoices = new byte[pointChoices.length][];
        for(int i = 0; i < pointChoices.length; i++){
            this.pointChoices[i] = pointChoices[i].getEncoded(true);
        }
        this.stringChoices = stringChoices;
    }

    public ECPoint[] getPointChoices() {
        var ecPoints = new ECPoint[pointChoices.length];
        for(int i = 0; i < pointChoices.length; i++){
            ecPoints[i] = Constants.getGroup().getCurve().decodePoint(pointChoices[i]);
        }
        return ecPoints;
    }

    public String[] getStringChoices() {
        return stringChoices;
    }
}
