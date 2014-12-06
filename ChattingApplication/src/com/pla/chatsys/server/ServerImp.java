package com.pla.chatsys.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.pla.chatsys.annotation.ChattingAnnotation;

public class ServerImp implements IServerImp {
	private ServerArch _arch;
	public ServerImp() {}
	public void setArch(ServerArch arch) {_arch = arch;}
	public ServerArch getArch() {return _arch;}
	
	/*
	 * Myx Lifecycle Methods: these methods are called automatically by the
	 * framework as the bricks are created, attached, detached, and destroyed
	 * respectively.
	 */
	public void init() {}
	public void begin() {}
	public void end() {}
	public void destroy() {}

	/*
	 * Mandatory implementation primitives required by the architecture
	 */

	// missing override? i think doesn't matter if we're making it mandatory anyway
	public void sendMessage(String sender, String message) {
		

		if (_arch.OUT_IChatListener != null) {
			_arch.OUT_IChatListener.messageSent(sender, message);
			
			@ChattingAnnotation(feature="Attachment", type="property")
			boolean writeMsg = _arch.OUT_IFileWriter.writeMsg(sender, message);
		}
	}

	// mandatory
	@Override
	public void sendTemplet(String sender, String msgCode) {
		_arch.OUT_ITempletSend.sendTemplet(sender, msgCode);
	}

	/*
	 * Annotated implementation primitives required by the architecture
	 */

	@ChattingAnnotation(feature = "History", type = "method")
	@Override
	public String retriveChatHistory(String sender) {
		String sCurrentLine = null;
		String Allfile = null;
		BufferedReader br = null;
		try {
			File file = new File("./ChatHistory.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			br = new BufferedReader(new FileReader("./ChatHistory.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				if (Allfile == null) {
					Allfile = sCurrentLine;
				} else {
					Allfile = Allfile + "\n" + sCurrentLine;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return Allfile;
	}

	@ChattingAnnotation(feature = "Attachment", type = "method")
	@Override
	public void sendFile(String sender, String path, byte[] fileData) {
		if (_arch.OUT_IFileSentListener != null) {
			_arch.OUT_IFileSentListener.fileSent(sender, path, fileData);
		}
	}

	@ChattingAnnotation(feature = "Image", type = "method")
	@Override
	public void sendImage(String sender, String name, byte[] data) {

		if (_arch.OUT_IImageSent != null) {
			_arch.OUT_IImageSent.imageSent(sender, name, data);
		}
	}

	@ChattingAnnotation(feature = "Game", type = "method")
	@Override
	public void startGame() {
		if (_arch.OUT_IGameListener != null) {
			_arch.OUT_IGameListener.gameStarted();
		}
	}

	@ChattingAnnotation(feature = "Game", type = "method")
	@Override
	public void endGame() {
		if (_arch.OUT_IGameListener != null) {
			_arch.OUT_IGameListener.gameEnded();
		}
	}

	@ChattingAnnotation(feature = "Game", type = "method")
	@Override
	public void play(String sender, int position) {
		if (_arch.OUT_IGameListener != null) {
			_arch.OUT_IGameListener.played(sender, position);
		}
	}

	@ChattingAnnotation(feature = "Incognito", type = "method")
	@Override
	public void sendIncognitoMessage(String sender, String msg) {
		if (_arch.OUT_IChatListener != null) {
			_arch.OUT_IChatListener.messageSent(sender, msg);
		}
	}

	@ChattingAnnotation(feature = "BGColor", type = "method")
	@Override
	public void sendColor(String sender, int color) {
		if (_arch.OUT_IToolBarEvent != null) {
			_arch.OUT_IToolBarEvent.onColorReceived(color);
		}
	}

	@ChattingAnnotation(feature = "Map", type = "method")
	@Override
	public void sendLocation(double lat, double lon) {
		if (_arch.OUT_IToolBarEvent != null) {
			_arch.OUT_IToolBarEvent.onLocationReceived(lat, lon);
		}
	}
}