package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.Message;
import com.example.emailbackserver.EmailService.MessageCriteria.*;
import com.google.gson.Gson;
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

    public Message[] monoFilterMessageSearch(List<String> searchedFor, String emailAddress, List<String> criteria)
            throws FileNotFoundException, CloneNotSupportedException {
        MessageCriteria criteria1 = getCriterion(searchedFor.get(0), emailAddress, criteria.get(0));
        return criteria1.meetCriteria();
    }

    public Message[] biFilteredMessageSearch(List<String> searchedFor, String emailAddress, List<String>criteria)
            throws FileNotFoundException, CloneNotSupportedException {
        MessageCriteria andCriteria;
        MessageCriteria criteria1 = getCriterion(searchedFor.get(0), emailAddress, criteria.get(0));
        MessageCriteria criteria2 = getCriterion(searchedFor.get(1), emailAddress, criteria.get(1));

        andCriteria = new BinAndCriteria(criteria1, criteria2);
        return andCriteria.meetCriteria();
    }

    public Message[] triFilteredMessageSearch(List<String> searchedFor, String emailAddress, List<String> criteria)
            throws FileNotFoundException, CloneNotSupportedException {
        MessageCriteria criteria1 = getCriterion(searchedFor.get(0), emailAddress, criteria.get(0));
        MessageCriteria criteria2 = getCriterion(searchedFor.get(0), emailAddress, criteria.get(1));
        MessageCriteria criteria3 = getCriterion(searchedFor.get(0), emailAddress, criteria.get(2));

        MessageCriteria andCriteria = new TriAndCriteria(criteria1, criteria2, criteria3);
        return andCriteria.meetCriteria();
    }

    public Message[] quadFilteredMessageSearch(List<String> searchedFor, String emailAddress, List<String>criteria)
            throws FileNotFoundException, CloneNotSupportedException {
        MessageCriteria criteria1 = getCriterion(searchedFor.get(0), emailAddress, criteria.get(0));
        MessageCriteria criteria2 = getCriterion(searchedFor.get(1), emailAddress, criteria.get(1));
        MessageCriteria criteria3 = getCriterion(searchedFor.get(2), emailAddress, criteria.get(2));
        MessageCriteria criteria4 = getCriterion(searchedFor.get(3), emailAddress, criteria.get(3));

        MessageCriteria andCriteria = new QuadAndCriteria(criteria1, criteria2, criteria3, criteria4);
        return andCriteria.meetCriteria();
    }

    public Message[] pentaFilteredMessageSearch(List<String> searchedFor, String emailAddress, List<String> criteria)
            throws FileNotFoundException, CloneNotSupportedException {
        MessageCriteria criteria1 = getCriterion(searchedFor.get(0), emailAddress, criteria.get(0));
        MessageCriteria criteria2 = getCriterion(searchedFor.get(1), emailAddress, criteria.get(1));
        MessageCriteria criteria3 = getCriterion(searchedFor.get(2), emailAddress, criteria.get(2));
        MessageCriteria criteria4 = getCriterion(searchedFor.get(3), emailAddress, criteria.get(3));
        MessageCriteria criteria5 = getCriterion(searchedFor.get(4), emailAddress, criteria.get(4));
        MessageCriteria andCriteria = new QuadAndCriteria(criteria1, criteria2, criteria3, criteria4);
        return andCriteria.meetCriteria();
    }

    private MessageCriteria getCriterion(String criteria, String emailAddress, String searchedFor)
            throws FileNotFoundException {
        switch (criteria) {
            case "to":
                return new ToCriteria(searchedFor, filesService.readMessageFile(emailAddress, "Sent"));
            case "from":
                return new FromCriteria(searchedFor, filesService.readMessageFile(emailAddress, "Inbox"));
            case "subject":
                return new SubjectCriteria(searchedFor, filesService.getAllMessages(emailAddress));
            case "date":
                return new DateCriteria(searchedFor, filesService.getAllMessages(emailAddress));
            default:
                return new BodyCriteria(searchedFor, filesService.getAllMessages(emailAddress));
        }
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

    public void draftMessage(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        List<Message> draftMessages = new ArrayList();
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for (Message message : allMessages) {
            if (idList.contains(message.getiD())) {
                message.setStarred(true);
                draftMessages.add(message);
            }
        }
        filesService.updateMessageFile(emailAddress, filesService.listToArray(draftMessages)
                , "Draft");
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
                    filesService.removeMessageFromFile(emailAddress, message, "Inbox");
                else filesService.removeMessageFromFile(emailAddress, message, "Sent");

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
