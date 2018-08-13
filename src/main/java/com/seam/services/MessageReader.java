package com.seam.services;

import com.seam.messages.pojo.Message;

public interface MessageReader {

    /**
     * Get the latest message of the given subscriber
     *
     * @param subscriberId the id of the subscriber whose message is to be found out
     * @return Latest message of the subscriber
     */
    Message getLatestMessage(String subscriberId);

    /**
     * Get the first unread message of the given subscriber
     *
     * @param subscriberId the id of the subscriber whose message is to be found out
     * @return First unread message of the subscriber
     */
    Message getFirstUnreadMessage(String subscriberId);
}
