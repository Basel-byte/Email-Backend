package com.example.emailbackserver.EmailController;

import com.example.emailbackserver.EmailModel.User;
import com.example.emailbackserver.EmailService.ContactsService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/emailUser")
public class ContactsController {

    private ContactsService contactsService;
    private Gson gson = new Gson();

    @Autowired
    public ContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @GetMapping("/searchUsers/{userEmailAddress}")
    public User[] searchAllUsers(@PathVariable String userEmailAddress, @RequestParam String searchedFor){
        return contactsService.searchAllUsers(userEmailAddress, searchedFor);
    }

    @GetMapping("/contacts/{userEmailAddress}")
    public User[] getContacts(@PathVariable String userEmailAddress){
        return contactsService.getContact(userEmailAddress);
    }

    @GetMapping("/searchContact/{userEmailAddress}")
    public User[] searchContact(@PathVariable String userEmailAddress, @RequestParam String searchedFor){
        return contactsService.searchContact(userEmailAddress,searchedFor);
    }

    @PutMapping("/addContact/{userEmailAddress}")
    public boolean addContact(@PathVariable String userEmailAddress, @RequestBody String userAdded){
        return contactsService.addContact(userEmailAddress, gson.fromJson(userAdded,User.class));
    }

    @DeleteMapping("/deleteContact/{userEmailAddress}")
    public boolean deleteContact(@PathVariable String userEmailAddress, @RequestBody String userDeleted){
        return contactsService.deleteContact(userEmailAddress, gson.fromJson(userDeleted,User.class));
    }
}
