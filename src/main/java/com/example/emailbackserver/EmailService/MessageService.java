package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.Message;
import com.example.emailbackserver.EmailService.MessageCriteria.*;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class MessageService {

    private final UserFilesService filesService;

    @Autowired
    public MessageService(UserFilesService filesService) {
        this.filesService = filesService;
    }

    public void sendEmail(String message) throws IOException, CloneNotSupportedException {

        Message eMail = new GsonBuilder().create().fromJson(message, Message.class);

        Message[] sentArray = this.filesService.readMessageFile(eMail.getFrom(), "Sent");
        Message[] modifiedSent = new Message[sentArray.length + 1];
        for (int i = 0; i < sentArray.length; i++) modifiedSent[i] = sentArray[i].clone();
        modifiedSent[sentArray.length] = eMail.clone();
        filesService.updateMessageFile(eMail.getFrom(), modifiedSent, "Sent");

        for (String toEmailAddress : eMail.getTo()) {
            Message[] inboxArray = filesService.readMessageFile(toEmailAddress, "Inbox");
            Message[] modifiedInbox = new Message[inboxArray.length + 1];
            for (int i = 0; i < inboxArray.length; i++) {
                modifiedInbox[i] = inboxArray[i].clone();
            }
            modifiedInbox[inboxArray.length] = eMail.clone();
            filesService.updateMessageFile(toEmailAddress, modifiedInbox, "Inbox");
        }
    }


    public Message[] notFilteredMessageSearch(String searchedFor, String emailAddress)
            throws FileNotFoundException, CloneNotSupportedException {

        MessageCriteria criteria1 = new ToCriteria(searchedFor, filesService.readMessageFile(emailAddress, "Sent"));
        MessageCriteria criteria2 = new FromCriteria(searchedFor, filesService.readMessageFile(emailAddress, "Inbox"));
        MessageCriteria criteria3 = new SubjectCriteria(searchedFor, filesService.getAllMessages(emailAddress));
        MessageCriteria criteria4 = new BodyCriteria(searchedFor, filesService.getAllMessages(emailAddress));
        MessageCriteria criteria5 = new DateCriteria(searchedFor, filesService.getAllMessages(emailAddress));

        MessageCriteria orCriteria = new OrCriteria(criteria1, criteria2, criteria3, criteria4, criteria5);
        return orCriteria.meetCriteria();
    }


    public Message[] filteredSearch(Message searchedFor, String emailAddress) throws FileNotFoundException, CloneNotSupportedException {
        List<MessageCriteria> criteria = new ArrayList<>();
        if(searchedFor.getTo().length != 0){
            MessageCriteria toCriteria= new ToCriteria(searchedFor.getTo()[0], filesService.readMessageFile(emailAddress, "Sent"));
            criteria.add(toCriteria);
        }
        if(! searchedFor.getFrom().equals("")){
            MessageCriteria fromCriteria= new FromCriteria(searchedFor.getFrom(), filesService.readMessageFile(emailAddress, "Inbox"));
            criteria.add(fromCriteria);
        }
        if(! searchedFor.getBody().equals("")){
            MessageCriteria bodyCriteria= new BodyCriteria(searchedFor.getBody(), filesService.getAllMessages(emailAddress));
            criteria.add(bodyCriteria);
        }
        if(! searchedFor.getSubject().equals("")){
            MessageCriteria subjectCriteria= new SubjectCriteria(searchedFor.getSubject(), filesService.getAllMessages(emailAddress));
            criteria.add(subjectCriteria);
        }
        if(! searchedFor.getDate().equals("")){
            MessageCriteria dateCriteria= new DateCriteria(searchedFor.getDate(), filesService.getAllMessages(emailAddress));
            criteria.add(dateCriteria);
        }
        MessageCriteria andCriteria = new AndCriteria(criteria);
        return andCriteria.meetCriteria();
    }



    public void starMessage(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        List<Message> starredMessages = new ArrayList();
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for (Message message : allMessages) {
            if (idList.contains(message.getiD())) {
                message.setStarred(true);
                starredMessages.add(message);
            }
        }
        filesService.updateMessageFile(emailAddress, filesService.listToArray(starredMessages)
                , "Starred");
    }

    public void makeMessageImportant(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        List<Message> importantMessages = new ArrayList();
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for (Message message : allMessages) {
            if (idList.contains(message.getiD())) {
                message.setStarred(true);
                importantMessages.add(message);
            }
        }
        filesService.updateMessageFile(emailAddress, filesService.listToArray(importantMessages)
                , "Important");
    }

//    public void draftMessage(String[] id, String emailAddress) throws IOException {
//        List<String> idList = new ArrayList<>();
//        Collections.addAll(idList, id);
//        List<Message> draftMessages = new ArrayList();
//        Message[] allMessages = filesService.getAllMessages(emailAddress);
//        for (Message message : allMessages) {
//            if (idList.contains(message.getiD())) {
//                message.setStarred(true);
//                draftMessages.add(message);
//            }
//        }
//        filesService.updateMessageFile(emailAddress, filesService.listToArray(draftMessages)
//                , "Draft");
//    }

    public void draftMessage(String message, String emailAddress) throws IOException, CloneNotSupportedException {
        Message eMail = new GsonBuilder().create().fromJson(message, Message.class);
        eMail.setDraft(true);
        Message[] draftArray = this.filesService.readMessageFile(eMail.getFrom(), "Draft");
        Message[] modifiedDraft = new Message[draftArray.length + 1];
        for (int i = 0; i < draftArray.length; i++) modifiedDraft[i] = draftArray[i].clone();
        modifiedDraft[draftArray.length] = eMail.clone();
        filesService.updateMessageFile(eMail.getFrom(), modifiedDraft, "draft");
    }

    public void makeCustom(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        List<Message> customMessages = new ArrayList();
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for (Message message : allMessages) {
            if (idList.contains(message.getiD())) {
                message.setStarred(true);
                customMessages.add(message);
            }
        }
        filesService.updateMessageFile(emailAddress, filesService.listToArray(customMessages),"Custom");
    }

    public void deleteMessage(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for(Message message : allMessages){
            if(idList.contains(message.getiD())){
                if(Objects.equals(message.getFrom(), emailAddress))
                    filesService.removeMessageFromFile(emailAddress, message, "Sent");
                else filesService.removeMessageFromFile(emailAddress, message, "Inbox");

                filesService.removeMessageFromFile(emailAddress, message, "Starred");
                filesService.removeMessageFromFile(emailAddress, message, "Important");
                filesService.removeMessageFromFile(emailAddress, message, "Draft");
                filesService.removeMessageFromFile(emailAddress, message, "Custom");

                Message[] starred = new Message[]{message};
                filesService.updateMessageFile(emailAddress, starred,"Trashed");
                return;
            }
        }
    }

    public void readMessage(String id, String emailAddress) throws FileNotFoundException {
        Message[] inbox = filesService.readMessageFile(emailAddress, "Inbox");
        for(Message message : inbox){
            if (Objects.equals(message.getiD(), id)){
                message.setRead(true);
                return ;
            }
        }
    }
}
