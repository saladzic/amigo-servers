package log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import user.User;
import util.main.Kernel;

import java.util.List;

public class SystemLogTest extends Kernel {
    @Before
    public void before() {
        resetDb();
    }

    @Test
    public void successTest() {
        SystemLog systemLog = new SystemLog();
        systemLog.success("test_heading", "test_message");
        List<SystemLog> logs = systemLog.getLogs();
        Assert.assertEquals("success", logs.get(0).getType());
        Assert.assertEquals("test_heading", logs.get(0).getHeading());
        Assert.assertEquals("test_message", logs.get(0).getMessage());
    }

    @Test
    public void warnTest() {
        SystemLog systemLog = new SystemLog();
        systemLog.warn("test_heading", "test_message");
        List<SystemLog> logs = systemLog.getLogs();
        Assert.assertEquals("warn", logs.get(0).getType());
        Assert.assertEquals("test_heading", logs.get(0).getHeading());
        Assert.assertEquals("test_message", logs.get(0).getMessage());
    }

    @Test
    public void errorTest() {
        SystemLog systemLog = new SystemLog();
        systemLog.error("test_heading", "test_message");
        List<SystemLog> logs = systemLog.getLogs();
        Assert.assertEquals("error", logs.get(0).getType());
        Assert.assertEquals("test_heading", logs.get(0).getHeading());
        Assert.assertEquals("test_message", logs.get(0).getMessage());
    }
}
