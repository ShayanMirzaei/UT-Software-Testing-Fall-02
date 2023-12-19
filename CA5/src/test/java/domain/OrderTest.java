package domain;

import org.junit.Test;


import static junit.framework.TestCase.*;

public class OrderTest {

    @Test
    public void GivenOrdersWithSameId_ShouldReturnTrue_WhenCheckingEquality() {
        var order1 = Helper.CreateOrderWithId(1);
        var order2 = Helper.CreateOrderWithId(1);


        assertEquals(order1, order2);
    }

    @Test
    public void GivenOrdersWithDifferentIds_ShouldReturnFalse_WhenCheckingEquality() {
        var order1 = Helper.CreateOrderWithId(1);
        var order2 = Helper.CreateOrderWithId(2);


        assertFalse(order1.equals(order2));
    }

    @Test
    public void TestEqualityWithAnotherObject() {
        var order1 = Helper.CreateOrderWithId(1);
        assertFalse(order1.equals(5));
    }

    @Test
    public void TestSettersAndGetters() {
        var order = new Order();
        order.setId(1);
        order.setCustomer(1);
        order.setPrice(1);
        order.setQuantity(1);
        assertEquals(1, order.getId());
        assertEquals(1, order.getCustomer());
        assertEquals(1, order.getPrice());
        assertEquals(1, order.getQuantity());
    }
}
