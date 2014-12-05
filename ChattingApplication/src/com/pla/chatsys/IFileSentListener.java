package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature="Attachment", type="class")
public interface IFileSentListener {
	
	public void fileSent(String sender,String fileName,byte[] fileData);

}
