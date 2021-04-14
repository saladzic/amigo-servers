package com.amigoservers.backend.controller.session;

import com.amigoservers.backend.user.Session;
import com.amigoservers.backend.util.exception.LoginFailedException;
import com.amigoservers.backend.util.exception.ServerException;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionController {
    @RequestMapping(path = "/api/session/login", method = RequestMethod.POST)
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestHeader(value = "User-Agent") String userAgent,
                        @RequestHeader(name = "X-FORWARDED-FOR", defaultValue = "") String ip) {
        try {
            int userId = Session.login(username, password);
            String sessionId = Session.create(userId, userAgent, ip);
            return "{'success': true, 'session': '" + sessionId + "'}";
        } catch (LoginFailedException e) {
            e.printStackTrace();
            return "{'success': false, 'error': 'login_exception'}";
        } catch (ServerException e) {
            e.printStackTrace();
            return "{'success': false, 'error': 'server_exception'}";
        }
    }
}
