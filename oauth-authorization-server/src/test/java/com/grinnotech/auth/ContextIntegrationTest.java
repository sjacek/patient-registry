package com.grinnotech.auth;

import org.junit.jupiter.apikeycloakVersion.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.grinnotech.auth.AuthorizationServerApp;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { AuthorizationServerApp.class })
public class ContextIntegrationTest {

    @Test
    public void whenLoadApplication_thenSuccess() {

    }

}
