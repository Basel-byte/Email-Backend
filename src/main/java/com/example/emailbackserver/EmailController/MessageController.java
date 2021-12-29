package com.example.emailbackserver.EmailController;

import com.example.emailbackserver.EmailModel.Message;
import com.example.emailbackserver.EmailService.MessageService;
import com.example.emailbackserver.EmailService.UserFilesService;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/emailMessage")
public class MessageController {
    private final MessageService messageService;
    private final UserFilesService filesService;

    @Autowired
    public MessageController(MessageService messageService, UserFilesService filesService) {
        this.messageService = messageService;
        this.filesService = filesService;
    }

    @PostMapping("/send")
    public void SendEmail(@RequestBody String message) throws IOException, CloneNotSupportedException {
        messageService.sendEmail(message);
    }

    @PostMapping("/noFilterSearch/{userEmail}")
    public Message[] nonFilterSearch(@RequestBody String expression, @PathVariable String userEmail)
            throws FileNotFoundException, CloneNotSupportedException {
        return messageService.notFilteredMessageSearch(expression, userEmail);
    }

    @PostMapping("/filterSearch/{userEmail}")
    public Message[] monoFilterSearch(@RequestBody String[] filterString, @PathVariable String userEmail) throws FileNotFoundException, CloneNotSupportedException {
        List<String> filterKey = new ArrayList<>();
        List<String> filterValue = new ArrayList<>();
        for (int i = 0; i < filterString.length; i++) {
            if(i % 2 == 0)
                filterKey.add(filterString[i]);
            else
                filterValue.add(filterString[i]);
        }
        if (filterKey.size() == 1)
            return messageService.monoFilterMessageSearch(filterValue, userEmail, filterKey);
        else if (filterKey.size() == 2)
            return messageService.biFilteredMessageSearch(filterValue, userEmail, filterKey);
        else if (filterKey.size() == 3)
            return messageService.triFilteredMessageSearch(filterValue, userEmail, filterKey);
        else if (filterKey.size() == 4)
            return messageService.quadFilteredMessageSearch(filterValue, userEmail, filterKey);
        else
            return messageService.pentaFilteredMessageSearch(filterValue, userEmail, filterKey);

    }

    @PutMapping("/starMessage/{emailAddress}")
    public void starMessage(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.starMessage(id, emailAddress);
    }

    @PutMapping("/importantMessage/{emailAddress}")
    public void makeMessageImportant(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.makeMessageImportant(id, emailAddress);
    }

    @PutMapping("/draftMessage/{emailAddress}")
    public void draftMessage(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.draftMessage(id, emailAddress);
    }

    @PutMapping("/customMessage/{emailAddress}")
    public void makeCustom(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.makeCustom(id, emailAddress);
    }

    @DeleteMapping("/deleteMessage/{emailAddress}")
    public void deleteMessage(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.deleteMessage(id, emailAddress);
    }

    @PutMapping("/readMessage/{emailAddress}")
    public void readMessage(@RequestBody String id, @PathVariable String emailAddress) throws FileNotFoundException {
        messageService.readMessage(id, emailAddress);
    }
//    @GetMapping("/getFile/{emailAddress}")
//    public Message[] getMessagesFile(@RequestParam String fileName, @PathVariable String userEmailAddress) throws FileNotFoundException {
//        return filesService.readMessageFile(userEmailAddress, fileName);
//    }

    @PostMapping("/getFile/{emailAddress}")
    public Message[] getMessagesFile(@RequestBody String fileName, @PathVariable String emailAddress) throws FileNotFoundException {
        return filesService.readMessageFile(emailAddress, fileName);
    }
}