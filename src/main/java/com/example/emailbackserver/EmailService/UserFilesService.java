package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserFilesService {
    private final String path ;
    private JSONParser parser;

    public UserFilesService() {
        this.path = "D:\\IntelliJ Projects\\emailBackServer\\users\\";
    }

    private Message[] messageParser(FileReader reader){
        parser = new JSONParser();
        Object read;
        try{read = parser.parse(reader);}
        catch (Exception e){return new Message[]{};}
        JSONArray jsonArray = (JSONArray) read;
        return new GsonBuilder().create().fromJson(jsonArray.toJSONString(), Message[].class);
    }
    private void writer(String filePath, String toBeTyped) throws IOException {
        File file = new File(filePath);
        FileWriter writer = new FileWriter(file);
        writer.write(toBeTyped);
        writer.flush();
        writer.close();
    }

    public Message[] listToArray(List<Message> messages){
        Message[] temp = new Message[messages.size()];
        temp = messages.toArray(temp);
        return temp;
    }

    public Message[] readMessageFile(String userEmailAddress, String fileName) throws FileNotFoundException {
        String inboxPath = this.path + userEmailAddress + "\\" + fileName + ".json";
        System.out.println(inboxPath);
        return messageParser(new FileReader(inboxPath));
    }
    public Message[] getAllMessages(String userEmailAddress) throws FileNotFoundException{
        List<Message> allMessages = new ArrayList<>();

        Message[] gotMessages = readMessageFile(userEmailAddress, "Inbox").clone();
        Collections.addAll(allMessages, gotMessages);

        gotMessages = readMessageFile(userEmailAddress, "Sent").clone();
        Collections.addAll(allMessages, gotMessages);

        gotMessages = readMessageFile(userEmailAddress, "Trashed").clone();
        Collections.addAll(allMessages, gotMessages);

        if(allMessages.isEmpty()) return new Message[]{};
        return listToArray(allMessages);
    }

    public void updateMessageFile(String userEmailAddress, Message[] eMail, String fileName) throws IOException {
        String filePath = this.path + userEmailAddress + "\\" +fileName+".json";
        String jsonEmail = new Gson().toJson(eMail);
        writer(filePath, jsonEmail);
    }
    public void removeMessageFromFile(String userEmailAddress, Message message,String fileName) throws IOException {
        Message[] messages = readMessageFile(userEmailAddress, fileName);

        List<Message> messagesModified = new ArrayList<>();
        Collections.addAll(messagesModified, messages);
        messagesModified.remove(message);

        updateMessageFile(userEmailAddress, listToArray(messagesModified), fileName);
    }
}
