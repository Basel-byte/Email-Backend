package com.example.emailbackserver.EmailService;

import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;

@Service
public class LoggerService {

    private final String usersFilePath = "D:\\IntelliJ Projects\\emailBackServer\\users\\UsersData.json";
    private Object object;
    private JSONArray jsonArray;

    public LoggerService() {
        this.object = null;
        this.jsonArray = null;
    }

    public String logINCheck(String email, String password){

        this.jsonArray=readFile();
        if(this.jsonArray == null) return "error log in empty this email is not found";

        for (Object o : this.jsonArray) {
            JSONObject userObject = (JSONObject) o;
            if (userObject.get("emailAddress").equals(email)) {
                if (userObject.get("password").equals(password)){
                    if(userObject.get("loggedIn").equals(true)) return "error log in User is Already Logged In";
                    else{
                        userObject.put("loggedIn",true);
                        writeFile(this.jsonArray);
                        return "Welcome Back " + userObject.get("name");
                    }
                }
                else return "error log in Incorrect Password";
            }
        }
        return "error log in Email is not found Please SignUp";
    }


    public String logOutCheck(String email, String password){

        this.jsonArray = readFile();
        if(this.jsonArray == null) return "error log out empty this email is not found";//will not happen

        for(Object o : this.jsonArray){
            JSONObject userObject = (JSONObject) o;
            if(userObject.get("emailAddress").equals(email)){
                if(userObject.get("password").equals(password)){
                    if(userObject.get("loggedIn").equals(true)){
                        userObject.put("loggedIn",false);
                        writeFile(this.jsonArray);
                        return "Logged Out";
                    }
                    else return "error lo out User has not logged in";
                }
                else return "error log out Invalid Password"; // will not happen
            }
        }
        return "error log out this email is not found";
    }


    public JSONArray readFile(){

        try{
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(usersFilePath);
            this.object = parser.parse(reader);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        if(object != null)
            return (JSONArray) object;

        return null;

    }

    public void writeFile(JSONArray jsonArray){

        Gson gson = new Gson();

        try{
            FileWriter file = new FileWriter(usersFilePath);
            file.write(gson.toJson(jsonArray));
            file.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}


