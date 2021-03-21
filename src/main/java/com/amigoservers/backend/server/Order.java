package com.amigoservers.backend.server;

import com.amigoservers.backend.user.User;

import java.util.List;

public class Order {
    private String id;
    private User user;
    private List<OrderItem> items;

    public User getUser() {
        return user;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
    }

    public void removeItem(int index) {
        items.remove(index);
    }
}
