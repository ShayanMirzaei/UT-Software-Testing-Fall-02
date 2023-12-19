package domain;

import org.junit.Before;
import org.junit.Test;


import static junit.framework.TestCase.*;

public class EngineTest {

    private Engine engine;

    @Before
    public void setUp() {
        engine = new Engine();
    }
    @Test
    public void TestOrderHistoryHasOrder() {
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(1, 1, 1, 1));
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(1, 1, 2, 2));
        assertEquals(1, engine.getAverageOrderQuantityByCustomer(1));
    }

    @Test
    public void TestGetAverageQuantityByCustomerWithNoOrders() {
        assertEquals(0, engine.getAverageOrderQuantityByCustomer(1));
    }

    @Test
    public void TestGetQuantityPatternByPriceNoOrders() {
        assertEquals(0, engine.getQuantityPatternByPrice(1));
    }

    @Test
    public void TestGetCustomerFraudulentQuantityReturnFraudulentQuantity() {
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(1, 1, 1, 1));
        var fraudulentQuantity = engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(2, 1, 1, 2));
        assertEquals(1, fraudulentQuantity);
    }

    @Test
    public void TestAddOrderForDifferentCustomersThrowsException() {
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(1, 1, 1, 1));
        try {
            engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(2, 2, 2, 2));
        }
        catch (Exception e) {
            assertEquals(ArithmeticException.class, e.getClass());
        }

    }

    @Test
    public void TestGetQuantityPatternByPricePaths() {
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(1, 1, 1, 100));
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(2, 1, 2, 9));
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(3, 1, 3, 8));
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(4, 1, 3, 6));
        var result = engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(5, 1, 3, 4));
        assertEquals(0, result);
    }

    @Test
    public void TestGetQuantityPatternByPricePaths2() {
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(1, 1, 1, 100));
        engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(3, 1, 3, 8));
        var result = engine.addOrderAndGetFraudulentQuantity(Helper.CreateOrder(5, 1, 3, 5));
        assertEquals(-92, result);
    }
}