package com.pla.chatsys.client;


import com.pla.chatsys.annotation.ChattingAnnotation;
import com.pla.chatsys.client.ClientArch;

public interface IClientImp 
{

	/*
	  Getter and Setter of architecture reference
	*/
    public void setArch (ClientArch arch);
	public ClientArch getArch();
	
	/*
  	  Myx Lifecycle Methods: these methods are called automatically by the framework
  	  as the bricks are created, attached, detached, and destroyed respectively.
	*/	
	public void init();	
	public void begin();
	public void end();
	public void destroy();

	/*
  	  Implementation primitives required by the architecture
	*/
	@ChattingAnnotation(feature="Availability", type="property")
    public void messageSent (String sender,String message)  ;        
  
    @ChattingAnnotation(feature="Attachment", type="property")
    public void fileSent (String sender,String fileName,byte[] fileData);        
    
    @ChattingAnnotation(feature="Image", type="property")
    public void imageSent (String sender,String imageName,byte[] imageData);        
  
    @ChattingAnnotation(feature="Game", type="property")
    public void myMove (int position);  
    @ChattingAnnotation(feature="Game", type="property")
    public void quit ();        
  
    @ChattingAnnotation(feature="Game", type="property")
    public void gameStarted ();
    @ChattingAnnotation(feature="Game", type="property")
    public void gameEnded ();  
    @ChattingAnnotation(feature="Game", type="property")
    public void played (String sender,int position);        
  
    
    public void sendTemplet (String sender,String code);        
  
    @ChattingAnnotation(feature="BGColor", type="property")
    public void onColorReceived (int color);      
    
    @ChattingAnnotation(feature="Map", type="property")
    public void onLocationReceived (double lat,double lon);        
}