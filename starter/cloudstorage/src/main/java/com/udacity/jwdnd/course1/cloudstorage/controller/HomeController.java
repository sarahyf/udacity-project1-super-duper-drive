package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private FileService fileService;

    public HomeController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping()
    public String homeView() {
        return "home";
    }

    @PostMapping()
    public String uploadFile(@ModelAttribute File file, @ModelAttribute User user, Model model) {

        if(fileService.isFileNameAvailable(file.getFileName(), user.getUsername())) {
            int i = fileService.uploadFile(file);
            System.out.println("fileName:" + file.getFileName());
            System.out.println("username:" + user.getUsername());
            System.out.println("INSERT ID:" + i);
        }

        return "home";
    }

}
