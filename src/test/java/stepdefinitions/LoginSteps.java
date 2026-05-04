package stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginSteps {
	
	WebDriver driver= Hooks.driver;
	
	// @Before
	    //public void setup() {
	       // driver = new ChromeDriver();
	        //driver.manage().window().maximize();
	   // }

    @Given("user is on login page")
    public void user_is_on_login_page() {
        driver.get("https://wccqa.on24.com/webcast/dashboard");
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(90));
        
    }

    @When("user enters valid username and password")
    public void user_enters_valid_username_and_password() {
    	driver.findElement(By.id("username")).sendKeys("shreyawebcast1");
		driver.findElement(By.id("password")).sendKeys("Password11");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    @Then("user should be logged in successfully")
    public void user_should_be_logged_in_successfully() {
    	 String actualTitle = driver.getTitle();
    	    System.out.println("Page Title is: " + actualTitle);

    	    if(actualTitle.contains("Dashboard")) {
    	        System.out.println("Login Successful ");
    	    } else {
    	        System.out.println("Login Failed ");
    	    }
    }
}