package com.example.journalApp.controller;

import com.example.journalApp.entity.JournalEntry;
import com.example.journalApp.service.JournalEntryService;
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

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAll() {
        return new ResponseEntity<>(journalEntryService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry newEntry) {
        try {
            journalEntryService.saveEntry(newEntry);
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

    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteJournalDoc(@PathVariable ObjectId myId){
        Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
        if(journalEntry.isPresent()){
            journalEntryService.deleteById(myId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @PutMapping("id/{id}")
    public ResponseEntity<JournalEntry> updateJournalDoc(@PathVariable ObjectId id, @RequestBody JournalEntry updatedEntry){

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
