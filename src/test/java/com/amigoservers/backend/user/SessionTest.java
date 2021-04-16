package com.amigoservers.backend.user;

import com.amigoservers.backend.util.exception.LoginFailedException;
import com.amigoservers.backend.util.main.Kernel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionTest extends Kernel {
    @Before
    public void before() {
        resetDb();
    }

    @Test
    public void logout() {
        try {
            Session session = new Session()
                    .login("angelo", "test123", "", "");
            Assert.assertTrue(session.isValid());
            session.logout();
            Assert.assertFalse(session.isValid());
        } catch (LoginFailedException exception) {
            exception.printStackTrace();
        }
    }

    @Test(expected = LoginFailedException.class)
    public void loginFailed() throws LoginFailedException {
        new Session()
                .login("angelo", "", "", "");
    }

    @Test
    public void loginSuccess() {
        try {
            new Session()
                    .login("angelo", "test", "", "");
        } catch (LoginFailedException exception) {
            Assert.fail();
        }
    }
}