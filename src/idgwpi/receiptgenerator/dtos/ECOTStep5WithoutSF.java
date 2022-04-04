package idgwpi.receiptgenerator.dtos;

import java.io.Serializable;

public class ECOTStep5WithoutSF implements Serializable {
    private final int d;

    public ECOTStep5WithoutSF(int d) {
        this.d = d;
    }

    public int getD() {
        return d;
    }
}
