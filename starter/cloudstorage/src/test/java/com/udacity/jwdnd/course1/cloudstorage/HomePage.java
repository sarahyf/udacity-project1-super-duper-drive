package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "submit-button")
    private WebElement submitButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTabLink;

    @FindBy(id = "add-new-note")
    private WebElement addNewNote;

    @FindBy(id = "note-title")
    private WebElement noteTitleField;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionField;

    @FindBy(id = "save-note")
    private WebElement saveNoteButton;

    @FindBy(id = "edit-note")
    private WebElement editNote;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void logout() {
        this.submitButton.click();
    }

    public void noteTab() {
       noteTabLink.click();
    }

    // public void addNote(String noteTitle, String noteDescription) {
    //     addNewNote.click();
    //     fillingFields(noteTitle, noteDescription);
    // }

    // public void fillingFields(String noteTitle, String noteDescription) {
    //     noteTitleField.sendKeys(noteTitle);
    //     noteDescriptionField.sendKeys(noteDescription);
    //     saveNoteButton.click();
    // }
    
}
