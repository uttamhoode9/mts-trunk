package com.devoteam.srit.xmlloader.importsipp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class StartSipP {
	/**
	 * @param args
	 */
	public static void main(String... args) {
		
		String fileName = null; 
		List<String> options = null ; 
		
		//
        // Handle arguments
        //
        if (args.length <= 0) {
            usage("At least one argument is required : the scenario file path");
        }
        for(int i=0; i<args.length; i++)
        {	
        	/*
        	 * It's an option ! 
        	 */
        	if(args[i].equals("-sn"))
        	{
        		fileName = args[i+1]+".xml"; 
        	}
        	if(args[i].equals("-sf"))
        	{
        		fileName = args[i+1]; 
        	}
        	if(args[i].equals("-users"))
        	{
        		options.add("-instances"); 
        	}
        }
        
		String command = "cmd /c startTest.bat"; 
        Process p;
		try 
		{
			p = Runtime.getRuntime().exec(command);
			InputStream input = p.getInputStream();
			InputStream errInput= p.getErrorStream();
			
			int data = input.read();
			while(data != -1) 
			{
				System.out.print((char)data);
				data = input.read();
			}
			
			data = errInput.read();
			while(data != -1) 
			{
				System.out.print((char)data);
				data = input.read();
			}
			
			p.waitFor();
			int exitValue = p.exitValue();
			System.exit(exitValue);
		}	
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static public void usage(String message) {
        System.out.println(message);
        System.exit(10);
    }
	
	public static void copy (String SourceFile, String NewDestFile) throws IOException
	//ouvre le fichier et copie le contenu du fichier dans un nouveau fichier 
	/*
	pre: SourceFile est initialis� et correspond au nom d'un fichier externe qui existe et est ferm�,
	NewDestFile est initialis� et correspond au nom d'un fichier externe qui n'existe pas (s'il existe d�j� le contenu du pr�c�dent fichier sera �cras�)
	post: SourceFile est inchang�e et NewDestFile est le contenu de SourceFile et est ferm�
	*/
	{	
		// je met SourceFile dans nomFichier
		File nomFichier = new File(SourceFile);
		//je met nomFichier dans inputfile
		Scanner inputFile = new Scanner(nomFichier);
		
		/*inputfile est initialis�,il est li� au fichier externe SourceFile et est ouvert en lecture*/
		PrintWriter outputFile = new PrintWriter(NewDestFile);
		
		//ouvre le fichier NewDestFile
		//�crit dans le fichier nexDestFile le contenu du fichier source
		while (inputFile.hasNext())//regarde si la ligne suivante existe
		{
			//inputfile.nextline() voir scanner et file au debut de la m�thode
			outputFile.println(inputFile.nextLine());
		}
		
		//ferme le fichier en �criture
		outputFile.close(); 
		//inputfile est ferm�
		inputFile.close();
	}


}
