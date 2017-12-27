package com.aghit.base.impl;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import com.aghit.base.MessageService;

/**
 * @author xiaowang
 *
 */
@Service("messageServiceImpl")
public class MessageServiceImpl implements MessageService {

	private static Logger logger = Logger.getLogger(MessageServiceImpl.class);
	
	@Resource(name = "messageSource")
	private MessageSource messages;

	/**
	 * 获取信息，当前设置为中文，以后可根据设置来读取
	 * 
	 * @param key
	 * @param args
	 * @return String
	 */
	@Override
	public String getMessage(String key, Object[] args) {
		return this.getMessage(key, args, Locale.CHINA);
	}

	/**
	 * 获取信息
	 * 
	 * @param key
	 * @param args
	 * @param lc
	 * @return String
	 */
	@Override
	public String getMessage(String key, Object[] args, Locale lc) {

		String ms = "";
		try {
			ms = messages.getMessage(key, args, lc);
		} catch (NoSuchMessageException e) {
			logger.error(e.getMessage());
		}
		return ms;
	}

	public MessageSource getMessages() {
		return messages;
	}

	public void setMessages(MessageSource messages) {
		this.messages = messages;
	}
}