package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature = "Image", type = "class")
public interface IImageSent {

	public void imageSent(String sender, String imageName, byte[] imageData);
}
