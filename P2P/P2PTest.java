package edu.ufl.alexgre.P2P;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class P2PTest {
	@Test
	public void test(){
		Server.main(null);
		
		
	}
	@Test
	public void test1(){
		Peer1.main(null);
		Peer2.main(null);
		Peer3.main(null);
		Peer4.main(null);
		Peer5.main(null);
	}
	
	
	//test file partition and combine
	@Test
	public void FilePartitionAndCombine(){
		String path = "C:\\Users\\alex\\Desktop\\computer networks\\P2PTest\\Client\\c1\\partfilefolder2";
		FileCombination fc = null;
		
		try {
			fc = new FileCombination(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println(fc.getOriginalFileName());
		System.out.println(fc.getPartFileNum());
		fc.fileCombination();
	}
	
	//test file partition and properties file generation and store
	@Test
	public void testFilePartition(){
		FilePartition fp = new FilePartition();
		File prop = null;
		boolean a = fp.filePartition("C:\\Users\\xiyang\\Desktop\\computer networks\\P2PTest\\Server\\test1.zip", 1024*1024);
		boolean b = fp.configFile();
		System.out.println(a + "  " + b);
		
		File[] files = new File(fp.fileDestination()).listFiles();
		for(File f: files){
			String name = f.getName();
			if(name.endsWith(".properties")){
				prop = f;
			}
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(prop));
			String info = null;
			while((info = br.readLine()) != null){
				System.out.println(info);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
