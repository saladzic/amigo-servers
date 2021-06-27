package com.amigoservers.backend.payment;

import com.amigoservers.backend.util.mvc.Model;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transaction extends Model {
    private int id;
    private int userId;
    private BigDecimal tax;
    private BigDecimal subTotal;
    private BigDecimal total;
    private String paypalOrderId;
    private String paymentMethod;
    private String transactionType;

    public void insert() {
        try {
            PreparedStatement stmt = getDb().prepareStatement("INSERT INTO amigo_transaction " +
                    "(user_id,tax,sub_total,total,paypal_order_id,payment_method,transaction_type,created_at) " +
                    "VALUES (?,?,?,?,?,?,?,UNIX_TIMESTAMP())");
            stmt.setInt(1, userId);
            stmt.setBigDecimal(2, tax);
            stmt.setBigDecimal(3, subTotal);
            stmt.setBigDecimal(4, total);
            stmt.setString(5, paypalOrderId);
            stmt.setString(6, paymentMethod);
            stmt.setString(7, transactionType);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getPaypalOrderId() {
        return paypalOrderId;
    }

    public void setPaypalOrderId(String paypalOrderId) {
        this.paypalOrderId = paypalOrderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
