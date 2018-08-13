package com.seam.services.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.seam.messages.pojo.Message;
import com.seam.messages.utils.MapperUtils;
import com.seam.services.MessageReader;

@Service
public class MessageReaderImpl implements MessageReader {

    @Override
    public Message getLatestMessage(String subscriberId) {
        try {
            // Read the message folder
            File folder = ResourceUtils.getFile("classpath:db");
            File[] listOfFiles = folder.listFiles();
            
            //sort the files in db folder, ordered by modified date in decreasing order        
            Arrays.sort(listOfFiles, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified());
                }
            });
            
            Date latestTime = new Date(Long.MIN_VALUE); // setting the latest date to dinosaur's age
            Message latestMessage = null;  
            //iterate through all folders and files in folder db in decreasing order of date
            //latest message can be sent by this subscriber 
            //or received by this subscriber
            //In case subscriber is receiver, it does not matter if the lastest message is read or unread  
            //that message will always be latest)
            for (File file : listOfFiles) {            	
                if (file.isFile()) {
                    // Get the message node from the file
                    JsonNode messageNode = JsonLoader.fromFile(file);

                    // Convert the message node to the corresponding object
                    Message message = MapperUtils.getMapper().convertValue(messageNode, Message.class);
                    
                    if(message.senderId.equals(subscriberId) || message.readAt.has(subscriberId))
                    {
                    	if(message.createdAt.after(latestTime))
                    	{
                    		latestMessage =  message;
                    		latestTime = message.createdAt;
                    	}
                    }                                        
                }else if(file.isDirectory())
                {
                	// this is a directory so, all files under that directory will be traversed
                	File[] filesInDirectory =file.listFiles();
                	//sort the files in db folder, ordered by modified date in decreasing order        
                    Arrays.sort(filesInDirectory, new Comparator<File>() {
                        public int compare(File f1, File f2) {
                            return Long.compare(f2.lastModified(), f1.lastModified());
                        }
                    });                    
                	for (File fileInDirectory : filesInDirectory) { 
                		// Get the message node from the file
                        JsonNode messageNode = JsonLoader.fromFile(fileInDirectory);
                        	
                        // Convert the message node to the corresponding object
                        Message message = MapperUtils.getMapper().convertValue(messageNode, Message.class);
                        
                       if(message.senderId.equals(subscriberId) || message.readAt.has(subscriberId))
                        {
                        	if(message.createdAt.after(latestTime))
                        	{
                        		latestMessage =  message;
                        		latestTime = message.createdAt;
                        	}
                        }
                        
                	}
                }
            }
         // Return the latest message
            
            return latestMessage;
            
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /*
     *@param subscriberId string id of subscriber
     *@return latest unread message or first unread message in list of unread message   
     */
    @Override
    public Message getFirstUnreadMessage(String subscriberId) {
        try {
            // Read the message folder
            File folder = ResourceUtils.getFile("classpath:db");
            File[] listOfFiles = folder.listFiles();

            //sort the files in db folder, ordered by modified date in decreasing order        
            Arrays.sort(listOfFiles, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.compare(f2.lastModified(), f1.lastModified());
                }
            });
            
            Date latestTime = new Date(Long.MIN_VALUE); // setting the latest date to dinosaur's age
            Message firstUnreadMessage = null;  
            //iterate through all folders and files in folder db in decreasing order of date
            //check if message is unread and current message is latest than previous unread message
            for (File file : listOfFiles) {            	
                if (file.isFile()) {
                    // Get the message node from the file
                    JsonNode messageNode = JsonLoader.fromFile(file);

                    // Convert the message node to the corresponding object
                    Message message = MapperUtils.getMapper().convertValue(messageNode, Message.class);
                    
                    if(message.readAt.has(subscriberId) && message.readAt.get(subscriberId).toString().equals("0"))
                    {
                    	if(message.createdAt.after(latestTime))
                    	{
                    		firstUnreadMessage =  message;
                    		latestTime = message.createdAt;
                    	}
                    }                                        
                }else if(file.isDirectory())
                {
                	// this is a directory so, all files under that directory will be traversed
                	File[] filesInDirectory =file.listFiles();
                	//sort the files in db folder, ordered by modified date in decreasing order        
                    Arrays.sort(filesInDirectory, new Comparator<File>() {
                        public int compare(File f1, File f2) {
                            return Long.compare(f2.lastModified(), f1.lastModified());
                        }
                    });                    
                	for (File fileInDirectory : filesInDirectory) { 
                		// Get the message node from the file
                        JsonNode messageNode = JsonLoader.fromFile(fileInDirectory);
                        	
                        // Convert the message node to the corresponding object
                        Message message = MapperUtils.getMapper().convertValue(messageNode, Message.class);
                        
                        if(message.readAt.has(subscriberId) && message.readAt.get(subscriberId).asText().equals("0"))
                        {
                        	if(message.createdAt.after(latestTime))
                        	{
                        		firstUnreadMessage =  message;
                        		latestTime = message.createdAt;
                        	}
                        }
                        
                	}
                }
            }
         // Return first unread message            
            return firstUnreadMessage;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

}
