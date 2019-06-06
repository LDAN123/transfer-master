package com.capgemini.transfer.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class FtpUtil {
	/*
	 * public static void uploadFile(String hostname, String username, String
	 * password, String dir,String filePath){ FTPSClient ftps = new
	 * FTPSClient(false); FTPClientConfig config = new FTPClientConfig();
	 * ftps.configure(config);
	 * ftps.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
	 * FileInputStream fin = null; try { ftps.connect(hostname,22);
	 * ftps.login(username, password); ftps.setFileType(FTP.BINARY_FILE_TYPE);
	 * ftps.changeWorkingDirectory(dir); File file = new File(filePath); fin =
	 * new FileInputStream(file); ftps.storeFile(file.getName(), fin);
	 * fin.close(); } catch (IOException e) { e.printStackTrace(); }finally{
	 * if(ftps.isConnected()){ try { ftps.disconnect(); } catch (IOException e)
	 * { e.printStackTrace(); } } }
	 * 
	 * }
	 */
	/**
	 * 
	 * @param uploadFile	待上传的文件
	 * @param dirName	ftp服务器的存储目录
	 * @param remoteFileName	上传到ftp的文件重命名
	 * @param host	ftp的ip
	 * @param port	ftp的port
	 * @param username	ftp的username
	 * @param password	ftp的password
	 * @return
	 */
	public static boolean uploadFileUseSftp(String uploadFile, String dirName, String remoteFileName, String host,
			int port, String username, String password) {
		ChannelSftp sftp = null;
		Session sshSession = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + host + ".");

			sftp.cd(dirName);

			File file = new File(uploadFile);
			if (remoteFileName == null || remoteFileName.trim().isEmpty()) {
				remoteFileName = file.getName();
			}
			FileInputStream fis = new FileInputStream(file);
			sftp.put(fis, remoteFileName);
			fis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
			if (sshSession != null) {
				sshSession.disconnect();
			}
		}
	}
	
	public static boolean uploadFilesUseSftp(List<String> uploadFiles, String dirName, String host, int port, String username, String password) {
		ChannelSftp sftp = null;
		Session sshSession = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + host + ".");

			sftp.cd(dirName);
			File file = null;
			FileInputStream fis = null;
			for(String uploadFile : uploadFiles){
				file = new File(uploadFile);
				if(file.exists() && file.canRead()){
					fis = new FileInputStream(file);
					sftp.put(fis, file.getName());
					fis.close();
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (sftp != null) {
				sftp.disconnect();
			}
			if (sshSession != null) {
				sshSession.disconnect();
			}
		}
	}

	/**
	 * 
	 * @param uploadFile	待上传的文件
	 * @param dirName	ftp服务器的存储目录
	 * @param remoteFileName	上传到ftp的文件重命名
	 * @param host	ftp的ip
	 * @param port	ftp的port
	 * @param userName	ftp的username
	 * @param userPassword	ftp的password
	 * @return
	 */
	public static boolean uploadFileUseFtp(String uploadFile, String dirName, String remoteFileName, String host,
			int port, String username, String password) {
		FTPSClient ftpClient = null;
		try {

			ftpClient = ftpConnection(host, port, username, password);

			makeDirectory(ftpClient, dirName);
			ftpClient.changeWorkingDirectory(dirName);

			boolean isUploadSuccess = ftpPut(ftpClient, remoteFileName, new FileInputStream(uploadFile));
			if (isUploadSuccess) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftpClose(ftpClient);
		}
		return false;
	}
	
	public static boolean uploadFilesUseFtp(List<String> uploadFiles, String dirName, String host,
			int port, String username, String password) {
		FTPSClient ftpClient = null;
		try {

			ftpClient = ftpConnection(host, port, username, password);

			makeDirectory(ftpClient, dirName);
			ftpClient.changeWorkingDirectory(dirName);
			boolean isUploadSuccess = true;
			File file = null;
			for(String uploadFile : uploadFiles){
				file = new File(uploadFile);
				if(!ftpPut(ftpClient, file.getName(), new FileInputStream(file))){
					isUploadSuccess = false;
				}
			}
			
			if (isUploadSuccess) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ftpClose(ftpClient);
		}
		return false;
	}

	private static FTPSClient ftpConnection(String adder, int port, String userName, String passWord) {
		FTPSClient ftpClient = null;
		try {
			ftpClient = new FTPSClient(false);
			ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
			ftpClient.connect(adder, port);
			ftpClient.login(userName, passWord);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.execPROT("P");
			ftpClient.enterLocalPassiveMode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ftpClient;
	}

	private static void makeDirectory(FTPSClient ftpClient, String dirName) {
		try {
			ftpClient.makeDirectory(dirName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean ftpPut(FTPSClient ftpClient, String filename, InputStream in) throws IOException {
		try {
			boolean result = ftpClient.storeFile(filename, in);
			return result;

		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	private static boolean ftpClose(FTPSClient ftpClient) {
		try {
			if (ftpClient != null) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
