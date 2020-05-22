package com.grinnotech.auth;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { JwtAuthApp.class })
public class ContextIntegrationTest {

    @Test
    @Ignore
    public void whenLoadApplication_thenSuccess() {

    }

}
