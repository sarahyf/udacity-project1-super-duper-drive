package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;

import org.springframework.stereotype.Service;


@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int addNote(Note note) {
        return noteMapper.insertNote(new Note(null, note.getNoteTitle(), note.getNoteDescription(), note.getUserId()));
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public void deleteNote(Note note) {
        noteMapper.deleteNote(note.getNoteId());
    }

}
