package edu.ufl.alexgre.P2P.examples;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		ServerSocket sskt = null;
		try {
			sskt = new ServerSocket(10004);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while(true){
				//accept() is a stuck method which can stop the program at current location if no work need to do.
				Socket skt = sskt.accept();
				UploadTask upload = new UploadTask(skt);
				Thread t = new Thread(upload, skt.getInetAddress().getHostAddress());
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
