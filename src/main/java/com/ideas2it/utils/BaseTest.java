package com.ideas2it.utils;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

public class BaseTest {
    protected WebDriver driver;
    protected PageManager pageManager;

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        driver = DriverFactory.getDriver(browser);
        pageManager = PageManager.getInstance(driver);
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
        pageManager.remove();
    }
}
