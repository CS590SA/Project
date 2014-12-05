package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature="Image", type="class")
public interface ISendImage {

	public void sendImage(String sender,String path,byte[] data);
}
