package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature = "Game", type = "class")
public interface IGameListener {

	public void gameStarted();

	public void gameEnded();

	public void played(String sender, int position);

}
