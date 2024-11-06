package com.ideas2it.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import pages.HomePage;
import pages.LoginPage;
import pages.ProfilePage;

public class PageManager {
    private WebDriver driver;

    private static ThreadLocal<PageManager> pageManager = new ThreadLocal<>();

    // Individual pages as part of the PageManager
    private HomePage homePage;
    private LoginPage loginPage;
    private ProfilePage profilePage;

    private PageManager(WebDriver driver) {
        this.driver = driver;
    }

    public static PageManager getInstance(WebDriver driver) {
        if (pageManager.get() == null) {
            pageManager.set(new PageManager(driver));
        }
        return pageManager.get();
    }

    public HomePage getHomePage() {
        if (homePage == null) {
            homePage = PageFactory.initElements(driver, HomePage.class);
        }
        return homePage;
    }

    public LoginPage getLoginPage() {
        if (loginPage == null) {
            loginPage = PageFactory.initElements(driver, LoginPage.class);
        }
        return loginPage;
    }

    public ProfilePage getProfilePage() {
        if (profilePage == null) {
            profilePage = PageFactory.initElements(driver, ProfilePage.class);
        }
        return profilePage;
    }

    public void remove() {
        pageManager.remove();
    }
}
