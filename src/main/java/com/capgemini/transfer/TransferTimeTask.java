package com.capgemini.transfer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TimerTask;

public class TransferTimeTask extends TimerTask {
	private String propertiesFile;
	private String excelConfig;
	public TransferTimeTask(String excelConfig, String propertiesFile){
		this.excelConfig = excelConfig;
		this.propertiesFile = propertiesFile;
	}
	@Override
	public void run() {
		System.out.println("start......");
		try {
			App.doIt(this.excelConfig,this.propertiesFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("end with error..........");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("end with error..........");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("end with error..........");
		}
		System.out.println("end......");
	}

}
