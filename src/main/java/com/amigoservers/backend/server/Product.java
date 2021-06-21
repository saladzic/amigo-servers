package com.amigoservers.backend.server;

import com.amigoservers.backend.util.mvc.Model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Product extends Model {
    private int id;
    private String name;
    private String nameId;
    private int quantity;
    private BigDecimal price;
    private int combiProductId;

    public Product(int productId) {
        this.id = productId;
    }

    public Product fetch() {
        try {
            PreparedStatement stmt = getDb().prepareStatement("SELECT name,name_id,quantity,price,combi_product_id" +
                    " FROM amigo_product WHERE id=?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            this.name = rs.getString("name");
            this.nameId = rs.getString("name_id");
            this.quantity = rs.getInt("quantity");
            this.price = rs.getBigDecimal("price");
            this.combiProductId = rs.getInt("combi_product_id");
            return this;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameId() {
        return nameId;
    }

    public void setNameId(String nameId) {
        this.nameId = nameId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getCombiProductId() {
        return combiProductId;
    }

    public void setCombiProductId(int combiProductId) {
        this.combiProductId = combiProductId;
    }
}
