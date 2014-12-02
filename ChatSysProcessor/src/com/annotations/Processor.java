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
	
	static String targetFolder = "ChattingApplication";
	static String outputRoot = null;
	static String[] ARGS= null;
	static HashMap<String, String> components;
	static List<String> features;
	
	public Processor(String[] args) {
		outputRoot = System.getProperty("user.home") + File.separator + "Documents" + File.separator;
		ARGS = args;
		features = fillCollection();
		String target = getRelativePathToChattingApplication();
		File file = new File(target);
		listFilesForFolder(file);
	}
	
	public Processor(String[] args, String pathToSource, String pathToOutput) {
		outputRoot = pathToOutput;
		ARGS = args;
		features = fillCollection();
		String target = pathToSource;
		File file = new File(target);
		listFilesForFolder(file);
	}
	
	private static String getRelativePathToChattingApplication() {
		String target = System.getProperty("user.dir"); //absolute path from where application has initialized
		target = target.substring(0, target.lastIndexOf(File.separator)); //drop the last folder to go down one level
		target = target + File.separator + targetFolder; //go into ChattingApplication src directory
		return target;
	}

	public static void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	    	String filename = fileEntry.getName();
	    	String filePath = fileEntry.getPath();
	    	String filePathExtension = filePath.substring(filePath.indexOf(targetFolder), filePath.length());
	    	String targetPath = outputRoot + filePathExtension;
	    	File targetFile = new File(targetPath);
	    	String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
	        if (!extension.equals("java")) {
	        	if (fileEntry.isDirectory()){
	        		System.out.println(fileEntry.getName());
	        		if (doesListContain(fileEntry.getName(), features)){ //i anticipate a problem here
		        		targetFile.mkdirs();
		        		listFilesForFolder(fileEntry);
	        		}
	        	}else
	        	{
	        		writeFile(fileEntry, targetFile);
	        	}
	        } else {
	        	annotationProcessor(fileEntry, targetFile);
	        }
	    }
	}
	
	private static void annotationProcessor(File file, File targetFile)
	{
		List<String> annotatedVariables = new ArrayList<String>();
		FileOutputStream output = null;
		BufferedReader reader = null;
		boolean processBit = true;
		boolean methodAnnotation = false;
		int bracketCount = 0;
		int parenthesisCount = 0;
		Type type = null;
		
		try{
			reader = new BufferedReader(new FileReader(file));
			output = new FileOutputStream(targetFile);
			file.createNewFile();
			while(reader.ready()){	
				String s = reader.readLine();
				if(s.contains("@ChattingAnnotation")){ 
					type = determineType(s, type);
					if(type == Type.METHOD)
						methodAnnotation = true;
					processBit = canProcess(s);
				}
				else if (isPropertyAnnotated(s, annotatedVariables))
				{
					type = Type.INSTANTIATEDPROPERTY;
					processBit = false;
				}
				if(!processBit){
						if(type == Type.METHOD || type == Type.CLASS){
							bracketCount += countCharacters(s, "{");
							bracketCount -= countCharacters(s, "}");
							if(bracketCount == 0 && !methodAnnotation){
								processBit = true;
								output.write(s.getBytes());
								output.write(13);
								output.write(10);
							}		
							methodAnnotation = true;
						}else if(type == Type.PROPERTY){
							if(s.contains(";"))
							{
								processBit = true;
								//string cat = "calico"; then variable = "cat = "calico"; problem?
								String variable = s.substring(s.indexOf(" "), s.indexOf(";")).trim();
								annotatedVariables.add(variable);
							}
						}else if(type == Type.INSTANTIATEDPROPERTY){
							parenthesisCount += countCharacters(s, "(");
							parenthesisCount -= countCharacters(s, ")");
							bracketCount += countCharacters(s, "{");
							bracketCount -= countCharacters(s, "}");
							if(parenthesisCount == 0 && bracketCount == 0)
							{
								processBit = true;
							}
						}
				}
				else if (!doesListContain(s, annotatedVariables)){
					output.write(s.getBytes());
					output.write(13);
					output.write(10);
				} else{			
					System.out.println(s);
				}
			}
			output.flush();
			output.close();
			reader.close();
			
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
	
	/*
	 * in this method, features = files
	 * The components added to hashmap are for mapping .java class files directly to ARGS
	 * items listed in the 'features' variable are used in writing files to the output directory
	 */
	private static List<String> fillCollection(){
		List<String> features = new ArrayList<String>();
		HashMap<String,String> components = new HashMap<String, String>();
		components.put("Game", "TTTGame");
		components.put("ChatBot", "Bot"); //where is the actual chatbot feature in the app? is it used?
		components.put("History", "ChatHistory");
		components.put("Attachment", "Templet");//problem here - other classes use Templet
		for(String values : ARGS)
			features.add(components.get(values).toString());
		
		if (ARGS.length > 0)//why?
			features.add("comp");
		features.add("com");
		features.add("pla");
		features.add("chatsys");
		features.add("client");
		features.add("server");
		features.add(".svn");
		features.add("prop-base");
		features.add("text-base");
		features.add("annotation");
		features.add("META-INF");
		features.add(".settings");
		features.add("icon");
		features.add("src");
		return features;
	}
	
	private static boolean doesListContain(String value, List<String> names){
		boolean contains = false;
		for(String name : names)
		{
			if (name.contains(value))
				contains = true;
		}
		return contains;
	}
	
	private static boolean isPropertyAnnotated(String line, List<String> annotatedVariables){
		boolean contains = false;
		for (String property : annotatedVariables){
			if (line.contains(property))
				contains = true;
		}
		return contains;
	}
}
