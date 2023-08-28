package com.library.steps;




import com.library.utility.ConfigurationReader;
import com.library.utility.DB_Util;
import com.library.utility.Driver;
import io.cucumber.java.*;
import io.restassured.RestAssured;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.time.Duration;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.reset;


public class Hooks {

    @Before("@ui")
    public void setUp(){
        Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        Driver.getDriver().manage().window().maximize();
        Driver.getDriver().get(ConfigurationReader.getProperty("library_url"));
    }
    @After("@ui")
    public void tearDown(Scenario scenario){
        if(scenario.isFailed()){
            final byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot,"image/png","screenshot");
        }
        Driver.closeDriver();
    }


    @Before("@db")
    public void setUpDB(){
        System.out.println("Connecting to database...");
        DB_Util.createConnection();
    }
    @After("@db")
    public void tearDownDB(){
        System.out.println("close database connection...");
        DB_Util.destroy();
    }

    @Before("@api")
    public static void setUpAPI(){
        RestAssured.baseURI = ConfigurationReader.getProperty("library.baseUri");
    }
    @After("@api")
    public static void destroy(){
        reset();
    }

}
