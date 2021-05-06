package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialsTab {
    
    @FindBy(id = "add-new-credential")
    private WebElement addNewCredential;

    @FindBy(id = "credential-url")
    private WebElement credentialURLField;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id = "save-credential")
    private WebElement saveCredentialButton;

    @FindBy(id = "edit-credential")
    private WebElement editCredential;

    @FindBy(id = "delete-credential")
    private WebElement deleteCredential;

    public CredentialsTab(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void addCredential() {
        addNewCredential.click();
    }

    public void editCredential() {
        editCredential.click();
    }

    public void clearTextFields() {
        credentialURLField.clear();
        credentialUsernameField.clear();
        credentialPasswordField.clear();
    }

    public void fillingFields(String url, String username, String password) {
        credentialURLField.sendKeys(url);
        credentialUsernameField.sendKeys(username);
        credentialPasswordField.sendKeys(password);
    }

    public void saveCredential() {
        saveCredentialButton.click();
    }

    public void deleteCredential() {
        deleteCredential.click();
    }

    public Credential getCredentialData() {
        return new Credential(null, credentialURLField.getText(), credentialUsernameField.getText(), null,
                credentialPasswordField.getText(), null);
    }
    
}
