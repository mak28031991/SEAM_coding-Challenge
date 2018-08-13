package com.seam.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seam.messages.pojo.Message;
import com.seam.services.MessageReader;
import com.seam.services.impl.MessageReaderImpl;

/**
 * The controller to get the messages
 */
@RestController
@RequestMapping(value = "/messages")
public class MessageReaderController {

    @Autowired
    private MessageReader messageReader;

    @RequestMapping(value = "/latest", method = RequestMethod.GET)
    public Message getLatestMessage(@RequestParam("subscriberId") String subscriberId) {
    	System.out.println(subscriberId);
    	messageReader = new MessageReaderImpl();
        return messageReader.getLatestMessage(subscriberId);
    }

    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    public Message getFirstUnreadMessage(@RequestParam("subscriberId") String subscriberId) {
    	messageReader = new MessageReaderImpl();
        return messageReader.getFirstUnreadMessage(subscriberId);
    }
}
