package com.amigoservers.backend.controller.user;

import com.amigoservers.backend.user.Session;
import com.amigoservers.backend.user.Ticket;
import com.amigoservers.backend.user.TicketMessage;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TicketController {
    @RequestMapping(path = "/api/tickets", method = RequestMethod.GET, produces = "application/json")
    public String getByUser(@RequestParam(name = "page", defaultValue = "1") int page,
                      @RequestHeader(name = "Session-Token") String sessionId) {
        Session session = new Session(sessionId);
        Ticket ticket = new Ticket();
        int userId = session.getUserId();
        if (userId == 0) {
            // No User found
            return "{\"success\": false, \"error\": \"user_not_found\"}";
        }
        List<Ticket> tickets = ticket.getByUser(userId, page);
        if (tickets == null) {
            return "{\"success\": true, \"error\": \"zero_tickets_found\"}";
        }

        String returnStr = "{\"success\": true, \"tickets\": [";
        for (int x = 0; x < tickets.size(); x++) {
            returnStr += "{\"id\": " + tickets.get(x).getId() + ",";
            returnStr += "\"author_id\": " + tickets.get(x).getAuthorId() + ",";
            returnStr += "\"heading\": \"" + tickets.get(x).getHeading() + "\",";
            returnStr += "\"msg\": \"" + tickets.get(x).getMsg() + "\",";
            returnStr += "\"active\": " + tickets.get(x).isActive() + ",";
            returnStr += "\"created_at\": " + tickets.get(x).getCreatedAt() + "}";
            if (x + 1 < tickets.size()) {
                returnStr += ",";
            }
        }
        returnStr += "]}";
        return returnStr;
    }

    @RequestMapping(path = "/api/ticket/create", method = RequestMethod.POST, produces = "application/json")
    public String create(@RequestHeader(name = "Session-Token") String sessionId,
                         @RequestParam(defaultValue = "General") String department,
                         @RequestParam(defaultValue = "Medium") String priority,
                         @RequestParam String heading,
                         @RequestParam String msg) {
        Session session = new Session(sessionId);
        int userId = session.getUserId();
        if (userId == 0) {
            // No User found
            return "{\"success\": false, \"error\": \"user_not_found\"}";
        }
        new Ticket(userId, department, priority, heading, msg)
                .create();
        return "{\"success\": true}";
    }

    @RequestMapping(path = "/api/ticket", method = RequestMethod.GET, produces = "application/json")
    public String get(@RequestParam(name = "ticketId") int ticketId,
                      @RequestHeader(name = "Session-Token") String sessionId) {
        Session session = new Session(sessionId);
        int userId = session.getUserId();
        if (userId == 0) {
            // No User found
            return "{\"success\": false, \"error\": \"user_not_found\"}";
        }
        Ticket ticket = new Ticket();
        ticket = ticket.get(userId);
        if (ticket == null) {
            return "{\"success\": true, \"error\": \"zero_tickets_found\"}";
        }

        TicketMessage message = new TicketMessage();
        List<TicketMessage> messages = message.getMessages(ticketId);

        String returnStr = "{\"success\": true, \"ticket\": {" +
                "\"id\": " + ticket.getId() + "," +
                "\"author_id\": " + ticket.getAuthorId() + "," +
                "\"heading\": \"" + ticket.getHeading() + "\"," +
                "\"msg\": \"" + ticket.getMsg() + "\"," +
                "\"active\": " + ticket.isActive() + "," +
                "\"created_at\": " + ticket.getCreatedAt() +
                "}, \"messages\": [";
        for (int x = 0; x < messages.size(); x++) {
            returnStr += "{\"id\": " + messages.get(x).getId() + ",";
            returnStr += "\"user_id\": " + messages.get(x).getUserId() + ",";
            returnStr += "\"msg\": \"" + messages.get(x).getMsg() + "\",";
            returnStr += "\"created_at\": " + messages.get(x).getCreatedAt() + "}";
            if (x + 1 < messages.size()) {
                returnStr += ",";
            }
        }
        returnStr += "]}";
        return returnStr;
    }

    @RequestMapping(path = "/api/ticket/status", method = RequestMethod.POST, produces = "application/json")
    public String create(@RequestHeader(name = "Session-Token") String sessionId,
                         @RequestParam(name = "ticketId") int ticketId) {
        Session session = new Session(sessionId);
        int userId = session.getUserId();
        if (userId == 0 && !session.isAdmin()) {
            // No User found
            return "{\"success\": false, \"error\": \"user_not_found\"}";
        }
        new Ticket()
                .toggleClose(userId, ticketId);
        return "{\"success\": true}";
    }

    @RequestMapping(path = "/api/ticket/message", method = RequestMethod.POST, produces = "application/json")
    public String sendMessage(@RequestHeader(name = "Session-Token") String sessionId,
                              @RequestParam(name = "ticketId") int ticketId,
                              @RequestParam(name = "message") String message) {
        Session session = new Session(sessionId);
        int userId = session.getUserId();
        if (userId == 0 && !session.isAdmin()) {
            // No User found
            return "{\"success\": false, \"error\": \"user_not_found\"}";
        }
        boolean sent = new TicketMessage()
                .sendMessage(userId, ticketId, message);
        return "{\"success\": " + sent + "}";
    }
}
