package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.entity.User;
import com.example.journalApp.service.JournalEntryService;
import com.example.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RequestMapping("/journal")
@RestController
public class JourneyEntryControllerv2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{username}")
    public ResponseEntity<List<JournalEntry>> getAll(@PathVariable String username) {
        User userInfo = userService.findByUsername(username);
        List<JournalEntry> allEntries = userInfo.getJournalEntries();
        return new ResponseEntity<>(allEntries, HttpStatus.OK);
    }

    @PostMapping("{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry newEntry, @PathVariable String username) {
        try {
            journalEntryService.saveEntry(newEntry, username);
            return new ResponseEntity<>(newEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId){

         Optional<JournalEntry> journalEntry= journalEntryService.findById(myId);
         if(journalEntry.isPresent()) {
             return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
         }
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{username}/{myId}")
    public ResponseEntity<?> deleteJournalDoc(@PathVariable ObjectId myId, @PathVariable String username){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if(journalEntry.isPresent()){
            journalEntryService.deleteById(myId, username);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PutMapping("id/{username}/{id}")
    public ResponseEntity<JournalEntry> updateJournalDoc(
            @PathVariable ObjectId id,
            @RequestBody JournalEntry updatedEntry,
            @PathVariable String username){
        JournalEntry oldJournalDoc = journalEntryService.findById(id).orElse(null);
        if(oldJournalDoc!=null){
            String newContent = updatedEntry.getContent();
            String newTitle = updatedEntry.getTitle();
            oldJournalDoc.setTitle(newTitle != null && !newTitle.equals("") ? newTitle : oldJournalDoc.getTitle());
            oldJournalDoc.setContent(newContent!=null && !newContent.equals("") ? newContent : oldJournalDoc.getContent());
            journalEntryService.saveEntry(oldJournalDoc);
            return new ResponseEntity<>(oldJournalDoc, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
