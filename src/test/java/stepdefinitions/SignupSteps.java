package stepdefinitions;

import org.openqa.selenium.By;
import org.testng.Assert;

import io.cucumber.java.en.*;

public class SignupSteps {

    @Given("user is on signup page")
    public void user_is_on_signup_page() {
        Hooks.driver.get("https://automationexercise.com/login");
    }

    @Then("new user signup section should be visible")
    public void new_user_signup_section_should_be_visible() {
        boolean signupText = Hooks.driver
                .findElement(By.xpath("//h2[text()='New User Signup!']"))
                .isDisplayed();

        Assert.assertTrue(signupText, "Signup section is not visible");
    }

    @When("user enters existing name and email")
    public void user_enters_existing_name_and_email() {
        Hooks.driver.findElement(By.xpath("//input[@data-qa='signup-name']")).sendKeys("newold");
        Hooks.driver.findElement(By.xpath("//input[@data-qa='signup-email']")).sendKeys("newold@mail.com");
        
    }

    @And("user clicks signup button")
    public void user_clicks_signup_button() {
        Hooks.driver.findElement(By.xpath("//button[@data-qa='signup-button']")).click();
    }

    @Then("existing email error message should be displayed")
    public void existing_email_error_message_should_be_displayed() {
        String actualError = Hooks.driver
                .findElement(By.xpath("//p[text()='Email Address already exist!']"))
                .getText();

        Assert.assertEquals(actualError, "Email Address already exist!");
    }
}