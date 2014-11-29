package com.pla.chatsys.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.pla.chatsys.annotation.ChattingAnnotation;

import comp.Templet.TemplateGUI;
import comp.Templet.TemplateGUI.SelectInterfce;

public class ClientImp extends JFrame implements ActionListener, IClientImp {

	//mandatory properties
	JFrame f = new JFrame();
	JButton btnSend;
	JEditorPane tvTranscript = new JEditorPane("text/html", "");
	private static final long serialVersionUID = -4658058460358555306L;
	private ClientArch _arch;
	public static String IconPath = "icons/";
	public int flag = 0;
	StringBuffer transcriptBuf;
	
	//annotated properties
	@ChattingAnnotation(feature="Attachment", type="property")
	private static String filePath = System.getProperty("user.home") + File.separator + "Temp" + File.separator;
	@ChattingAnnotation(feature="Texting", type="property")
	JTextField entryField;
	@ChattingAnnotation(feature="Templates", type="property")
	JButton btnTemplates;
	@ChattingAnnotation(feature="History", type="property")
	JButton btnHistory;
	@ChattingAnnotation(feature="Map", type="property")
	JButton btnMap;
	@ChattingAnnotation(feature = "Game", type = "property")
	JButton btnGame;
	@ChattingAnnotation(feature = "Image", type = "property")
	JButton btnImage;
	@ChattingAnnotation(feature = "Print", type = "property")
	JButton btnPrint;
	@ChattingAnnotation(feature="Incognito", type="property")
	JButton btnIncognito;
	@ChattingAnnotation(feature = "Attachment", type = "property")
	JButton btnAttachment;
	@ChattingAnnotation(feature="BGColor", type="property")
	JButton btnBGColor;
	@ChattingAnnotation(feature="Settings", type="property")
	JButton btnSettings;
	@ChattingAnnotation(feature="Availability", type="property")
	DefaultComboBoxModel model = new DefaultComboBoxModel();
	@ChattingAnnotation(feature="Availability", type="property")
	JComboBox comboBox = new JComboBox(model);

