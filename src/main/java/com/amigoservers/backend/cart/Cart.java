package com.amigoservers.backend.cart;

import com.amigoservers.backend.util.main.RandomString;
import com.amigoservers.backend.util.mvc.Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cart extends Model {
    private String id;
    private List<CartItem> items;

    public Cart() {
    }

    public Cart(String cartId) {
        this.id = cartId;
        items = new ArrayList<>();
    }

    public Cart get() {
        try {
            Connection connection = getDb();
            PreparedStatement stmt = connection.prepareStatement("SELECT product_id,quantity" +
                    " FROM amigo_cart_item WHERE cart_id=?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                int quantity = rs.getInt("quantity");
                CartItem item = new CartItem(productId, quantity);
                item.fetch();
                items.add(item);
            }
            return this;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Cart create(String ip) {
        String id = RandomString.random(72);
        try {
            Connection connection = getDb();
            PreparedStatement stmt = connection.prepareStatement("SELECT created_at" +
                    " FROM amigo_cart WHERE ip=? ORDER BY created_at DESC");
            stmt.setString(1, ip);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int cartCreatedAt = rs.getInt("created_at");
                if (cartCreatedAt + 30 > System.currentTimeMillis() / 1000L) {
                    // Prevent creating new cart,
                    // because last created cart from the same IP was before 30 seconds
                    return null;
                }
            }

            stmt = connection.prepareStatement("INSERT INTO amigo_cart " +
                    "(id,ip,created_at) VALUES (?,?,UNIX_TIMESTAMP())");
            stmt.setString(1, id);
            stmt.setString(2, ip);
            stmt.executeUpdate();
            setId(id);
            return this;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
