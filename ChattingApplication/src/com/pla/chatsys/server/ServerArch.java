package com.pla.chatsys.server;

import com.pla.chatsys.IChat;
import com.pla.chatsys.IChatListener;
import com.pla.chatsys.IFileSentListener;
import com.pla.chatsys.IFileWriter;
import com.pla.chatsys.IGame;
import com.pla.chatsys.IGameListener;
import com.pla.chatsys.IHistoryRetrive;
import com.pla.chatsys.IImageSent;
import com.pla.chatsys.ISendFileListener;
import com.pla.chatsys.ISendImage;
import com.pla.chatsys.ISendTemplet;
import com.pla.chatsys.ITempletSend;
import com.pla.chatsys.IToolBar;
import com.pla.chatsys.IToolBarEvent;
import com.pla.chatsys.annotation.ChattingAnnotation;

import edu.uci.isr.myx.fw.AbstractMyxSimpleBrick;
import edu.uci.isr.myx.fw.IMyxName;
import edu.uci.isr.myx.fw.MyxUtils;

//how about all these extensions? processor will leave an extension or something like 'IGame,,IToolBar'
public class ServerArch extends AbstractMyxSimpleBrick implements IChat,
		IHistoryRetrive, ISendFileListener, ISendImage, IGame, ISendTemplet,
		IToolBar {
	
	// mandatory properties
	public static final IMyxName msg_IChat = MyxUtils.createName("com.pla.chatsys.IChat");
	public static final IMyxName msg_IChatListener = MyxUtils.createName("com.pla.chatsys.IChatListener");
	public static final IMyxName msg_ISendTemplet = MyxUtils.createName("com.pla.chatsys.ISendTemplet");
	public static final IMyxName msg_ITempletSend = MyxUtils.createName("com.pla.chatsys.ITempletSend");
	public static final IMyxName msg_IToolBar = MyxUtils.createName("com.pla.chatsys.IToolBar");
	public static final IMyxName msg_IToolBarEvent = MyxUtils.createName("com.pla.chatsys.IToolBarEvent");

	// annotated properties (myx)
	@ChattingAnnotation(feature = "Attachment", type = "property")
	public static final IMyxName msg_IFileWriter = MyxUtils.createName("com.pla.chatsys.IFileWriter");
	@ChattingAnnotation(feature = "History", type = "property")
	public static final IMyxName msg_IHistoryRetrive = MyxUtils.createName("com.pla.chatsys.IHistoryRetrive");
	@ChattingAnnotation(feature = "Attachment", type = "property")
	public static final IMyxName msg_ISendFileListener = MyxUtils.createName("com.pla.chatsys.ISendFileListener");
	@ChattingAnnotation(feature = "Attachment", type = "property")
	public static final IMyxName msg_IFileSentListener = MyxUtils.createName("com.pla.chatsys.IFileSentListener");
	@ChattingAnnotation(feature = "Image", type = "property")
	public static final IMyxName msg_ISendImage = MyxUtils.createName("com.pla.chatsys.ISendImage");
	@ChattingAnnotation(feature = "Image", type = "property")
	public static final IMyxName msg_IImageSent = MyxUtils.createName("com.pla.chatsys.IImageSent");
	@ChattingAnnotation(feature = "Game", type = "property")
	public static final IMyxName msg_IGame = MyxUtils.createName("com.pla.chatsys.IGame");
	@ChattingAnnotation(feature = "Game", type = "property")
	public static final IMyxName msg_IGameListener = MyxUtils.createName("com.pla.chatsys.IGameListener");

	// mandatory
	public IChatListener OUT_IChatListener;
	public ITempletSend OUT_ITempletSend;
	public IToolBarEvent OUT_IToolBarEvent;

	// annotated properties
	@ChattingAnnotation(feature = "Attachment", type = "property")
	public IFileWriter OUT_IFileWriter;
	@ChattingAnnotation(feature = "Attachment", type = "property")
	public IFileSentListener OUT_IFileSentListener;
	@ChattingAnnotation(feature = "Image", type = "property")
	public IImageSent OUT_IImageSent;
	@ChattingAnnotation(feature = "Game", type = "property")
	public IGameListener OUT_IGameListener;

	private IServerImp _imp;

	public ServerArch() {
		_imp = getImplementation();
		if (_imp != null) {
			_imp.setArch(this);
		} else {
			System.exit(1);
		}
	}

	protected IServerImp getImplementation() {
		try {
			return new ServerImp();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	public void init() {
		_imp.init();
	}

	// probably need to move each of these into their own methods and annotate
	// them accordingly
	// maybe better: recognize if statements like method, eliminate from { to }
	public void begin() {
		OUT_IChatListener = (IChatListener) MyxUtils.getFirstRequiredServiceObject(this, msg_IChatListener);
		if (OUT_IChatListener == null) {
			System.err.println("Error: Interface com.pla.chatsys.IChatListener returned null");
			return;
		}
		OUT_IFileWriter = (IFileWriter) MyxUtils.getFirstRequiredServiceObject(this, msg_IFileWriter);
		if (OUT_IFileWriter == null) {
			System.err.println("Error: Interface com.pla.chatsys.IFileWriter returned null");
			return;
		}
		OUT_IFileSentListener = (IFileSentListener) MyxUtils.getFirstRequiredServiceObject(this, msg_IFileSentListener);
		if (OUT_IFileSentListener == null) {
			System.err.println("Error: Interface com.pla.chatsys.IFileSentListener returned null");
			return;
		}
		OUT_IImageSent = (IImageSent) MyxUtils.getFirstRequiredServiceObject(this, msg_IImageSent);
		if (OUT_IImageSent == null) {
			System.err.println("Error: Interface com.pla.chatsys.IImageSent returned null");
			return;
		}
		OUT_IGameListener = (IGameListener) MyxUtils.getFirstRequiredServiceObject(this, msg_IGameListener);
		if (OUT_IGameListener == null) {
			System.err.println("Error: Interface com.pla.chatsys.IGameListener returned null");
			return;
		}
		OUT_ITempletSend = (ITempletSend) MyxUtils.getFirstRequiredServiceObject(this, msg_ITempletSend);
		if (OUT_ITempletSend == null) {
			System.err.println("Error: Interface com.pla.chatsys.ITempletSend returned null");
			return;
		}
		OUT_IToolBarEvent = (IToolBarEvent) MyxUtils.getFirstRequiredServiceObject(this, msg_IToolBarEvent);
		if (OUT_IToolBarEvent == null) {
			System.err.println("Error: Interface com.pla.chatsys.IToolBarEvent returned null");
			return;
		}
		_imp.begin();
	}

	public void end() {
		_imp.end();
	}

	public void destroy() {
		_imp.destroy();
	}

	// same problem here with ifs
	public Object getServiceObject(IMyxName arg0) {
		if (arg0.equals(msg_IChat)) {
			return this;
		}
		if (arg0.equals(msg_IHistoryRetrive)) {
			return this;
		}
		if (arg0.equals(msg_ISendFileListener)) {
			return this;
		}
		if (arg0.equals(msg_ISendImage)) {
			return this;
		}
		if (arg0.equals(msg_IGame)) {
			return this;
		}
		if (arg0.equals(msg_ISendTemplet)) {
			return this;
		}
		if (arg0.equals(msg_IToolBar)) {
			return this;
		}
		return null;
	}

	//mandatory
	public void sendMessage(String sender, String message) {
		_imp.sendMessage(sender, message);
	}
	
	//mandatory
	public void sendTemplet(String sender, String msgCode) {
		_imp.sendTemplet(sender, msgCode);
	}

	@ChattingAnnotation(feature = "History", type = "method")
	public String retriveChatHistory(String sender) {
		return _imp.retriveChatHistory(sender);
	}

	@ChattingAnnotation(feature = "Attachment", type = "method")
	public void sendFile(String sender, String path, byte[] fileData) {
		_imp.sendFile(sender, path, fileData);
	}

	@ChattingAnnotation(feature = "Image", type = "method")
	public void sendImage(String sender, String path, byte[] data) {
		_imp.sendImage(sender, path, data);
	}

	@ChattingAnnotation(feature = "Game", type = "method")
	public void startGame() {
		_imp.startGame();
	}

	@ChattingAnnotation(feature = "Game", type = "method")
	public void endGame() {
		_imp.endGame();
	}

	@ChattingAnnotation(feature = "Game", type = "method")
	public void play(String sender, int position) {
		_imp.play(sender, position);
	}
	
    @ChattingAnnotation(feature="Incognito", type="method")
	public void sendIncognitoMessage(String sender, String msg) {
		_imp.sendIncognitoMessage(sender, msg);
	}
    
    @ChattingAnnotation(feature="BGColor", type="method")
	public void sendColor(String sender, int color) {
		_imp.sendColor(sender, color);
	}
    
    @ChattingAnnotation(feature="Map", type="method")
	public void sendLocation(double lat, double lon) {
		_imp.sendLocation(lat, lon);
	}
}