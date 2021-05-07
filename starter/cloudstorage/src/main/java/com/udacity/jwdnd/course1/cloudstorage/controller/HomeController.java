package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.io.IOException;
import java.io.InputStream;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private FileService fileService;
    private NoteService noteService;
    private UserService userService;
    private CredentialService credentialService;
    private static boolean checkError = false;

    public HomeController(FileService fileService, NoteService noteService, UserService userService, CredentialService credentialService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    public String homeView(Authentication authentication, Model model) {
            Integer userId = userService.getUser(authentication.getName()).getUserId();

            model.addAttribute("activeTab", "files");
            getAllTabsData(userId, model);

            if(checkError) {
                model.addAttribute("errorMessage", "File name already exists");
                checkError = false;
            }

        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST, params = "fileSubmit")
    public String uploadFile(Authentication authentication, @RequestParam("fileUpload") MultipartFile fileUpload, Model model,
            RedirectAttributes redirectAttributes) throws IOException {
            Integer userId = userService.getUser(authentication.getName()).getUserId();

            if(fileService.isFileNameAvailable(fileUpload.getOriginalFilename(), userId)) {
                InputStream fis = fileUpload.getInputStream();

                fileService.uploadFile(new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(),
                        fileUpload.getSize(), userId, fis));

                getAllTabsData(userId, model);
            } else {
                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("message", "File name already exists");
                checkError = true;
            }

        model.addAttribute("activeTab", "files");

        return "home";
    }

    @RequestMapping(value = "/home/file/download/{fileId}", method = RequestMethod.GET)
    public ResponseEntity downloadFile(Authentication authentication, @ModelAttribute("aFile") File file, Model model) {
        file.setUserId(userService.getUser(authentication.getName()).getUserId());

        InputStreamResource resource = new InputStreamResource(fileService.getFile(file).getFileData());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileService.getFile(file).getContentType()))
               .header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=\"" + fileService.getFile(file)
                                .getFileName() + "\"")
               .body(resource);

    }

    @RequestMapping(value = "/home/file/{fileId}", method = RequestMethod.GET)
    public String deleteFile(Authentication authentication, @ModelAttribute("aFile") File file, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        file.setUserId(userId);
        fileService.deleteFile(file);

        getAllTabsData(userId, model);
        model.addAttribute("activeTab", "files");

        return "home";
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST, params = "noteSubmit")
    public String addEditNote(Authentication authentication, @ModelAttribute("newNote") Note note, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);

        if(note.getNoteId() != null) {
            noteService.updateNote(note);
            // model.addAttribute("addNoteError", true);
        } else {
            noteService.addNote(note);
            // model.addAttribute("addNoteSuccess", true);
        }

        getAllTabsData(userId, model);
        model.addAttribute("activeTab", "notes");

        return "home";
    }

   @RequestMapping(value = "/home/note/{noteId}", method = RequestMethod.GET)
    public String deleteNote(Authentication authentication, @ModelAttribute("aNote") Note note, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);
        noteService.deleteNote(note);

        getAllTabsData(userId, model);
        model.addAttribute("activeTab", "notes");

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

        getAllTabsData(userId, model);
        model.addAttribute("activeTab", "credentials");

        return "home";
    }

    @RequestMapping(value = "/home/credential/{credentialId}", method = RequestMethod.GET)
    public String deleteCredential(Authentication authentication, @ModelAttribute("aCredential") Credential credential, Model model,
            @PathVariable(value = "credentialId") Integer credentialId) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        credential.setUserId(userId);
        credentialService.deleteCredential(credentialId);

        getAllTabsData(userId, model);
        model.addAttribute("activeTab", "credentials");

        return "home";
    }

    public void getAllTabsData(Integer userId, Model model) {
        model.addAttribute("allFiles", fileService.getAllUserFile(userId));
        model.addAttribute("allNotes", noteService.getAllNotes(userId));
        model.addAttribute("allCredentials", credentialService.getAllCredentials(userId));
        model.addAttribute("credentialService", credentialService);
    }

}
