package com.cisco.cstg.ccp.customloader;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pravanga on 1/21/2016.
 */
public class CustomLoader {

	Map<String, Integer> hMap;
	MappingDataDto mappingDataDto;
	DateFormat originalDateFormat;
	DateFormat targetFormat;

	Pattern pattern;
	String urlRegex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	public CustomLoader() {
		targetFormat = new SimpleDateFormat("MM/dd/yyyy");
		hMap = new HashMap<>();
		pattern.compile(urlRegex);
	}

	public MappingDataDto getMappingDataDto() {
		return mappingDataDto;
	}

	public void setMappingDataDto(MappingDataDto mappingDataDto) {
		this.mappingDataDto = mappingDataDto;
	}

	public void fillHMap(){
		for(FieldMappingDto fieldMappingDto : mappingDataDto.getFieldMappingDtoList()){
			if(fieldMappingDto.getOutputField().equals("Collected Date")){
				originalDateFormat = new SimpleDateFormat(fieldMappingDto.getFormat());
			}
			hMap.put(fieldMappingDto.getOutputField(), fieldMappingDto.getPosition());
		}
	}

	public String firstFilter(String src){
		StringBuilder sb = new StringBuilder();
		try{
			List<String> split = intellegentSplit(src, mappingDataDto.getDelimiter());
			sb.append(dateFormat(split.get(hMap.get("Collected Date")))).append(",").append(split.get(hMap.get("Source IP"))).append(",");
			if(!hMap.containsKey("In Traffic") && !hMap.containsKey("Total Traffic")){
				sb.append("0").append(",").append(split.get(hMap.get("Out Traffic"))).append(",").append(split.get(hMap.get("Out Traffic"))).append(",");
			} else if(hMap.containsKey("In Traffic") && !hMap.containsKey("Total Traffic")){
				sb.append(split.get(hMap.get("In Traffic"))).append(",").append(split.get(hMap.get("Out Traffic"))).append(",");
				Integer total = Integer.parseInt(split.get(hMap.get("In Traffic"))) + Integer.parseInt(split.get(hMap.get("Out Traffic")));
				if(total < 0){
					sb.append("0").append(",");	
				}else{
					sb.append(total).append(",");
				}
			} else if(hMap.containsKey("In Traffic") && hMap.containsKey("Total Traffic")){
				sb.append(split.get(hMap.get("In Traffic"))).append(",").append(split.get(hMap.get("Out Traffic"))).append(",").append(split.get(hMap.get("Total Traffic"))).append(",");
			}            
			if(hMap.containsKey("URL")){
				sb.append(urlHandler(split.get(hMap.get("URL"))));
			}else if(hMap.containsKey("Domain")){
				sb.append(split.get(hMap.get("Domain")));
			}else if(hMap.containsKey("Destination IP")){
				sb.append(split.get(hMap.get("Destination IP")));
			}
		}catch(Exception e){
			return "";
		}
		return sb.toString();
	}

	public String dateFormat(String dateIn) throws ParseException{
		return targetFormat.format(originalDateFormat.parse(dateIn));
	}

	public String urlHandler(String url)
	{
		String urlReturn = "";
		try {
			URL isValidUrl = new URL( url );
			if(isValidUrl != null)
				urlReturn = url;
		} catch (MalformedURLException e) {
			String [] urlArr = url.split(" ");
			for (String urlSample : urlArr){
				Matcher matcher = this.pattern.matcher(urlSample);
				if(matcher.matches()){
					urlReturn = urlSample;
				}
			}
		}
		return urlReturn;
	}

	public static void main(String args[]) throws IOException{
		long startTime = System.currentTimeMillis();
		CustomLoader customLoader = new CustomLoader();
		FileInputStream fstream = new FileInputStream(args[0]);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine = "";
		while ((strLine = br.readLine()) != null)   {
			Gson gson = new GsonBuilder().create();
			customLoader.setMappingDataDto(gson.fromJson(decrypt(strLine), MappingDataDto.class));   
		}
		customLoader.fillHMap();

		List<String> fileNames = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(args[1]))) {
			for (Path path : directoryStream) {
				fileNames.add(path.toString());
			}
		} catch (IOException ex) {
			System.out.println("No Directory Found");
		}

		for(String fileName : fileNames){
			try{
				Files.lines(Paths.get(fileName), Charset.defaultCharset())
				//        		.skip(customLoader.getMappingDataDto().getDataStartLine() + 5)        
				.map(customLoader::firstFilter)
				.filter(e -> e.length() > 5) //Grater than zero works but !!!
				.forEach(System.out::println);

			} catch (Exception e){
			}
		}

		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime)/1000);
	}


	private static final String DEFAULT_PWD = "Csc0#ccs$123";

	public static String encrypt(String plaintext) {
		return encrypt(plaintext, DEFAULT_PWD);
	}

	public static String decrypt(String ciphertext) {
		return decrypt(ciphertext, DEFAULT_PWD);
	}

	public static String encrypt(String plaintext, String passphrase) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(passphrase);
		return encryptor.encrypt(plaintext);
	}

	public static String decrypt(String ciphertext, String passphrase) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(passphrase);
		return encryptor.decrypt(ciphertext);
	}

	public static List<String> intellegentSplit(String sequence, String delimiter){
		char delim = ',';
		if(delimiter.equals("\t")){
			delim = '\t';
		}else {
			delim = delimiter.charAt(0);
		}
		List<String> result = new ArrayList<String>();
		int start = 0;
		boolean inQuotes = false;
		for (int current = 0; current < sequence.length(); current++) {
			if (sequence.charAt(current) == '\"') inQuotes = !inQuotes; 
			boolean atLastChar = (current == sequence.length() - 1);
			if(atLastChar) result.add(sequence.substring(start));
			else if (sequence.charAt(current) == delim && !inQuotes) {
				result.add(sequence.substring(start, current));
				start = current + 1;
			}
		}
		if(sequence.length() > 1 && sequence.charAt(sequence.length()-1) == delim){
			result.add("");
		}
		return result;
	}
}
