package com.amigoservers.backend.controller.session;

import com.amigoservers.backend.user.Session;
import com.amigoservers.backend.util.exception.LoginFailedException;
import org.springframework.web.bind.annotation.*;

@RestController
public class SessionController {
    @RequestMapping(path = "/api/session/login", method = RequestMethod.POST, produces = "application/json")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestHeader(name = "User-Agent") String userAgent,
                        @RequestHeader(name = "X-FORWARDED-FOR", defaultValue = "") String ip) {
        try {
            Session session = new Session()
                    .login(username, password, userAgent, ip);
            return "{\"success\": true, \"session\": \"" + session.getId() + "\"}";
        } catch (LoginFailedException e) {
            e.printStackTrace();
            return "{\"success\": false, \"error\": \"login_exception\"}";
        }
    }

    @RequestMapping(path = "/api/session/logout", method = RequestMethod.GET, produces = "application/json")
    public String logout(@RequestHeader(name = "Session-Token") String sessionId) {
        new Session(sessionId)
                .logout();
        return "{\"success\": true}";
    }
}
