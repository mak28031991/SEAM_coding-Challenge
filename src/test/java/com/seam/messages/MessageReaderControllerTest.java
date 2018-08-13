package com.seam.messages;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.seam.messages.pojo.Message;
import com.seam.messages.utils.MapperUtils;
import com.seam.services.impl.MessageReaderImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageReaderController.class)
public class MessageReaderControllerTest {

	@Autowired
    private MockMvc mockMvc;

	@MockBean
    private MessageReaderImpl service;
	
	@Test
	public void testLatestMessageForInvalidSubscriber() {
		String subscriberId ="9020be9e-958a-4536-8164-4a8bbc17c";
		assertNull(service.getLatestMessage(subscriberId));
	}
	
	@Test
	public void testLatestUnreadMessageForInvalidSubscriber() {
		String subscriberId ="9020be9e-958a-4536-8164-4a8bb";
		assertNull(service.getFirstUnreadMessage(subscriberId));
	}
	
	@Test
	public void testLatestMessage() throws Exception {
		String subscriberId ="9020be9e-958a-4536-8164-4a8bbc17c3da";
		File file = ResourceUtils.getFile("classpath:db/18-06-18/msg7");
		assertTrue(file.exists());
		JsonNode messageNode = JsonLoader.fromFile(file);
		assertNotNull(messageNode);
        Message message = MapperUtils.getMapper().convertValue(messageNode, Message.class);
		assertNotNull(message);
        when(service.getLatestMessage(subscriberId)).thenReturn(message);
        this.mockMvc.perform(get("/messages/latest?subscriberId="+subscriberId)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString(messageNode.asText())));
	}
	
	@Test
	public void testLatestUnreadMessage() throws Exception{
		String subscriberId ="e8549d50-31ec-451d-a9ba-44e13549ddb1";
		File file = ResourceUtils.getFile("classpath:db/18-06-18/msg7");
		assertTrue(file.exists());
		JsonNode messageNode = JsonLoader.fromFile(file);
		assertNotNull(messageNode);
        Message message = MapperUtils.getMapper().convertValue(messageNode, Message.class);
		assertNotNull(message);
        when(service.getLatestMessage(subscriberId)).thenReturn(message);
        this.mockMvc.perform(get("/messages/unread?subscriberId="+subscriberId)).andDo(print()).andExpect(status().isOk())
        .andExpect(content().string(containsString(messageNode.asText())));
	}

}
