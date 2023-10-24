package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import exceptions.InvalidQuantity;
import helper.TestHelper;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void GivenAnonymousUser_ShouldAddCredit_WhenPositiveAmountIsAddedToCredit() throws InvalidCreditRange {
        var user = TestHelper.createAnonymousUser();
        var oldCredit = user.getCredit();

        user.addCredit(10);
        assertEquals(oldCredit + 10, user.getCredit());
    }

    @Test
    void GivenAnonymousUser_ShouldThrowInvalidCreditRange_WhenNegativeAmountIsAddedToCredit() {
        var user = TestHelper.createAnonymousUser();

        assertThrows(InvalidCreditRange.class,  () -> user.addCredit(-20));
    }

    @Test
    void GivenUserWithCredit_ShouldWithdrawCorrectly_WhenWithdrawAmountIsOkay() throws InsufficientCredit {
        var user = TestHelper.createUserWithCredit(100);

        var oldCredit = user.getCredit();
        user.withdrawCredit(20);

        assertEquals( oldCredit - 20, user.getCredit());
    }

    @Test
    void GivenUserWithLowCredit_ShouldThrowInsufficientCredit_WhenWithdrawAmountIsHigh() {
        var user = TestHelper.createUserWithCredit(10);

        assertThrows(InsufficientCredit.class,  () -> user.withdrawCredit(20));
    }

    @Test
    void GivenAnonymousUser_ShouldHaveItemQuantityOfOne_WhenBuyItemIsAdded() {
        var user = TestHelper.createAnonymousUser();
        var commodity = TestHelper.createAnonymousCommodity();

        user.addBuyItem(commodity);

        assertEquals( 1, user.getBuyList().get(commodity.getId()));
    }

    @Test
    void GivenUserWithCommodityInBuyList_ShouldIncreaseQuantity_WhenBuyItemIsAdded() {
        var commodity = TestHelper.createAnonymousCommodity();
        var user = TestHelper.createUserWithItemInBuyList(commodity.getId(), 5);

        var oldQuantity = user.getBuyList().get(commodity.getId());
        user.addBuyItem(commodity);

        assertEquals(oldQuantity + 1, user.getBuyList().get(commodity.getId()));
    }

    @Test
    void GivenAnonymousUser_ShouldHaveCorrectQuantity_WhenPurchasedItemIsAdded() throws InvalidQuantity {
        var user = TestHelper.createAnonymousUser();
        var commodity = TestHelper.createAnonymousCommodity();

        user.addPurchasedItem(commodity.getId(), 5);

        assertEquals(5, user.getPurchasedList().get(commodity.getId()));
    }

    @Test
    void GivenUserWithPurchasedItem_ShouldAddCorrectQuantity_WhenPurchasedItemIsAdded() throws InvalidQuantity {
        var commodity = TestHelper.createAnonymousCommodity();
        var user = TestHelper.createUserWithItemInPurchasedList(commodity.getId(), 5);

        var oldQuantity = user.getPurchasedList().get(commodity.getId());
        user.addPurchasedItem(commodity.getId(), 10);

        assertEquals(oldQuantity + 10, user.getPurchasedList().get(commodity.getId()));
    }

    @Test
    void GivenUser_ShouldThrowInvalidQuantity_WhenAddedPurchasedItemQuantityIsNegative() {
        var commodity = TestHelper.createAnonymousCommodity();
        var user = TestHelper.createAnonymousUser();

        assertThrows(InvalidQuantity.class,  () ->   user.addPurchasedItem(commodity.getId(), -10));
    }

    @Test
    void GivenUserWithBuyListItemWithQuantityOne_ShouldNotHaveItemInBuyList_WhenItemIsRemoved() throws CommodityIsNotInBuyList {
        var commodity = TestHelper.createAnonymousCommodity();
        var user = TestHelper.createUserWithItemInBuyList(commodity.getId(), 1);

        user.removeItemFromBuyList(commodity);

        assertNull(user.getBuyList().get(commodity.getId()));
    }

    @Test
    void GivenUserWithBuyListItemWithQuantityMoreThanOne_ShouldDecreaseQuantity_WhenItemIsRemoved() throws CommodityIsNotInBuyList {
        var commodity = TestHelper.createAnonymousCommodity();
        var user = TestHelper.createUserWithItemInBuyList(commodity.getId(), 10);

        var oldQuantity = user.getBuyList().get(commodity.getId());
        user.removeItemFromBuyList(commodity);

        assertEquals(oldQuantity - 1, user.getBuyList().get(commodity.getId()));
    }

    @Test
    void GivenUserWithEmptyBuyList_ShouldThrowCommodityNotInBuyList_WhenItemIsRemoved() {
        var commodity = TestHelper.createAnonymousCommodity();
        var user = TestHelper.createAnonymousUser();

        assertThrows(CommodityIsNotInBuyList.class,  () ->   user.removeItemFromBuyList(commodity));
    }
}
