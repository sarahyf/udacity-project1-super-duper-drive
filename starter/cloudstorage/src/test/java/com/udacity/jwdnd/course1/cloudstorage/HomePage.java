package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTabLink;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        this.submitButton.click();
    }

    public void noteTab() {
       noteTabLink.click();
    }

}
