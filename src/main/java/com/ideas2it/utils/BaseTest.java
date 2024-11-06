package com.ideas2it.utils;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
public class BaseTest {
    protected WebDriver driver;
    protected PageManager pageManager;

    @BeforeMethod
    @Parameters("browser")
    public void setUp(String browser) {
        driver = DriverFactory.getDriver(browser);
        pageManager = PageManager.getInstance(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
        pageManager.remove();
    }
}
