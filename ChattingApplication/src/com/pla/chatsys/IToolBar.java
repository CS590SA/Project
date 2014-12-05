package com.pla.chatsys;

//mandatory
public interface IToolBar {

	public void sendIncognitoMessage(String sender, String msg);

	public void sendColor(String sender, int color);

	public void sendLocation(double lat, double lon);
}
