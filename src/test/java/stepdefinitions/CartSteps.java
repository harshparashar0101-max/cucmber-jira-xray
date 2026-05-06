package stepdefinitions;

import org.openqa.selenium.By;
import org.testng.Assert;

import io.cucumber.java.en.*;

public class CartSteps {

    @Given("user is on products page")
    public void user_is_on_products_page() {
        Hooks.driver.get("https://automationexercise.com/products");
    }

    @When("user adds first product to cart")
    public void user_adds_first_product_to_cart() {
       // Hooks.driver.findElement(By.xpath("(//a[contains(text(),'Add to cart')])[1]")).click();
        Hooks.driver.findElement(By.xpath("//a[@data-product-id='1']")).click();
    }

    @And("user clicks view cart")
    public void user_clicks_view_cart() {
        Hooks.driver.findElement(By.xpath("//u[text()='View Cart']")).click();
        //Hooks.driver.findElement(By.xpath("//a[herf='/viewcart']")).click();
    }

    @Then("product should be visible in cart")
    public void product_should_be_visible_in_cart() {
        boolean productVisible = Hooks.driver
                .findElement(By.xpath("//td[@class='cart_description']"))
                .isDisplayed();

        Assert.assertTrue(productVisible, "Product is not visible in cart");
    }
}