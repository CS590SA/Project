package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature = "Game", type = "class")
public interface IGame {

	public void startGame();

	public void endGame();

	public void play(String sender, int position);

}
