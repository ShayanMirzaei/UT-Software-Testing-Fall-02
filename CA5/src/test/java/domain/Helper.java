package domain;

import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static Order CreateOrder(int id, int customer, int price, int quantity) {
        var order = new Order();
        order.id = id;
        order.customer = customer;
        order.price = price;
        order.quantity = quantity;
        return order;
    }

    public static ArrayList<Order> CreateOrdersWithQuantity(int customer, List<Integer> quantities) {
        var orders = new ArrayList<Order>();
        for (int i = 0; i < quantities.size(); i++) {
            orders.add(CreateOrder(i, customer, 10, quantities.get(i)));
        }
        return orders;
    }

    public static Order CreateOrderWithId(int id) {
        var order = new Order();
        order.id = id;
        order.customer = 1;
        order.price = 1;
        order.quantity = 1;
        return order;
    }
}
