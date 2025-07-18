package de.jcup.kubegen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SystemEnvironmentEntryProviderTest {

    @Test
    void path_variable_from_environment_is_same_as_from_provider() {
       String fromEnv = System.getenv("PATH");
       assertNotNull(fromEnv);
       
       assertEquals(fromEnv, new SystemEnvironmentMapDataProvider().getMap().get("PATH"));
    }

}
