package com.example.emailbackserver.EmailController;

import com.example.emailbackserver.EmailModel.User;
import com.google.gson.*;

import java.lang.reflect.Type;

public class userLoggedAdapter implements JsonDeserializer<User> {

    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jObject = jsonElement.getAsJsonObject();
        String emailAddress = jObject.get("emailAddress").getAsString();
        String password = jObject.get("password").getAsString();
        return new User(emailAddress, password);
    }
}
