package stepdefinitions;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class DashboardSteps {

    WebDriver driver = Hooks.driver;

   // @Before
    //public void setup() {
      //  driver = new ChromeDriver();
       // driver.manage().window().maximize();
  //  }

    @Given("user launches the application URL")
    public void user_launches_the_application_url() {
        driver.get("https://dataqa.on24.com/wcc/report?cb=on24&token=u8w2jrte8UD0WBcLZOjxseHQQpVLuBfLXJSDQdr2_TAzQYDzNnDME1rnEPekLhAVgI4uA6w2AadGADBu0Zwwg_dxns_xnlkQnjUzyq-9mfj6gR2qvxTwZDLgZ8eWL3O6"); // replace with your actual URL
    }

    @Then("dashboard text should be visible on header")
    public void dashboard_text_should_be_visible_on_header() {

    	WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(90));
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id("panel-iframe")));
    	String headerText = driver.findElement(By.id("dashboard-title")).getText();
        System.out.println("Header Text: " + headerText);

        if(headerText.equalsIgnoreCase("Dashboard")) {
            System.out.println("Dashboard is visible ");
        } else {
            System.out.println("Dashboard is NOT visible ");
        }
    }

   
}