package com.amigoservers.backend.cronjob;

import com.amigoservers.backend.payment.PayPal;
import com.amigoservers.backend.util.driver.Db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Payment extends Thread {
    @Override
    public void run() {
        while (true) {
            Db db = new Db();
            Connection connection = db.getDb();

            // PayPal Capture
            try {
                PreparedStatement stmt = connection.prepareStatement("SELECT paypal_order_id,transaction_type,user_id,total " +
                        "FROM amigo_transaction WHERE paid=0 AND created_at>UNIX_TIMESTAMP()-3600*24*30");
                ResultSet res = stmt.executeQuery();
                while (res.next()) {
                    String orderId = res.getString("paypal_order_id");
                    String type = res.getString("transaction_type");
                    int userId = res.getInt("user_id");
                    BigDecimal total = res.getBigDecimal("total");
                    PayPal paypal = new PayPal();
                    boolean paid = paypal.capture(orderId);
                    if (paid) {
                        stmt = connection.prepareStatement("UPDATE amigo_transaction SET paid=1 WHERE paypal_order_id=?");
                        stmt.setString(1, orderId);
                        stmt.executeUpdate();

                        if (type.equals("cashin")) {
                            stmt = connection.prepareStatement("SELECT balance FROM amigo_user WHERE id=?");
                            res = stmt.executeQuery();
                            if (res.next()) {
                                BigDecimal balance = res.getBigDecimal("balance");
                                balance = balance.add(total);
                                stmt = connection.prepareStatement("UPDATE amigo_user SET balance=? WHERE id=?");
                                stmt.setBigDecimal(1, balance);
                                stmt.setInt(2, userId);
                            }
                        }
                    }
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            try {
                Thread.sleep(1000*30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
