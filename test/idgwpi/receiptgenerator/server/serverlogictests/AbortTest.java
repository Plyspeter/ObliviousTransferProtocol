package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AbortTest {
    @Test
    @DisplayName("Abort throws expected error and error message")
    public void throwsExpectedError(){
        var serverLogic = new RGServerLogicECOT();
        var expected = "Error message";

        var exception = assertThrows(RuntimeException.class, () -> serverLogic.abort(expected));
        assertEquals(expected, exception.getMessage());
    }
}
