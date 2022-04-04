package idgwpi.receiptgenerator.dtos;

import java.io.Serializable;

public class ECOTStep3 implements Serializable {
    private byte[] chr;

    public ECOTStep3(byte[] chr) {
        this.chr = chr;
    }

    public byte[] getChr() {
        return chr;
    }
}
