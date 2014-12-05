package com.pla.chatsys.server;

import com.pla.chatsys.annotation.ChattingAnnotation;

public interface IServerImp {

	/*
	 * Getter and Setter of architecture reference
	 */
	public void setArch(ServerArch arch);

	public ServerArch getArch();

	/*
	 * Myx Lifecycle Methods: these methods are called automatically by the
	 * framework as the bricks are created, attached, detached, and destroyed
	 * respectively.
	 */
	public void init();

	public void begin();

	public void end();

	public void destroy();

	/*
	 * Implementation primitives required by the architecture
	 */

	public void sendMessage(String sender, String message);

	public void sendTemplet(String sender, String msgCode);
	
	@ChattingAnnotation(type = "History", feature = "property")
	public String retriveChatHistory(String sender);

	@ChattingAnnotation(type = "Attachment", feature = "property")
	public void sendFile(String sender, String path, byte[] fileData);

	@ChattingAnnotation(type = "Image", feature = "property")
	public void sendImage(String sender, String path, byte[] data);

	@ChattingAnnotation(type = "Game", feature = "property")
	public void startGame();

    @ChattingAnnotation(type="Game", feature="property")
	public void endGame();

    @ChattingAnnotation(type="Game", feature="property")
	public void play(String sender, int position);

    @ChattingAnnotation(type="Incognito", feature="property")
	public void sendIncognitoMessage(String sender, String msg);

    @ChattingAnnotation(type="BGColor", feature="property")
	public void sendColor(String sender, int color);

    @ChattingAnnotation(type="Map", feature="property")
	public void sendLocation(double lat, double lon);
}