package user;

import org.junit.Assert;
import system.main.Kernel;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

public class UserTest extends Kernel {
    @Before
    public void before() {
        resetDb();
    }

    @Test
    public void get() {
        User user = new User();
        user.setId(1)
            .get();
        Assert.assertEquals("angelo", user.getUsername());
        Assert.assertNull(user.setId(1848534).get());
    }

    @Test
    public void set() {
        User user = new User();
        user.setId(1)
            .get()
            .setBalance(new BigDecimal("1000.00"))
            .set()
            .get();
        Assert.assertEquals(new BigDecimal("1000.00"), user.getBalance());
    }
}