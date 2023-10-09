package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;


    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void addNote(Note note, Integer userId) {
        note.setUserId(userId);
        noteMapper.insertNote(note);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public void deleteNote(Integer noteId, Integer userId) {
        noteMapper.deleteNote(noteId, userId);
    }
}
