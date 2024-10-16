package com.example.journalApp.service;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;
import com.example.journalApp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired private UserService userService;

    public void saveEntry(JournalEntry journalEntry, String username){
        User userInfo = userService.findByUsername(username);
        JournalEntry saved = journalEntryRepository.save(journalEntry);
        userInfo.getJournalEntries().add(saved);
        userService.saveEntry(userInfo);
    }

    public void saveEntry(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id){
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id, String username){
        User userInfo = userService.findByUsername(username);
        userInfo.getJournalEntries().removeIf(x -> x.getId().equals(id));
        userService.saveEntry(userInfo);
        journalEntryRepository.deleteById(id);
    }
}
