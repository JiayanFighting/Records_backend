package com.demo.records.service;

import com.demo.records.domain.MessageDO;

import java.util.List;

public interface MessageService {
    int readMessage(int id);
    int deleteMessage(int id);
    int createMessage(MessageDO message);
    MessageDO getMessage(int id);
    List<MessageDO> getMessageList(String userEmail);
}
