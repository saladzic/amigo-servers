package com.amigoservers.backend.cart;

import com.amigoservers.backend.server.Product;
import com.amigoservers.backend.util.mvc.Model;

public class CartItem extends Model {
    private Product product;
    private int quantity;
    private int productId;

    public CartItem(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void fetch() {
        Product product = new Product(productId);
        this.product = product.fetch();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
