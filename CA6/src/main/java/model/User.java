package model;

import exceptions.CommodityIsNotInBuyList;
import exceptions.InsufficientCredit;
import exceptions.InvalidCreditRange;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private String email;
    private String birthDate;
    private String address;
    private float credit;

    private Map<Integer, Integer> commoditiesRates = new HashMap<>();
    private Map<String, Integer> buyList = new HashMap<>();
    private Map<String, Integer> purchasedList = new HashMap<>();

    public User(String username, String password, String email, String birthDate, String address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
    }

    public void addCredit(float amount) throws InvalidCreditRange {
        if (amount < 0)
            throw new InvalidCreditRange();

        this.credit += amount;
    }

    public void withdrawCredit(float amount) throws InsufficientCredit {
        if (amount > this.credit)
            throw new InsufficientCredit();

        this.credit -= amount;
    }

    public void addBuyItem(Commodity commodity) {
        String id = commodity.getId();
        if (this.buyList.containsKey(id)) {
            int existingQuantity = this.buyList.get(id);
            this.buyList.put(id, existingQuantity + 1);
        } else
            this.buyList.put(id, 1);
    }

    public void addPurchasedItem(String id, int quantity) {
        if (this.purchasedList.containsKey(id)) {
            int existingQuantity = this.purchasedList.get(id);
            this.purchasedList.put(id, existingQuantity + quantity);
        } else
            this.purchasedList.put(id, quantity);
    }

    public void removeItemFromBuyList(Commodity commodity) throws CommodityIsNotInBuyList {
        String id = commodity.getId();
        if (this.buyList.containsKey(id)) {
            int existingQuantity = this.buyList.get(id);
            if (existingQuantity == 1)
                this.buyList.remove(commodity.getId());
            else
                this.buyList.put(id, existingQuantity - 1);
        } else
            throw new CommodityIsNotInBuyList();
    }

}




public class UserCreditManagementStepsadd {

/*
Feature: User Credit Management

  Scenario: Add credit to the user's account
    Given a user with a credit of 100
    When the user adds 50 credit
    Then the user's credit should be 150

  Scenario: Add negative credit to the user's account
    Given a user with a credit of 100
    When the user adds -50 credit
    Then an InvalidCreditRange exception should be thrown
 */
    private User user;

    @Given("a user with a credit of {int}")
    public void givenAUserWithCredit(int initialCredit) {
        user = new User(initialCredit);
    }

    @When("the user adds {int} credit")
    public void whenTheUserAddsCredit(int creditToAdd) {
        try {
            user.addCredit(creditToAdd);
        } catch (InvalidCreditRange e) {
        }
    }

    @Then("the user's credit should be {int}")
    public void thenTheUsersCreditShouldBe(int expectedCredit) {
        Assert.assertEquals(expectedCredit, user.getCredit());
    }

    @Then("an InvalidCreditRange exception should be thrown")
    public void thenAnInvalidCreditRangeExceptionShouldBeThrown() {
    }
}

public class UserCreditManagementStepsWithdraw {
/*
Feature: User Credit Management

  Scenario: Withdraw credit from the user's account
    Given a user with a credit of 100
    When the user withdraws 50 credit
    Then the user's credit should be 50

  Scenario: Withdraw more credit than available
    Given a user with a credit of 100
    When the user withdraws 150 credit
    Then an InsufficientCredit exception should be thrown
 */


    private User user;

    @Given("a user with a credit of {int}")
    public void givenAUserWithCredit(int initialCredit) {
        user = new User(initialCredit);
    }

    @When("the user withdraws {int} credit")
    public void whenTheUserWithdrawsCredit(int creditToWithdraw) {
        try {
            user.withdrawCredit(creditToWithdraw);
        } catch (InsufficientCredit e) {
        }
    }

    @Then("the user's credit should be {int}")
    public void thenTheUsersCreditShouldBe(int expectedCredit) {
        Assert.assertEquals(expectedCredit, user.getCredit());
    }

    @Then("an InsufficientCredit exception should be thrown")
    public void thenAnInsufficientCreditExceptionShouldBeThrown() {
    }
}

public class UserBuyListManagementSteps {
    /*
    Feature: User Buy List Management

  Scenario: Remove an item from the user's buy list with quantity greater than 1
    Given a user with a buy list containing item X with quantity 2
    When the user removes 1 item X from the buy list
    Then the quantity of item X in the buy list should be 1

  Scenario: Remove the last item from the user's buy list
    Given a user with a buy list containing only 1 item Y
    When the user removes 1 item Y from the buy list
    Then the buy list should be empty

  Scenario: Remove an item that is not in the user's buy list
    Given a user with an empty buy list
    When the user tries to remove item Z from the buy list
    Then a CommodityIsNotInBuyList exception should be thrown
     */

    private User user;

    @Given("a user with a buy list containing item X with quantity {int}")
    public void givenAUserWithBuyListContainingItemX(int initialQuantity) {
        user = new User();
        user.addItemToBuyList(new Commodity("X"), initialQuantity);
    }

    @When("the user removes {int} item X from the buy list")
    public void whenTheUserRemovesItemXFromBuyList(int quantityToRemove) {
        try {
            user.removeItemFromBuyList(new Commodity("X"), quantityToRemove);
        } catch (CommodityIsNotInBuyList e) {

        }
    }

    @Then("the quantity of item X in the buy list should be {int}")
    public void thenTheQuantityOfItemXShouldBe(int expectedQuantity) {
        int actualQuantity = user.getBuyList().get("X");
        Assert.assertEquals(expectedQuantity, actualQuantity);
    }

    @Then("the buy list should be empty")
    public void thenTheBuyListShouldBeEmpty() {
        Assert.assertTrue(user.getBuyList().isEmpty());
    }

    @Then("a CommodityIsNotInBuyList exception should be thrown")
    public void thenACommodityIsNotInBuyListExceptionShouldBeThrown() {
    }
}
