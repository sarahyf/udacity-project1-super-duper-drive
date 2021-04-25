package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private FileService fileService;
    private NoteService noteService;
    private UserService userService;

    public HomeController(FileService fileService, NoteService noteService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping()
    public String homeView(Authentication authentication, @ModelAttribute Note note, Model model) {
       // if(userService.getUser(authentication.getName()) != null) {
            Integer userId = userService.getUser(authentication.getName()).getUserId();

            model.addAttribute("allNotes", noteService.getAllNotes(userId));
      //  }
 
        return "home";
    }

    /*@PostMapping()
    public String uploadFile(@ModelAttribute File file, @ModelAttribute User user, Model model) {

        if(fileService.isFileNameAvailable(file.getFileName(), user.getUsername())) {
            int i = fileService.uploadFile(file);
            System.out.println("fileName:" + file.getFileName());
            System.out.println("username:" + user.getUsername());
            System.out.println("INSERT ID:" + i);
        }

        return "home";
    }*/

    @PostMapping()
    public String addNote(Authentication authentication, @ModelAttribute Note note, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);
        noteService.addNote(note);
        
        model.addAttribute("allNotes", noteService.getAllNotes(userId));
    
        return "home";
    }

    @DeleteMapping()
    public String deleteNote(Authentication authentication, @ModelAttribute("aNote") Note note, Model model) {
        System.out.println("there");
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);
        noteService.deleteNote(note);

        model.addAttribute("allNotes", noteService.getAllNotes(userId));

        return "home";
    }

}
