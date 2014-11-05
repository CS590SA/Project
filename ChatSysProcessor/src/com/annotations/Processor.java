package com.annotations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

public class Processor {
	static String FILEROOT = "C:\\temp\\Development\\";
	static String[] ARGS= null;
	static HashMap<String, String> components;
	public static void main(String[] args) {
		ARGS = args;
		components = fillCollection();
		File file = new File("C:/Users/Timothy.Timothy-HP/Downloads/ChattingApp-master (1)/ChattingApp-master/src");
		listFilesForFolder(file);
	}
	
	public static void listFilesForFolder(final File folder) {
		HashMap<String, String> components = fillCollection();
	    for (final File fileEntry : folder.listFiles()) {
	    	String filename = fileEntry.getName();
	    	String filePath = fileEntry.getPath();
	    	String filePathExtension = filePath.substring(filePath.indexOf("ChattingApp-master"), filePath.length());
	    	String targetPath = FILEROOT + filePathExtension;
	    	File targetFile = new File(targetPath);
	    	String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
	        if (!extension.equals("java")) {
	        	if (fileEntry.isDirectory()){
	        		System.out.println(fileEntry.getName());
	        		targetFile.mkdirs();
	        		listFilesForFolder(fileEntry);
	        	}else
	        	{
	        		writeFile(fileEntry, targetFile);
	        	}
	        } else {
	        	//checkAnnotations
	        	annotationProcessor(fileEntry, targetFile);
	            //System.out.println(fileEntry.getName());
	        }
	    }
	}
	
	private static void annotationProcessor(File file, File targetFile)
	{
		List<String> annotatedVariables = new ArrayList<String>();
		FileOutputStream output = null;
		boolean processBit = true;
		boolean foundAnnotationBit = false;
		int bracketCount = 0;
		int propertyCount = 0;
		Type type = null;
		
		try{
			
			//input = new FileInputStream("C:/development/TestResources/AnnotationImpl.java");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			output = new FileOutputStream(targetFile);
			file.createNewFile();
			while(reader.ready()){	
				String s = reader.readLine();
				if(s.contains("@ChattingAnnotation")){ 
					type = determineType(s, type);
					processBit = canProcess(s);
				}
				if(!processBit){
						if(type == Type.METHOD || type == Type.CLASS){
							bracketCount += countCharacters(s, "{");
							bracketCount -= countCharacters(s, "}");
							if(bracketCount == 0){
								processBit = true;
								output.write(s.getBytes());
								output.write(13);
								output.write(10);
							}						
						}else if(type == Type.PROPERTY){
							if(s.contains(";"))
							{
								processBit = true;
								String annotatedV = s.substring(s.indexOf(" "), s.indexOf(";")).trim();
								annotatedVariables.add(annotatedV);
							}
						}
				}
				else{
					boolean containsIllegalVariable = false;
					for(String variable : annotatedVariables){
						if (s.contains(variable))
							containsIllegalVariable = true;
					}
					if (!containsIllegalVariable){
						output.write(s.getBytes());
						output.write(13);
						output.write(10);
					}
				}				
			}
			//input.close();
			output.flush();
			output.close();
			for(String variable : annotatedVariables){
				System.out.println(variable);
			}
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	private static void writeFile(File file, File targetFile){
		BufferedReader reader;
		FileOutputStream output = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			output = new FileOutputStream(targetFile);
			file.createNewFile();
			while(reader.ready()){
				String s = reader.readLine();
				output.write(s.getBytes());
				output.write(13);
				output.write(10);
			}
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static String findVariable(String line){
		return line.substring(line.indexOf(" "), line.length());
	}
	
	private static int countCharacters(String line, String character){
		int characterCount = 0;
		for (int i = 0; i < line.length(); i++)
		{
			if (line.charAt(i) == character.toCharArray()[0])
				characterCount++;
		}
		
		return characterCount;
	}
	
	private static Type determineType(String line, Type type){
		Type returnType = type;
		if(line.contains("@ChattingAnnotation")){ 
			if(line.contains("method"))
				returnType = Type.METHOD;
			else if(line.contains("class"))
				returnType = Type.CLASS;
			else if(line.contains("property"))
				returnType = Type.PROPERTY;
		}
		return returnType;
	}
	
	private static boolean canProcess(String line){
		boolean processBit = true;
		boolean foundAnnotationBit = false;
		for(String lines: ARGS){
			if(line.contains(lines))
				foundAnnotationBit = true;
		}
		if(foundAnnotationBit){
			processBit = true;
			foundAnnotationBit = false;
		}else
			processBit = false;
		
		return processBit;
	}
	
	private static HashMap fillCollection(){
		HashMap components = new HashMap<String, String>();
		components.put("game", "TTTGame");
		components.put("chatbot", "Bot");
		components.put("history", "ChatHistory");
		return components;
	}
}
