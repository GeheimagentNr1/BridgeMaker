package de.geheimagentnr1.bridge_maker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BridgeMakerTest {

    @Test
    void modIdIsValid() {

        String modId = "bridge_maker";
        assertTrue( modId.matches( "[a-z][a-z0-9_]{1,63}" ) );
    }
}
