package com.amigoservers.backend.cart;

import com.amigoservers.backend.util.main.RandomString;
import com.amigoservers.backend.util.mvc.Model;

import java.math.BigDecimal;
import java.math.MathContext;
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

    public Cart fetch() {
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

    public void addItem(CartItem item) {
        try {
            items.add(item);
            clearItem(item.getProductId());
            PreparedStatement stmt = getDb().prepareStatement("INSERT INTO amigo_cart_item " +
                    "(cart_id,product_id,quantity) VALUES (?,?,?)");
            stmt.setString(1, id);
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void clearItem(int productId) {
        try {
            PreparedStatement stmt = getDb().prepareStatement("DELETE FROM amigo_cart_item WHERE cart_id=? AND product_id=?");
            stmt.setString(1, id);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public BigDecimal getTax() {
        Cart cart = new Cart(id).fetch();
        if (cart == null) {
            return null;
        }
        List<CartItem> items = cart.getItems();
        BigDecimal total = new BigDecimal(0);
        Tax tax = new Tax();
        BigDecimal taxFee = tax.getTaxFee().divide(new BigDecimal(100));
        for (int x = 0; x < items.size(); x++) {
            BigDecimal price = new BigDecimal(items.get(x).getQuantity());
            price = price.multiply(items.get(x).getProduct().getPrice());
            total = total.add(price);
        }
        return taxFee.multiply(total).round(new MathContext(2));
    }

    public BigDecimal getSubTotal() {
        Cart cart = new Cart(id).fetch();
        if (cart == null) {
            return null;
        }
        List<CartItem> items = cart.getItems();
        BigDecimal total = new BigDecimal(0);
        Tax tax = new Tax();
        BigDecimal taxFee = tax.getTaxFee().divide(new BigDecimal(100));
        for (int x = 0; x < items.size(); x++) {
            BigDecimal price = new BigDecimal(items.get(x).getQuantity());
            price = price.multiply(items.get(x).getProduct().getPrice());
            total = total.add(price);
        }
        BigDecimal taxTotal = taxFee.multiply(total).round(new MathContext(2));
        return total.subtract(taxTotal);
    }

    public BigDecimal getTotal() {
        Cart cart = new Cart(id).fetch();
        if (cart == null) {
            return null;
        }
        List<CartItem> items = cart.getItems();
        BigDecimal total = new BigDecimal(0);
        Tax tax = new Tax();
        BigDecimal taxFee = tax.getTaxFee().divide(new BigDecimal(100));
        for (int x = 0; x < items.size(); x++) {
            BigDecimal price = new BigDecimal(items.get(x).getQuantity());
            price = price.multiply(items.get(x).getProduct().getPrice());
            total = total.add(price);
        }
        return total;
    }
}
