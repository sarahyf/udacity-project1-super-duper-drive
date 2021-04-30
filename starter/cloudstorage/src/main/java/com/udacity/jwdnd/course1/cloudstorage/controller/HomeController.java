package com.udacity.jwdnd.course1.cloudstorage.controller;

import javax.websocket.server.PathParam;

import com.fasterxml.jackson.annotation.JacksonInject.Value;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
 
        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST, params = "noteSubmit")
    public String addEditNote(Authentication authentication, @ModelAttribute("newNote") Note note, Model model) {
        System.out.println("note");
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);

        if(note.getNoteId() != null) {
            System.out.println(note.getNoteId());
            noteService.updateNote(note);
        } else {
            noteService.addNote(note);
        }

        model.addAttribute("allNotes", noteService.getAllNotes(userId));
    
        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST, params = "deleteNote")
    public String deleteNote(Authentication authentication, @ModelAttribute("aNote") Note note, Model model) {
        System.out.println("at deleteNote method");
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);
        noteService.deleteNote(note);

        model.addAttribute("allNotes", noteService.getAllNotes(userId));
        
        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST, params = "credentialSubmit")
    public String addEditCredential(Authentication authentication, @ModelAttribute("newCredential") Credential credential, Model model) {
        System.out.println("addCredentials");
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        credential.setUserId(userId);

        if(credential.getCredentialId() != null) {
            System.out.println(credential.getCredentialId());
            credentialService.updateCredential(credential);
        } else {
            credentialService.addCredential(credential);
        }

        model.addAttribute("allCredentials", credentialService.getAllCredentials(userId));

        return "home";
    }

    @ModelAttribute("plainTextPassword")
    public String decryptPassword(@ModelAttribute("aCredential") Credential credential) {
        if(credential.getPassword() != null && credential.getCredentialId() != null) {
            System.out.println(credential.getPassword());
            return credentialService.decryptPassword(credential);
        }
        
        return "password";
            //     Integer userId = userService.getUser(authentication.getName()).getUserId();
            //     credential.setUserId(userId);

            // model.addAttribute("plainTextPassword", credentialService.decryptPassword(credential));
    }

    //the items are duplicated when I refresh the page, so take it into consideration.
    //when a credential is add the already added note disappear 

}
