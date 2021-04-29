package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
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
import org.springframework.web.util.pattern.PathPattern;

@Controller
@RequestMapping("/home/note")
public class NoteController {
    
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping()
    public String homeView(Authentication authentication, @ModelAttribute Note note, Model model) {
        // if(userService.getUser(authentication.getName()) != null) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        model.addAttribute("allNotes", noteService.getAllNotes(userId));
        // }

        return "home";
    }

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
