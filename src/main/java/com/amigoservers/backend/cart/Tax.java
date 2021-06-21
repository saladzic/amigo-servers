package com.amigoservers.backend.cart;

import com.amigoservers.backend.util.main.Config;
import com.amigoservers.backend.util.mvc.Model;

import java.math.BigDecimal;

public class Tax extends Model {
    public BigDecimal getTaxFee() {
        Config config = new Config();
        return BigDecimal.valueOf(config.getTax());
    }
}
