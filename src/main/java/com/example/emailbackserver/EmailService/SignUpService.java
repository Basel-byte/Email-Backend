package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Objects;

@Service
public class SignUpService {
    private String usersDataFilePath;
    private String eachUserPath;
    private User[] currentUsers;

    public SignUpService() throws IOException {
        this.usersDataFilePath = "D:\\IntelliJ Projects\\emailBackServer\\users";
        createFile(usersDataFilePath, "directory");

        this.usersDataFilePath = "D:\\IntelliJ Projects\\emailBackServer\\users\\UsersData.json";
        createFile(usersDataFilePath, "file");

        this.eachUserPath = "D:\\IntelliJ Projects\\emailBackServer\\users\\";
        currentUsers = new User[0];
    }

    private void createFile(String path, String type) throws IOException {
        if(!checkFileExistence(path)){
            File file = new File(path);
            if(type == "file") file.createNewFile();
            else if(type == "directory") file.mkdir();
        }
    }
    private boolean checkFileExistence(String path){
        File file = new File(path);
        return file.exists();
    }

    private User[] loadUsersData() throws IOException, ParseException {
        File file = new File(usersDataFilePath);
        FileReader fileReader = new FileReader(usersDataFilePath);
        JSONParser jsonParser = new JSONParser();
        Object read;
        try {
            read = jsonParser.parse(fileReader);
        }
        catch (Exception e){
            read = "";
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(read instanceof JSONObject){
            jsonObject = (JSONObject) read;
            currentUsers = new User[] { new Gson().fromJson(jsonObject.toJSONString(), User.class) };
        }
        else if(read instanceof JSONArray){
            jsonArray = (JSONArray) read;
            GsonBuilder gsonBuilder = new GsonBuilder();
            currentUsers = gsonBuilder.create().fromJson(jsonArray.toJSONString(), User[].class);
        }
        else currentUsers = new User[0];
        return currentUsers;
    }
    private User deserialize(String newUser){
        return new Gson().fromJson(newUser, User.class);
    }

    private void register(User newUser, User[] currentUsers) throws CloneNotSupportedException, IOException {
        User[] currentUserModified = new User[currentUsers.length + 1];
        for(int i = 0; i < currentUsers.length; i++){
            currentUserModified[i] = currentUsers[i].clone();
        }
        currentUserModified[currentUsers.length] = newUser.clone();
        String currentUsersJson = new Gson().toJson(currentUserModified);

        File file = new File(usersDataFilePath);
        FileWriter writer = new FileWriter(file);
        writer.write(currentUsersJson);
        writer.flush();
        writer.close();

        createFile(eachUserPath+newUser.getEmailAddress(),"directory");
        String eachPath = eachUserPath;
        eachUserPath += newUser.getEmailAddress() + "\\";
        createFile(eachUserPath + "Inbox.json", "file");
        createFile(eachUserPath + "Sent.json", "file");
        createFile(eachUserPath + "Starred.json", "file");
        createFile(eachUserPath + "Important.json", "file");
        createFile(eachUserPath + "Draft.json", "file");
        createFile(eachUserPath + "Trashed.json", "file");
        createFile(eachUserPath + "Contacts.json", "file");
        createFile(eachUserPath + "Custom.json", "file");
        eachUserPath = eachPath;
    }
    public boolean isRegistered(String newUser) throws IOException, ParseException, CloneNotSupportedException {
        boolean repeated = false;
        User parsedNewUser = deserialize(newUser);
        currentUsers = loadUsersData();
        for(int i = 0; i < currentUsers.length; i++){
            System.out.println("stored:" + currentUsers[i].getEmailAddress()+"...");
            System.out.println("new:" + parsedNewUser.getEmailAddress()+"...");
              if(Objects.equals(currentUsers[i].getEmailAddress(), parsedNewUser.getEmailAddress()))
                  repeated = true;
        }
        System.out.println("repeated: " + repeated + "...");
        if(!repeated) register(parsedNewUser, currentUsers);
        return repeated;
    }
}
