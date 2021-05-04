package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.ErrorManager;

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

            model.addAttribute("allFiles", fileService.getAllUserFile(userId));
            model.addAttribute("allNotes", noteService.getAllNotes(userId));
            model.addAttribute("allCredentials", credentialService.getAllCredentials(userId));
            model.addAttribute("credentialService", credentialService);
            model.addAttribute("fileService", fileService);
            model.addAttribute("filesNames", fileService.getFilesName(userId));

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

            model.addAttribute("uploadedFileName", fileUpload.getOriginalFilename());
            System.out.println(fileUpload.getOriginalFilename());
            model.addAttribute("filesNames", fileService.getFilesName(userId));

            if(fileService.isFileNameAvailable(fileUpload.getOriginalFilename(), userId)) {
                InputStream fis = fileUpload.getInputStream();

                fileService.uploadFile(new File(null, fileUpload.getOriginalFilename(), fileUpload.getContentType(),
                        fileUpload.getSize(), userId, fis));

                model.addAttribute("allFiles", fileService.getAllUserFile(userId));
            } else {
                model.addAttribute("errorMessage", "File name already exists");
                model.addAttribute("allFiles", fileService.getAllUserFile(userId));
                model.addAttribute("filesNames", fileService.getFilesName(userId));
                model.addAttribute("uploadedFileName", fileUpload.getOriginalFilename());

                redirectAttributes.addAttribute("error", true);
                redirectAttributes.addAttribute("message", "File name already exists");
                //displayingErrorMessage("File name already exists");
                checkError = true;
            }

        return "redirect:/home";
    }

//    @ModelAttribute("errorMessage")
//     public String displayingErrorMessage(String errorMessage) {
//         System.out.println(errorMessage);
//         return errorMessage;
//        // return "File name already exists";
//     }

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

        model.addAttribute("allFiles", fileService.getAllUserFile(userId));

        return "redirect:/home";
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
    
        return "redirect:/home";
    }

   @RequestMapping(value = "/home/note/{noteId}", method = RequestMethod.GET)
    public String deleteNote(Authentication authentication, @ModelAttribute("aNote") Note note, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        note.setUserId(userId);
        noteService.deleteNote(note);

        model.addAttribute("allNotes", noteService.getAllNotes(userId));
        
        return "redirect:/home";
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

        return "redirect:/home";
    }

    @RequestMapping(value = "/home/credential/{credentialId}", method = RequestMethod.GET)
    public String deleteCredential(Authentication authentication, @ModelAttribute("aCredential") Credential credential, Model model,
            @PathVariable(value = "credentialId") Integer credentialId) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        credential.setUserId(userId);
        credentialService.deleteCredential(credentialId);

        model.addAttribute("allCredentials", credentialService.getAllCredentials(userId));

        return "redirect:/home";
    }

    //the items are duplicated when I refresh the page, so take it into consideration.
    //when a credential is add the already added note disappear 

}
