package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbortTest {
    @Test
    @DisplayName("Abort throws expected error and error message")
    public void throwsExpectedError() {
        var clientLogic = new RGClientLogicECOT();
        var expected = "Error message";

        var exception = assertThrows(RuntimeException.class, () -> clientLogic.abort(expected));
        assertEquals(expected, exception.getMessage());
    }
}
