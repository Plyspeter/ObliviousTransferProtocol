package idgwpi.receiptgenerator.server.serverlogictests;

import idgwpi.receiptgenerator.cryptosystem.*;
import idgwpi.receiptgenerator.server.RGServerLogicECOT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class SetupTest {

    private RGServerLogicECOT serverLogic;
    private ICryptoSystem cryptoSystem;

    @BeforeEach
    public void setup(){
        serverLogic = new RGServerLogicECOT();
        cryptoSystem = mock(ICryptoSystem.class);
    }

    @Test
    @DisplayName("Setup correctly sets fields")
    public void CorrectFields(){
        try {
            serverLogic.setup(cryptoSystem);
            var cryptoSystemActual = serverLogic.getClass().getDeclaredField("cryptoSystem");
            cryptoSystemActual.setAccessible(true);
            assertEquals(cryptoSystem, cryptoSystemActual.get(serverLogic));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Setup calls setup method for cryptoSystem")
    public void CallsSetupMethodInCryptoSystem(){
        serverLogic.setup(cryptoSystem);
        verify(cryptoSystem, times(1)).setup(any(CryptoSystemLogic.class), any(Hash.class));
    }
}
