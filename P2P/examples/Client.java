package edu.ufl.alexgre.P2P.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	public static void main(String[] args) {
		try {
			Socket skt = new Socket("127.0.0.1", 10004);
			File f = new File("tiger.jpg");
			String dir = f.getPath();
			System.out.println(dir);
			FileInputStream fis = new FileInputStream(f);
			
			OutputStream os = skt.getOutputStream();
			byte[] buf = new byte[512];
			int len = 0;
			
			while((len = fis.read(buf)) != -1){
				os.write(buf, 0, len);
				os.flush();
			}
			skt.shutdownOutput();
			
			InputStream is = skt.getInputStream();
			byte[] bufin = new byte[512];
			int len1 = is.read(bufin);
			
			System.out.println(new String(bufin, 0 ,len1));
			
			fis.close();
			
			skt.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
