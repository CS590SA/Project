package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature = "History", type = "class")
public interface IHistoryRetrive {

	public String retriveChatHistory(String sender);
}
