package com.amigoservers.backend.controller.cart;

import com.amigoservers.backend.cart.Cart;
import com.amigoservers.backend.cart.CartItem;
import com.amigoservers.backend.cart.Tax;
import com.amigoservers.backend.payment.PayPal;
import com.amigoservers.backend.payment.Transaction;
import com.amigoservers.backend.server.Product;
import com.amigoservers.backend.user.Session;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@RestController
public class CartController {
    @RequestMapping(path = "/api/cart", method = RequestMethod.GET, produces = "application/json")
    public String get(@CookieValue("cart_id") String cartId) {
        Cart cart = new Cart(cartId).fetch();
        if (cart == null) {
            return "{\"success\": false, \"error\": \"cart_doesnt_exist\"}";
        }
        String returnStr = "{\"success\": true, \"items\": [";
        List<CartItem> items = cart.getItems();
        BigDecimal total = new BigDecimal(0);
        Tax tax = new Tax();
        BigDecimal taxFee = tax.getTaxFee().divide(new BigDecimal(100));
        for (int x = 0; x < items.size(); x++) {
            returnStr += "{\"name\": \"" + items.get(x).getProduct().getName() + "\",";
            returnStr += "\"name_id\": \"" + items.get(x).getProduct().getNameId() + "\",";
            returnStr += "\"quantity\": " + items.get(x).getQuantity() + ",";
            returnStr += "\"combiProductId\": " + items.get(x).getProduct().getCombiProductId() + ",";
            BigDecimal price = new BigDecimal(items.get(x).getQuantity());
            price = price.multiply(items.get(x).getProduct().getPrice());
            total = total.add(price);
            returnStr += "\"price\": " + price + "}";
            if (x + 1 < items.size()) {
                returnStr += ",";
            }
        }
        BigDecimal taxTotal = taxFee.multiply(total).round(new MathContext(2));
        BigDecimal subTotal = total.subtract(taxTotal);
        returnStr += "], \"tax_percent\": " + tax.getTaxFee() + ",";
        returnStr += "\"tax\": " + taxTotal + ",";
        returnStr += "\"subTotal\": " + subTotal + ",";
        returnStr += "\"total\": " + total + "}";
        return returnStr;
    }

    @RequestMapping(path = "/api/cart/create", method = RequestMethod.GET, produces = "application/json")
    public String create(@RequestHeader(name = "X-FORWARDED-FOR", defaultValue = "") String ip) {
        Cart cart = new Cart();
        cart.create(ip);
        if (cart.getId() == null) {
            return "{\"success\": false}";
        }
        return "{\"success\": true, \"cart_id\": \"" + cart.getId() + "\"}";
    }

    @RequestMapping(path = "/api/cart/add", method = RequestMethod.POST, produces = "application/json")
    public String add(@CookieValue("cart_id") String cartId, @RequestParam int productId, @RequestParam int quantity) {
        if (quantity <= 0) {
            return "{\"success\": false, \"error\": \"quantity_too_low\"}";
        }
        Product product = new Product(productId).fetch();
        if (product == null) {
            return "{\"success\": false, \"error\": \"product_doesnt_exist\"}";
        }
        int productQuantity = product.getQuantity();
        if (productQuantity < quantity) {
            return "{\"success\": false, \"error\": \"quantity_too_big\", \"quantity\": " + productQuantity + "}";
        }
        CartItem item = new CartItem(productId, quantity);
        Cart cart = new Cart(cartId).fetch();
        if (cart == null) {
            return "{\"success\": false, \"error\": \"cart_doesnt_exist\"}";
        }
        cart.addItem(item);
        return get(cartId);
    }

    @RequestMapping(path = "/api/cart/modify", method = RequestMethod.POST, produces = "application/json")
    public String modify(@CookieValue("cart_id") String cartId, @RequestParam int productId, @RequestParam int quantity) {
        Cart cart = new Cart(cartId).fetch();
        if (cart == null) {
            return "{\"success\": false, \"error\": \"cart_doesnt_exist\"}";
        }
        cart.clearItem(productId);
        return add(cartId, productId, quantity);
    }

    @RequestMapping(path = "/api/cart/remove", method = RequestMethod.DELETE, produces = "application/json")
    public String remove(@CookieValue("cart_id") String cartId, @RequestParam int productId) {
        Cart cart = new Cart(cartId).fetch();
        if (cart == null) {
            return "{\"success\": false, \"error\": \"cart_doesnt_exist\"}";
        }
        cart.clearItem(productId);
        return "{\"success\": true}";
    }

    @RequestMapping(path = "/api/cart/pay", method = RequestMethod.GET, produces = "application/json")
    public String pay(@CookieValue("cart_id") String cartId,
                      @RequestHeader(name = "Session-Token") String sessionId,
                      @RequestParam String paymentMethod) {
        Cart cart = new Cart(cartId).fetch();
        if (cart == null) {
            return "{\"success\": false, \"error\": \"cart_doesnt_exist\"}";
        }
        Session session = new Session(sessionId);
        int userId = session.getUserId();
        if (userId == 0) {
            // No User found
            return "{\"success\": false, \"error\": \"user_not_found\"}";
        }
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setTax(cart.getTax());
        transaction.setSubTotal(cart.getSubTotal());
        transaction.setTotal(cart.getTotal());
        transaction.setPaymentMethod(paymentMethod);
        transaction.setTransactionType("order");

        switch (paymentMethod) {
            case "paypal": {
                PayPal paypal = new PayPal(transaction);
                return "{\"success\": true, \"link\": \"" + paypal.getPaymentUrl() + "\"}";
            }
            case "paysafecard": {

            }
            case "bitcoin": {

            }
            default: {
                return "{\"success\": false, \"error\": \"payment_method_doesnt_exist\"}";
            }
        }
    }

    /* ToDo: Voucher System
    @RequestMapping(path = "/api/cart/voucher", method = RequestMethod.POST, produces = "application/json")
    public String addVoucher(@RequestHeader(name = "Session-Token") String sessionId) {
        return "{\"success\": true, \"session\": \"" + 2 + "\"}";
    }*/

    /* ToDo: Voucher System
    @RequestMapping(path = "/api/cart/voucher", method = RequestMethod.DELETE, produces = "application/json")
    public String removeVoucher(@RequestHeader(name = "Session-Token") String sessionId) {
        return "{\"success\": true, \"session\": \"" + 2 + "\"}";
    }*/
}
