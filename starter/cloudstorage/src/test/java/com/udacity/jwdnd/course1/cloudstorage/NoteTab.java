package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NoteTab {

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

    @FindBy(id = "delete-note")
    private WebElement deleteNote;

    public NoteTab(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void addNote() {
        System.out.println("first");
        addNewNote.click();
        System.out.println("second");
        // fillingFields(noteTitle, noteDescription);
    }

    public void editNote() {
        editNote.click();
    }

    public void clearTextFields() {
        noteTitleField.clear();
        noteDescriptionField.clear();
    }

    public void fillingFields(String noteTitle, String noteDescription) {
        System.out.println("at fillingFields");
        noteTitleField.sendKeys(noteTitle);
        System.out.println("noteTitle");
        noteDescriptionField.sendKeys(noteDescription);
        System.out.println("noteDescription");
        saveNoteButton.click();
        System.out.println("saveNoteButton");
    }
    
    public void deleteNote() {
        deleteNote.click();
    }

    public Note getNoteData() {
        return new Note(null, noteTitleField.getText(), noteDescriptionField.getText(), null);
    }
}
