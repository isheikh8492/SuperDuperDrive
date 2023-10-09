package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {
    private FileService fileService;
    private NoteService noteService;
    private CredentialsService credentialsService;
    private UserMapper userMapper;
    private EncryptionService encryptionService;

    public HomeController(FileService fileService, NoteService noteService, CredentialsService credentialsService, UserMapper userMapper, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.userMapper = userMapper;
        this.credentialsService = credentialsService;
        this.encryptionService = encryptionService;
    }
    @GetMapping("/home")
    public String home(Authentication authentication, Model model) {
        String loggedInUsername = authentication.getPrincipal().toString();
        User user = userMapper.getUser(loggedInUsername);

        model.addAttribute("files", fileService.getAllFiles(user.getUserId()));
        model.addAttribute("notes", noteService.getAllNotes(user.getUserId()));
        return "home";
    }

    @GetMapping("/result")
    public String result() { return "result"; }

}
