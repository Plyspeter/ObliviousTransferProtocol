package idgwpi.receiptgenerator.client.clientlogictests;

import idgwpi.receiptgenerator.client.RGClientLogicECOT;
import idgwpi.receiptgenerator.cryptosystem.*;
import idgwpi.globals.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class SetupTest {
    private RGClientLogicECOT clientLogic;
    private ICryptoSystem cryptoSystem;

    @BeforeEach
    public void setup(){
        clientLogic = new RGClientLogicECOT();
        cryptoSystem = mock(ICryptoSystem.class);
    }

    @Test
    @DisplayName("Setup correctly sets fields")
    public void CorrectFields(){
        try {
            clientLogic.setup(cryptoSystem);
            var cryptoSystemActual = clientLogic.getClass().getDeclaredField("cryptoSystem");
            cryptoSystemActual.setAccessible(true);
            assertEquals(cryptoSystem, cryptoSystemActual.get(clientLogic));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Setup calls setup method for cryptoSystem")
    public void CallsSetupMethodInCryptoSystem(){
        clientLogic.setup(cryptoSystem);
        verify(cryptoSystem, times(1)).setup(any(CryptoSystemLogic.class), any(Hash.class));
    }
}
