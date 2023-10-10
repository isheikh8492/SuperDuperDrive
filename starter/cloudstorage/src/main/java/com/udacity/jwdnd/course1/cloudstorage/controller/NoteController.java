package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/home/notes")
public class NoteController {
    private NoteService noteService;
    private UserMapper userMapper;

    public NoteController(NoteService noteService, UserMapper userMapper) {
        this.noteService = noteService;
        this.userMapper = userMapper;
    }


    @PostMapping()
    public String handleAddOrUpdateNote(Note note, Authentication authentication) {
        String loggedInUserName = authentication.getPrincipal().toString();
        User user = userMapper.getUser(loggedInUserName);
        if (note.getNoteId() != null) {
            noteService.updateNote(note);
        } else {
            noteService.addNote(note, user.getUserId());
        }

        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("id") Integer noteId, Authentication authentication, RedirectAttributes redirectAttributes) {
        String loggedInUserName = authentication.getPrincipal().toString();
        User user = userMapper.getUser(loggedInUserName);

        if (noteId > 0) {
            noteService.deleteNote(noteId, user.getUserId());
            return "redirect:/result?success";
        }

        redirectAttributes.addAttribute("error", "Unable to delete the note.");
        return "redirect:/result?error";
    }
}
