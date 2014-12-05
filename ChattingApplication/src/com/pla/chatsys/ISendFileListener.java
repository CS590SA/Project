package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature="Attachment", type="class")
public interface ISendFileListener {

	public void sendFile(String sender, String path, byte[] fileData);

}
