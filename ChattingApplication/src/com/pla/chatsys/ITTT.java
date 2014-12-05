package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature="Game", type="class")
public interface ITTT {
	
	public void startTTT(int x, int y);
	
	public void endTTT();
	
	public void opponentMove(int position);

}
