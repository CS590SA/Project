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
  
    
    public void messageSent (String sender,String message)  ;        
  
    @ChattingAnnotation(feature="file")
    public void fileSent (String sender,String fileName,byte[] fileData)  ;        
  
    @ChattingAnnotation(feature="image")
    public void imageSent (String sender,String imageName,byte[] imageData)  ;        
  
    @ChattingAnnotation(feature="game")
    public void myMove (int position)  ;
    @ChattingAnnotation(feature="game")
    public void quit ()  ;        
  
    @ChattingAnnotation(feature="game")
    public void gameStarted ()  ;      
    
    @ChattingAnnotation(feature="game")
    public void gameEnded ()  ;        
    
    @ChattingAnnotation(feature="game")
    public void played (String sender,int position)  ;        
  
    
    public void sendTemplet (String sender,String code)  ;        
  
    
    public void onColorReceived (int color)  ;        
    public void onLocationReceived (double lat,double lon)  ;        
}