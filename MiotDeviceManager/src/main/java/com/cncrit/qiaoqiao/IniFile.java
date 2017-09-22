package com.cncrit.qiaoqiao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

/** */

/**
 *
 * 
 * @author wyf
 * @version 
 */
public final class IniFile {
	public static final String tag = IniFile.class.getName();
	
	public static boolean createFileIfNotExist(String file){
		try {
			File f = new File(file);
			if (!f.exists()){
				if ( !f.createNewFile() ){
					return false;
				}
			}
		} catch (Exception e){
		}
		return true;
	}
	
	public static String getProfileString(String file, String section,
			String variable, String defaultValue) {
		String strLine, value = "";
		boolean isInSection = false;
		try {
			if(!createFileIfNotExist(file))
				return defaultValue;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			while ((strLine = bufferedReader.readLine()) != null) {
				strLine = strLine.trim();
				strLine = strLine.split(";")[0].trim();
				isInSection = strLine.startsWith("["+section+"]");
				if (isInSection == true) {
					while ((strLine = bufferedReader.readLine()) != null) {
						strLine = strLine.trim();
						if(strLine.startsWith("[")) {
							bufferedReader.close();
							return defaultValue;
						}
						String[] strArray = strLine.split("=");
						if ( strArray[0].trim().equalsIgnoreCase(variable)) {
							if (strArray.length == 1) {
								bufferedReader.close();
								return defaultValue;
							} else if (strArray.length == 2) {
								value = strArray[1].trim();
								bufferedReader.close();
								return value;
							} else if (strArray.length > 2) {
								value = strLine.substring(
										strLine.indexOf("=") + 1).trim();
								bufferedReader.close();
								return value;
							}
						}
					}
				}
			}
			bufferedReader.close();
		} catch(Exception e){
		}
		return defaultValue;
	}

	public static boolean setProfileString(String file, String section,
			String variable, String value) {
		String allLine, strLine, newLine;
		String getValue;
		boolean isInSection = false;
		
		try {			
			if(!createFileIfNotExist(file))
				return false;
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			allLine = "";
			while ((strLine = bufferedReader.readLine()) != null) {
				strLine = strLine.trim();
				allLine += strLine+"\r\n";
				isInSection = strLine.startsWith("["+section+"]");
				if (isInSection == true) {
					while((strLine = bufferedReader.readLine()) != null) {
						strLine = strLine.trim();
						if(strLine.startsWith("[")) {
							break;
						}
						String[] strArray = strLine.split("=");
						getValue = strArray[0].trim();
						if (getValue.equalsIgnoreCase(variable)) {
							newLine = getValue + "=" + value ;
							allLine += newLine + "\r\n";
							while (( strLine = bufferedReader.readLine()) != null) {
								allLine += strLine + "\r\n";
							}
							bufferedReader.close();
							BufferedWriter bufferedWriter = new BufferedWriter(
									new FileWriter(file, false));
							bufferedWriter.write(allLine);
							bufferedWriter.flush();
							bufferedWriter.close();
	
							return true;
						}
						if(strLine!=null)
							allLine += strLine+"\r\n";
					}
					newLine = variable + "=" + value ;
					allLine += newLine + "\r\n";
					if(strLine!=null)
						allLine += strLine + "\r\n";
					while (( strLine = bufferedReader.readLine()) != null) {
						allLine += strLine + "\r\n";
					}
					bufferedReader.close();
					BufferedWriter bufferedWriter = new BufferedWriter(
							new FileWriter(file, false));
					bufferedWriter.write(allLine);
					bufferedWriter.flush();
					bufferedWriter.close();
					return true;
				} 
			}
			if (!isInSection){
				allLine += "["+section+"]\r\n";
				newLine = variable + "=" + value ;
				allLine += newLine + "\r\n";
				bufferedReader.close();
				BufferedWriter bufferedWriter = new BufferedWriter(
						new FileWriter(file, false));
				bufferedWriter.write(allLine);
				bufferedWriter.flush();
				bufferedWriter.close();
				return true;
			}
			bufferedReader.close();
		} catch (Exception ex) {
		}
		return false;
	}
}
