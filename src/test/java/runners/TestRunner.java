package runners;

import org.testng.annotations.Test;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@Test
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "stepdefinitions",
        plugin = {
                "pretty",
                "json:target/cucumber.json",
                "junit:target/cucumber.xml",
                "html:target/cucumber-report.html"
        },
        monochrome = true,
        tags="@smoke"
        
)
public class TestRunner extends AbstractTestNGCucumberTests {
}