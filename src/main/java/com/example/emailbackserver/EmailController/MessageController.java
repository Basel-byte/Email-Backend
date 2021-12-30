package com.example.emailbackserver.EmailController;

import com.example.emailbackserver.EmailModel.Message;
import com.example.emailbackserver.EmailService.MessageService;
import com.example.emailbackserver.EmailService.UserFilesService;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.io.IOException;

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
    public Message[] filterSearch(@PathVariable String userEmail, @RequestBody String message) throws FileNotFoundException, CloneNotSupportedException {
        System.out.println(message);
        Message filters = new GsonBuilder().create().fromJson(message, Message.class);
        return messageService.filteredSearch(filters, userEmail);
    }

    @PostMapping("/starMessage/{emailAddress}")
    public void starMessage(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.starMessage(id, emailAddress);
    }

    @PostMapping("/importantMessage/{emailAddress}")
    public void makeMessageImportant(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.makeMessageImportant(id, emailAddress);
    }

    @PostMapping("/draftMessage/{emailAddress}")
    public void draftMessage(@RequestBody String message, @PathVariable String emailAddress) throws IOException, CloneNotSupportedException {
        messageService.draftMessage(message, emailAddress);
    }

    @PostMapping("/customMessage/{emailAddress}")
    public void makeCustom(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.makeCustom(id, emailAddress);
    }

    @PostMapping("/deleteMessage/{emailAddress}")
    public void deleteMessage(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.deleteMessage(id, emailAddress);
    }

    @PostMapping("/readMessage/{emailAddress}")
    public void readMessage(@RequestBody String id, @PathVariable String emailAddress) throws FileNotFoundException {
        messageService.readMessage(id, emailAddress);
    }

    @PostMapping("/getFile/{emailAddress}")
    public Message[] getMessagesFile(@RequestBody String fileName, @PathVariable String emailAddress) throws FileNotFoundException {
        System.out.println("UUUUUUU: " + emailAddress);
        return filesService.readMessageFile(emailAddress, fileName);
    }
}