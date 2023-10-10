package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/credentials")
public class CredentialsController {

    private CredentialsService credentialsService;
    private UserMapper userMapper;


    public CredentialsController(CredentialsService credentialsService, UserMapper userMapper) {
        this.credentialsService = credentialsService;
        this.userMapper = userMapper;
    }

    @PostMapping()
    public String handleAddOrUpdateCredentials(Credentials credentials, Authentication authentication) {
        String loggedInUsername = authentication.getPrincipal().toString();
        User user = userMapper.getUser(loggedInUsername);

        if (credentials.getCredentialId() != null) {
            credentialsService.updateCredentials(credentials, user.getUserId());
        } else {
            credentialsService.addCredentials(credentials, user.getUserId());
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Integer credentialId, Authentication authentication, RedirectAttributes redirectAttributes) {
        String loggedInUserName = authentication.getPrincipal().toString();
        User user = userMapper.getUser(loggedInUserName);

        if (credentialId > 0) {
            credentialsService.deleteCredentials(credentialId, user.getUserId());
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Unable to delete the credentials.");
        return "redirect:/result?error";
    }


}
