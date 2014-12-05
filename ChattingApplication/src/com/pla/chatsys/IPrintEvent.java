package com.pla.chatsys;

import com.pla.chatsys.annotation.ChattingAnnotation;

@ChattingAnnotation(feature = "Print", type = "class")
public interface IPrintEvent {

	public void print(String msg);
}
