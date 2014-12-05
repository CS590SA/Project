package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature="Game", type="class")
public interface ITTTListener {

	public void myMove(int position);
	
	public void quit();
	
}
