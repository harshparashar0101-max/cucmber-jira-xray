package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginSteps {
	
	WebDriver driver = Hooks.driver;

    @Given("user is on login page")
    public void user_is_on_login_page() {
        driver.get("https://automationexercise.com/login");
    }

    @When("user enters valid email and password")
    public void enter_valid_credentials() {
        driver.findElement(By.xpath("//input[@data-qa='login-email']")).sendKeys("newold@mail.com");
        driver.findElement(By.xpath("//input[@data-qa='login-password']")).sendKeys("123456");
    }

    @And("clicks on login button")
    public void click_login() {
        driver.findElement(By.xpath("//button[@data-qa='login-button']")).click();
    }

    @Then("user should be logged in successfully")
    public void verify_login() {
    	boolean loggedInText = Hooks.driver
                .findElement(By.xpath("//a[contains(text(),'Logged in as')]"))
                .isDisplayed();

        Assert.assertTrue(loggedInText, "User is not logged in successfully");
        
    }
    
    @When("user enters invalid email and password")
    public void enter_invalid_cre() {
    	 driver.findElement(By.xpath("//input[@data-qa='login-email']")).sendKeys("xyold@mail.com");
         driver.findElement(By.xpath("//input[@data-qa='login-password']")).sendKeys("1256");
    }
    
    @Then("error message should be displayed")
     public void errormsg() {
    	 String actualError = Hooks.driver
                 .findElement(By.xpath("//p[text()='Your email or password is incorrect!']"))
                 .getText();
    	 Assert.assertEquals(actualError, "Your email or password is incorrect!");
    	
    }
}