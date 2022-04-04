package idgwpi.votingsimulator.dtos;

import java.io.Serializable;

public class AcceptReceipt implements Serializable {
    private boolean receiptAccepted;

    public AcceptReceipt(boolean receiptAccepted){
        this.receiptAccepted = receiptAccepted;
    }

    public boolean isAccepted() {
        return receiptAccepted;
    }
}
