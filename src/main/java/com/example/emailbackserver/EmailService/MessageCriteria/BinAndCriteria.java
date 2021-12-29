package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;

import java.util.ArrayList;
import java.util.List;

public class BinAndCriteria implements MessageCriteria{
    private MessageCriteria criteria1;
    private MessageCriteria criteria2;

    public BinAndCriteria(MessageCriteria criteria1, MessageCriteria criteria2) {
        this.criteria1 = criteria1;
        this.criteria2 = criteria2;
    }

    public void setDomain(Message[] messages){}
    @Override
    public Message[] meetCriteria() throws CloneNotSupportedException {
        Message[] filtered1st = criteria1.meetCriteria();
        criteria2.setDomain(filtered1st);
        return criteria2.meetCriteria();
    }

}
