package com.amigoservers.backend.user;

import com.amigoservers.backend.log.SystemLog;
import com.amigoservers.backend.util.driver.Db;
import com.amigoservers.backend.util.mvc.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ticket extends Model {
    private int id;
    private int authorId;
    private String department;
    private String priority;
    private String heading;
    private String msg;
    private boolean active;
    private int createdAt;

    public Ticket() {
    }

    public Ticket(int authorId, String department, String priority, String heading, String msg) {
        this.authorId = authorId;
        this.department = department;
        this.priority = priority;
        this.heading = heading;
        this.msg = msg;
    }

    public void toggleClose(int userId, int ticketId) {
        try {
            Connection conn = getDb();
            PreparedStatement stmt = conn.prepareStatement("SELECT active FROM amigo_ticket WHERE author_id=? AND id=?");
            stmt.setInt(1, userId);
            stmt.setInt(2, ticketId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return;
            }
            boolean active = rs.getBoolean("active");
            if (active) {
                // Close Ticket
                stmt = conn.prepareStatement("UPDATE amigo_ticket SET active=0 WHERE author_id=? AND id=?");
            } else {
                // Reopen Ticket
                stmt = conn.prepareStatement("UPDATE amigo_ticket SET active=1 WHERE author_id=? AND id=?");
            }
            stmt.setInt(1, userId);
            stmt.setInt(2, ticketId);
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public List<Ticket> getByUser(int authorId, int page) {
        try {
            PreparedStatement stmt = getDb().prepareStatement("SELECT id,author_id,department,priority,heading,message,active,created_at" +
                    " FROM amigo_ticket WHERE author_id=? ORDER BY created_at DESC LIMIT ?,15");
            stmt.setInt(1, authorId);
            stmt.setInt(2, page * 15);
            ResultSet rs = stmt.executeQuery();
            List<Ticket> list = new ArrayList<>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setAuthorId(rs.getInt("author_id"));
                ticket.setDepartment(rs.getString("department"));
                ticket.setPriority(rs.getString("priority"));
                ticket.setHeading(rs.getString("heading"));
                ticket.setMsg(rs.getString("message"));
                ticket.setActive(rs.getBoolean("active"));
                ticket.setCreatedAt(rs.getInt("created_at"));
                list.add(ticket);
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Ticket get(int ticketId) {
        try {
            PreparedStatement stmt = getDb().prepareStatement("SELECT id,author_id,department,priority,heading,message,active,created_at" +
                    " FROM amigo_ticket WHERE id=?");
            stmt.setInt(1, ticketId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Ticket ticket = new Ticket();
            ticket.setId(rs.getInt("id"));
            ticket.setAuthorId(rs.getInt("author_id"));
            ticket.setDepartment(rs.getString("department"));
            ticket.setPriority(rs.getString("priority"));
            ticket.setHeading(rs.getString("heading"));
            ticket.setMsg(rs.getString("message"));
            ticket.setActive(rs.getBoolean("active"));
            ticket.setCreatedAt(rs.getInt("created_at"));
            return ticket;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void create() {
        try {
            Connection connection = getDb();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO amigo_ticket " +
                    "(author_id,department,priority,heading,message,created_at) VALUES (?,?,?,?,?,UNIX_TIMESTAMP())");
            stmt.setInt(1, authorId);
            stmt.setString(2, department);
            stmt.setString(3, priority);
            stmt.setString(4, heading);
            stmt.setString(5, msg);
            stmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }
}
