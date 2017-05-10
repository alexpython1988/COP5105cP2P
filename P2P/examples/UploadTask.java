package edu.ufl.alexgre.P2P.examples;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class UploadTask implements Runnable {
	private Socket skt;
	
	public UploadTask(Socket skt){
		this.skt = skt;
	}

	@Override
	public void run() {
		FileOutputStream fos = null;
		int duplicate = 1;
		String ip = skt.getInetAddress().getHostName();
		try {
			System.out.println(ip + " connected...");
			
			File dir = new File("C:\\Users\\xiyang\\Desktop\\picture");
			if(! dir.exists()){
				dir.mkdirs();
			}
			
			File f = new File(dir, ip + ".jpg");
			while(f.exists()){
				f = new File(dir, ip + "(" + (duplicate++) + ").jpg");
			}
			
			InputStream is = skt.getInputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			fos = new FileOutputStream(f);
			
			while((len = is.read(buf)) != -1){
				fos.write(buf, 0, len);
				fos.flush();
			}
			
			OutputStream os = skt.getOutputStream();
			String echo = "echo: received the file from " + ip +" .";
			os.write(echo.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(skt != null){
				try {
					skt.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
