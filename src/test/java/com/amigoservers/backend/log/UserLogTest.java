package com.amigoservers.backend.log;

import com.amigoservers.backend.user.User;
import com.amigoservers.backend.util.main.Kernel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class UserLogTest extends Kernel {
    User user = new User().setId(1);

    @Before
    public void before() {
        resetDb();
    }

    @Test
    public void successTest() {
        UserLog userLog = new UserLog();
        userLog.success(user, "test_heading", "test_message");
        List<UserLog> logs = userLog.getLogs(user);
        Assert.assertEquals("success", logs.get(0).getType());
        Assert.assertEquals("test_heading", logs.get(0).getHeading());
        Assert.assertEquals("test_message", logs.get(0).getMessage());
    }

    @Test
    public void warnTest() {
        UserLog userLog = new UserLog();
        userLog.warn(user, "test_heading", "test_message");
        List<UserLog> logs = userLog.getLogs(user);
        Assert.assertEquals("warn", logs.get(0).getType());
        Assert.assertEquals("test_heading", logs.get(0).getHeading());
        Assert.assertEquals("test_message", logs.get(0).getMessage());
    }

    @Test
    public void errorTest() {
        UserLog userLog = new UserLog();
        userLog.error(user, "test_heading", "test_message");
        List<UserLog> logs = userLog.getLogs(user);
        Assert.assertEquals("error", logs.get(0).getType());
        Assert.assertEquals("test_heading", logs.get(0).getHeading());
        Assert.assertEquals("test_message", logs.get(0).getMessage());
    }
}