	public ClientImp() {}
	public void setArch(ClientArch arch) { _arch = arch; }
	public ClientArch getArch() { return _arch; }
	/*
	 * Myx Lifecycle Methods: these methods are called automatically by the
	 * framework as the bricks are created, attached, detached, and destroyed
	 * respectively.
	 */
	public void init() {}
	public void begin() {
		transcriptBuf = new StringBuffer();
		entryField = new JTextField(20);
		btnSend = new JButton("Send");
		btnTemplates = new JButton("Templates");
		btnGame = new JButton(new ImageIcon(IconPath + "TTT.png"));
		btnGame.setBackground(Color.WHITE);
		btnImage = new JButton("Send Image");
		btnPrint = new JButton(new ImageIcon(IconPath + "Print.png"));
		btnPrint.setBackground(Color.WHITE);
		btnIncognito = new JButton(new ImageIcon(IconPath + "Private.png"));
		btnIncognito.setBackground(Color.WHITE);
		btnMap = new JButton(new ImageIcon(IconPath + "Track.png"));
		btnMap.setBackground(Color.WHITE);
		btnBGColor = new JButton(new ImageIcon(IconPath + "Color.png"));
		btnBGColor.setBackground(Color.WHITE);
		btnAttachment = new JButton("Send File");
		btnAttachment = new JButton(new ImageIcon(IconPath + "SendFile.png"));
		btnAttachment.setBackground(Color.WHITE);
		btnSettings = new JButton(new ImageIcon(IconPath + "Settings.png"));
		btnSettings.setBackground(Color.WHITE);
		btnHistory = new JButton("Chat History");
		tvTranscript.setEditable(false);
		btnSend.addActionListener(this);
		final JPopupMenu menu = new JPopupMenu();
		JMenuItem defaultLoc = new JMenuItem("Default Location");
		menu.add(defaultLoc);
		String title = "Chat Client " + _arch.getMyxBrickItems().getBrickName().toString().substring(42);
		f.setTitle(title);
		this.setTitle(title);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		JScrollPane scrollingArea = new JScrollPane(tvTranscript);
		centerPanel.add(scrollingArea, BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		bottomPanel.add(entryField);
		bottomPanel.add(btnSend);
		bottomPanel.add(btnHistory);
		bottomPanel.add(btnTemplates);
		bottomPanel.add(btnImage);
		model.addElement("Available");
		model.addElement("Do not Disturb");
		topPanel.add(btnSettings);
		topPanel.add(btnMap);
		topPanel.add(btnAttachment);
		topPanel.add(btnBGColor);
		topPanel.add(btnGame);
		topPanel.add(btnPrint);
		topPanel.add(btnIncognito);
		topPanel.add(comboBox);
		f.getContentPane().add("Center", centerPanel);
		f.getContentPane().add("South", bottomPanel);
		f.getContentPane().add("North", topPanel);
		f.addWindowListener(new WindowEventHandler());
		f.setLocation(100, 200);
		f.setSize(800, 600);
		f.setVisible(true);
		validate();
		repaint();
		defaultLoc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int fileStatus = fileChooser.showDialog(ClientImp.this, "Select file");
				if (fileStatus == JFileChooser.APPROVE_OPTION) {
					filePath = fileChooser.getSelectedFile().getAbsolutePath() + File.separator;
				}
			}
		});
		btnSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				menu.show(btnSettings, 0, btnSettings.getBounds().height);
			}
		});
		btnBGColor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Color color = JColorChooser.showDialog(ClientImp.this, "Pick Color", Color.GRAY);
				ArrayList<Object> parameters = new ArrayList<Object>();
				parameters.add(getTitle());
				parameters.add(color);
				_arch.OUT_IToolBar.sendColor(getTitle(), color.hashCode());
			}
		});
		btnHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ChatHistory = _arch.OUT_IHistoryRetrive.retriveChatHistory(getTitle());
				tvTranscript.setText(ChatHistory);
			}
		});
		btnGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				_arch.OUT_IGame.startGame();
			}
		});
		btnTemplates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TemplateGUI temp = new TemplateGUI();
				temp.setSelectInterface(new SelectInterfce() {
					@Override
					public void onItemSelect(String msg1) {
						if (!msg1.equals("")) {
							_arch.OUT_ISendTemplet.sendTemplet(getTitle(), msg1);
						}
					}
				});
			}
		});
		btnImage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
							@Override
							public boolean accept(File f) {
								String filename = f.getName();
								if (filename.endsWith(".jpg")
										|| filename.endsWith(".jpeg")
										|| filename.endsWith(".png")
										|| filename.endsWith(".bmp")
										|| filename.endsWith(".gif")
										|| filename.endsWith(".jpe")) {
									return true;
								}
								return false;
							}
							@Override
							public String getDescription() {
								return "JPEG files";
							}
						});
				int fileStatus = fileChooser.showDialog(ClientImp.this,
						"Select file");
				if (fileStatus == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					_arch.OUT_ISendImage.sendImage(getTitle(), f.getName(), getBytes(f));
				}
			}
		});
		btnAttachment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int fileStatus = fileChooser.showDialog(ClientImp.this, "Select file");
				if (fileStatus == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					byte[] fileData = getBytes(file);
					_arch.OUT_ISendFileListener.sendFile(getTitle(), file.getName(), fileData);
				}
			}
		});
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_arch.OUT_IPrintEvent.print(tvTranscript.getText());
			}
		});
		btnIncognito.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (flag == 0) {
					flag = 1;
					btnIncognito.setBackground(new Color(150, 150, 150));
				} else if (flag == 1) {
					flag = 0;
					btnIncognito.setBackground(Color.WHITE);
				}
			}
		});
		btnMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				URL connection;
				try {
					connection = new URL("http://checkip.amazonaws.com/");
					URLConnection con = connection.openConnection();
					String str = null;
					String xml = null;
					String XMLcode;
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(con.getInputStream()));
					str = reader.readLine();
					reader.close();
					String tracking = " http://api.ipaddresslabs.com/iplocation/v1.7/locateip?key=demo&ip=" + str + "&format=XML";
					BufferedReader reader1 = new BufferedReader(new InputStreamReader(new URL(tracking).openStream()));
					XMLcode = reader1.readLine();
					while ((XMLcode = reader1.readLine()) != null) {
						if (xml == null) {
							xml = XMLcode;
						} else {
							xml = xml + XMLcode;
						}
					}
					System.out.println(xml);
					try {
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db = dbf.newDocumentBuilder();
						InputSource is = new InputSource();
						is.setCharacterStream(new StringReader(xml));
						Document doc = db.parse(is);
						NodeList nodes = doc.getElementsByTagName("geolocation_data");
						for (int i = 0; i < nodes.getLength(); i++) {
							Element element = (Element) nodes.item(i);
							NodeList name = element.getElementsByTagName("latitude");
							Element line = (Element) name.item(0);
							String lat = getCharacterDataFromElement(line);
							NodeList title = element.getElementsByTagName("longitude");
							line = (Element) title.item(0);
							String lng = getCharacterDataFromElement(line);
							_arch.OUT_IToolBar.sendLocation(Double.parseDouble(lat), Double.parseDouble(lng));
							String browserPath = "C:/Users/arun/AppData/Local/Google/Chrome/Application/chrome.exe";
							String url = "https://maps.google.com/maps?q=" + lat + "," + lng;
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "?";
	}
	public void end() {}
	public void destroy() {}
	/*
	 * Implementation primitives required by the architecture
	 */
	public void messageSent(String sender, String message) {
		String status = (String) comboBox.getSelectedItem();
		if (status == "Available") {
			addMessageToTranscript(sender + ": " + "<h2>" + message + "</h2>");
		} else {
			JOptionPane.showMessageDialog(this, "User Status: DO NOT DISTURB!", "Message Sending Failed!!", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void addMessageToTranscript(String text) {
		transcriptBuf.append(text);
		transcriptBuf.append(System.getProperty("line.separator"));
		tvTranscript.setText(transcriptBuf.toString());
	}
	public String reading() {
		String sCurrentLine = null;
		String Allfile = null;
		BufferedReader br = null;
		try {
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
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String cmd = arg0.getActionCommand();
		if (cmd.equalsIgnoreCase("Send")) {
			System.out.println("HI");
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String date = " Sent on-" + dateFormat.format(cal.getTime());
			String text = entryField.getText();
			String Msg = getTitle() + ": " + text + date;
			if (!text.equals("")) {
				if (flag == 1) {
					_arch.OUT_IToolBar.sendIncognitoMessage(getTitle(), text);
				} else {
					_arch.OUT_IChat.sendMessage(getTitle(), text);
				}
			}
		} else if (cmd.equals("Color")) {
			JColorChooser colorChooser = new JColorChooser();
			Color color = colorChooser.showDialog(ClientImp.this, "Pick Color", Color.GRAY);
			_arch.OUT_IToolBar.sendColor(getTitle(), color.hashCode());
		}
	}
	@Override
	public void fileSent(String sender, String fileName, byte[] fileData) {
		if (!sender.equals(this.getTitle())) {
			tvTranscript.setText("Recived a file " + fileName + "\n");
			try {
				saveFile(filePath + fileName, fileData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private byte[] getBytes(java.io.File file) {
		byte[] b = new byte[(int) file.length()];
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(b);
			for (int i = 0; i < b.length; i++) {
				System.out.print((char) b[i]);
			}
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found.");
			e.printStackTrace();
		} catch (IOException e1) {
			System.out.println("Error Reading The File.");
			e1.printStackTrace();
		}
		return b;
	}
	private void saveFile(String strFilePath, byte[] fileData) {
		try {
			FileOutputStream fos = new FileOutputStream(strFilePath);
			fos.write(fileData);
			fos.close();
		} catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException : " + ex);
		} catch (IOException ioe) {
			System.out.println("IOException : " + ioe);
		}
	}
	@Override
	public void imageSent(String sender, String imageName, byte[] imageData) {
		try {
			saveFile(filePath + imageName, imageData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String imgtag = "<img src =\"file:" + filePath + imageName + "\"/>";
		messageSent(sender, imgtag);
	}
	@Override
	public void gameStarted() {
		_arch.OUT_ITTT.startTTT(f.getLocationOnScreen().x, f.getLocationOnScreen().y - 100);
	}
	@Override
	public void gameEnded() {
		_arch.OUT_ITTT.endTTT();
	}
	@Override
	public void played(String sender, int position) {
		if (!sender.equals(getTitle())) {
			_arch.OUT_ITTT.opponentMove(position);
		}
	}
	@Override
	public void myMove(int position) {
		_arch.OUT_IGame.play(getTitle(), position);
	}
	@Override
	public void quit() {
		_arch.OUT_IGame.endGame();
	}
	public void buzz() {
		final int lX = f.getLocationOnScreen().x;
		final int lY = f.getLocationOnScreen().y;
		System.out.println(lX + "+" + lY);
		try {
			for (int i = 0; i < 20; i++) {
				Thread.sleep(10);
				f.setLocation(lX, lY + 5);
				Thread.sleep(10);
				f.setLocation(lX, lY - 5);
				Thread.sleep(10);
				f.setLocation(lX + 5, lY);
				Thread.sleep(10);
				f.setLocation(lX, lY);
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
	@Override
	public void onColorReceived(int color) {
		tvTranscript.setBackground(new Color(color));
	}
	@Override
	public void onLocationReceived(double lat, double lon) {
		String url = "https://maps.google.com/maps?q=" + lat + "," + lon;
		String tag = "<a href = \"" + url + "\" target=\"_blank\"> click </a> ";
		messageSent(getTitle(), "lat:" + lat + " \n lon: " + lon);
	}
	@Override
	public void sendTemplet(String sender, String code) {
		String[] result = _arch.OUT_ITempletRetriver.getTemplet(code);
		if (result != null) {
			if (result[0].equals("text")) {
				messageSent(sender, result[1]);
			} else if (result[0].equals("image")) {
				String imgtag = "<img src =\"file:" + IconPath + result[1] + "\"/>";
				messageSent(sender, imgtag);
			} else if (result[0].equals("multimedia")) {
				if (result[1].equals("buzz")) {
					buzz();
				}
			}
		}
	}
}
class WindowEventHandler extends WindowAdapter {
	public void windowClosing(WindowEvent evt) {
		System.exit(0);
	}
}
