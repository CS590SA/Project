package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature = "Attachment", type = "class")
public interface IFileWriter {

	public boolean writeMsg(String sender, String message);

}
