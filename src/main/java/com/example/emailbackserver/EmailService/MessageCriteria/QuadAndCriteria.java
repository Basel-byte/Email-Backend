package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;

import java.util.ArrayList;
import java.util.List;

public class QuadAndCriteria implements MessageCriteria{
    private MessageCriteria criteria1;
    private MessageCriteria criteria2;
    private MessageCriteria criteria3;
    private MessageCriteria criteria4;

    public QuadAndCriteria(MessageCriteria criteria1, MessageCriteria criteria2,
                           MessageCriteria criteria3, MessageCriteria criteria4) {
        this.criteria1 = criteria1;
        this.criteria2 = criteria2;
        this.criteria3 = criteria3;
        this.criteria4 = criteria4;
    }
    @Override
    public void setDomain(Message[] messages) {}

    @Override
    public Message[] meetCriteria() throws CloneNotSupportedException {
        Message[] filtered1st = criteria1.meetCriteria();
        criteria2.setDomain(filtered1st);
        filtered1st = criteria2.meetCriteria();
        criteria3.setDomain(filtered1st);
        filtered1st =  criteria3.meetCriteria();
        criteria4.setDomain(filtered1st);
        return criteria4.meetCriteria();
    }
}