package com.amigoservers.backend.user;

import com.amigoservers.backend.util.driver.Db;
import com.amigoservers.backend.util.mvc.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketMessage extends Model {
    private int id;
    private int userId;
    private String msg;
    private int createdAt;

    public TicketMessage() {
    }

    public TicketMessage(int id, int userId, String message, int created_at) {
        this.id = id;
        this.userId = userId;
        this.msg = message;
        this.createdAt = created_at;
    }

    public List<TicketMessage> getMessages(int ticketId) {
        try {
            PreparedStatement stmt = getDb().prepareStatement("SELECT id,ticket_id,user_id,msg,created_at" +
                    " FROM amigo_ticket_msg WHERE ticket_id=? ORDER BY created_at ASC");
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            List<TicketMessage> list = new ArrayList<>();
            while (rs.next()) {
                TicketMessage ticketMessage = new TicketMessage(rs.getInt("id"), rs.getInt("user_id"),
                        rs.getString("msg"), rs.getInt("created_at"));
                list.add(ticketMessage);
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean sendMessage(int userId, int ticketId, String message) {
        try {
            Connection connection = getDb();
            PreparedStatement stmt = connection.prepareStatement("SELECT created_at" +
                    " FROM amigo_ticket_msg WHERE ticket_id=? ORDER BY created_at DESC");
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int msgCreatedAt = rs.getInt("created_at");
                if (msgCreatedAt + 60 > System.currentTimeMillis() / 1000L) {
                    // Prevent sending Message, because last message was before 60 seconds
                    return false;
                }
            }

            stmt = connection.prepareStatement("INSERT INTO amigo_ticket_msg " +
                    "(user_id,ticket_id,msg,created_at) VALUES (?,?,?,UNIX_TIMESTAMP())");
            stmt.setInt(1, userId);
            stmt.setInt(2, ticketId);
            stmt.setString(3, message);
            stmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }
}
