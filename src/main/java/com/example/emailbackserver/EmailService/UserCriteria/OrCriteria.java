package com.example.emailbackserver.EmailService.UserCriteria;

import com.example.emailbackserver.EmailModel.User;

import java.util.Arrays;

public class OrCriteria implements UserCriteria{
    private String FilterName;
    private String FilterEmail;
    private UserCriteria criteriaName;
    private UserCriteria criteriaEmail;

    public OrCriteria(String filterEmail, String filterName) {
        FilterName = filterName;
        FilterEmail = filterEmail;
        this.criteriaName = new NameCriteria(FilterName);
        this.criteriaEmail = new EmailCriteria(FilterEmail);
    }

    public String getFilterName() {
        return FilterName;
    }

    public void setFilterName(String filterName) {
        FilterName = filterName;
    }

    public String getFilterEmail() {
        return FilterEmail;
    }

    public void setFilterEmail(String filterEmail) {
        FilterEmail = filterEmail;
    }

    @Override
    public User[] filter(User[] users) {
        try {
            User[] FilteredName = this.criteriaName.filter(users);
            User[] FilteredEmail = this.criteriaEmail.filter(users);
            boolean foundFlag = false;
            for (int i = 0; i < FilteredName.length; i++) {
                for (int j = 0; j < FilteredEmail.length; j++) {
                    if (!FilteredEmail[j].getEmailAddress().equals(FilteredName[i].getEmailAddress())) {
                        FilteredEmail = Arrays.copyOf(FilteredEmail,FilteredEmail.length+1);
                        FilteredEmail[FilteredEmail.length - 1] = FilteredName[i];
                        break;
                    }
                }
            }
            return FilteredEmail;
        }
        catch (Exception e){
            return null;
        }
    }
}
