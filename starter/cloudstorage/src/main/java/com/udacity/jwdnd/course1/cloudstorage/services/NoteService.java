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

    public boolean updateNote(Note note) {
        noteMapper.updateNote(note);
        Note updatedRow = noteMapper.getNote(note);
        return note.getNoteTitle().equals(updatedRow.getNoteTitle())
                && note.getNoteDescription().equals(updatedRow.getNoteDescription());
    }

    public List<Note> getAllNotes(Integer userId) {
        return noteMapper.getAllNotes(userId);
    }

    public boolean deleteNote(Note note) {
        noteMapper.deleteNote(note.getNoteId());
        return noteMapper.getNote(note) == null;
    }

}
