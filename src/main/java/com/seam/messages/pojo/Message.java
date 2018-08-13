package com.seam.messages.pojo;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * The message object class
 */
public class Message {

    public String senderId;

    public String body;

    public List<String> attachmentIds;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss.SSSZ",timezone="GMT+0530")
    public Date createdAt;

    public JsonNode readAt; //Unread Message will have value as 0 and the key is the reciever id

}
