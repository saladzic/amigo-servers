package com.amigoservers.backend.controller.cart;

import com.amigoservers.backend.cart.Cart;
import com.amigoservers.backend.cart.CartItem;
import com.amigoservers.backend.cart.Tax;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

@RestController
public class CartController {
    @RequestMapping(path = "/api/cart", method = RequestMethod.GET, produces = "application/json")
    public String get(@CookieValue("cart_id") String cartId) {
        Cart cart = new Cart(cartId);
        cart.get();
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
        BigDecimal subTotal = taxFee.multiply(total).round(new MathContext(2));
        returnStr += "], \"tax_percent\": " + tax.getTaxFee() + ",";
        returnStr += "\"tax\": " + subTotal + ",";
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
    public String add(@RequestHeader(name = "Session-Token") String sessionId) {
        return "{\"success\": true, \"session\": \"" + 2 + "\"}";
    }

    @RequestMapping(path = "/api/cart/modify", method = RequestMethod.POST, produces = "application/json")
    public String modify(@RequestHeader(name = "Session-Token") String sessionId) {
        return "{\"success\": true, \"session\": \"" + 2 + "\"}";
    }

    @RequestMapping(path = "/api/cart/remove", method = RequestMethod.DELETE, produces = "application/json")
    public String remove(@RequestHeader(name = "Session-Token") String sessionId) {
        return "{\"success\": true, \"session\": \"" + 2 + "\"}";
    }

    @RequestMapping(path = "/api/cart/voucher", method = RequestMethod.POST, produces = "application/json")
    public String addVoucher(@RequestHeader(name = "Session-Token") String sessionId) {
        return "{\"success\": true, \"session\": \"" + 2 + "\"}";
    }

    @RequestMapping(path = "/api/cart/voucher", method = RequestMethod.DELETE, produces = "application/json")
    public String removeVoucher(@RequestHeader(name = "Session-Token") String sessionId) {
        return "{\"success\": true, \"session\": \"" + 2 + "\"}";
    }
}
