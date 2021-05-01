package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    private FileService fileService;
    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;

    public HomeController(FileService fileService, NoteService noteService, UserService userService, CredentialService credentialService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String homeView(Authentication authentication, @ModelAttribute("newNote") Note note, @ModelAttribute("newCredential") Credential credential, Model model) {
            Integer userId = userService.getUser(authentication.getName()).getUserId();

            model.addAttribute("allNotes", noteService.getAllNotes(userId));
            model.addAttribute("allCredentials", credentialService.getAllCredentials(userId));
            model.addAttribute("credentialService", credentialService);
 
        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST, params = "noteSubmit")
    public String addEditNote(Authentication authentication, @ModelAttribute("newNote") Note note, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);

        if(note.getNoteId() != null) {
            noteService.updateNote(note);
        } else {
            noteService.addNote(note);
        }

        model.addAttribute("allNotes", noteService.getAllNotes(userId));
    
        return "home";
    }

   @RequestMapping(value = "/home/note/{noteId}", method = RequestMethod.GET)
    public String deleteNote(Authentication authentication, @ModelAttribute("aNote") Note note, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);
        noteService.deleteNote(note);

        model.addAttribute("allNotes", noteService.getAllNotes(userId));
        
        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST, params = "credentialSubmit")
    public String addEditCredential(Authentication authentication, @ModelAttribute("newCredential") Credential credential, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        credential.setUserId(userId);

        if(credential.getCredentialId() != null) {
            credentialService.updateCredential(credential);
        } else {
            credentialService.addCredential(credential);
        }

        model.addAttribute("allCredentials", credentialService.getAllCredentials(userId));
        model.addAttribute("credentialService", credentialService);

        return "home";
    }

    @RequestMapping(value = "/home/credential/{credentialId}", method = RequestMethod.GET)
    public String deleteCredential(Authentication authentication, @ModelAttribute("aCredential") Credential credential, Model model,
            @PathVariable(value = "credentialId") Integer credentialId) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        credential.setUserId(userId);
        credentialService.deleteCredential(credentialId);

        model.addAttribute("allCredentials", credentialService.getAllCredentials(userId));

        return "home";
    }

    //the items are duplicated when I refresh the page, so take it into consideration.
    //when a credential is add the already added note disappear 

}
