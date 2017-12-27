package com.aghit.task.manager;

import org.springframework.context.support.ResourceBundleMessageSource;

import com.aghit.base.MessageService;
import com.aghit.base.impl.MessageServiceImpl;

public class MsgServiceFactory {

	
	public static MessageService getMessageService(){
		
		MessageServiceImpl m=new MessageServiceImpl();
		ResourceBundleMessageSource messages = new ResourceBundleMessageSource();
		messages.setBasename("ApplicationResources");
		m.setMessages(messages);
		
		return m;
	}
}
