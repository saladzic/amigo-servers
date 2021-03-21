package com.amigoservers.backend.cms;

import com.amigoservers.backend.util.mvc.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TextBlock extends Model {
    public String translate(String lang, String block) {
        try {
            PreparedStatement stmt = getDb().prepareStatement("SELECT text " +
                    "FROM amigo_text_block WHERE lang=? AND name=?");
            ResultSet res = stmt.executeQuery();
            if (res.next()) {
                return res.getString("text");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return block;
    }
}
