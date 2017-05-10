package edu.ufl.alexgre.P2P.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.Test;

public class TestFIlePartition {
	//file divider
	//divide a file into several chunks
	@Test
	public void testFileDivider(){
		/*
		 * file cutter:
		 * cut file into different fragment files
		 * two ways to cut:
		 * 1. cut into certain number of files
		 * 2. cut into files each has no more than certain number of bytes 
		 */
		
		//cut according to each fragment file has certain number of bytes (eg. 1Kb)
		
		//inputstreamfile to connection to original file, define buffer by ourselves
		File dir = new File("/home/alex/workspace/fileparts");
		if(! dir.exists()){
			dir.mkdirs();
		}
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			File f = new File("1.jpg");
			fis = new FileInputStream(f);
			
			//calculate how many parts the file will be cutted
			int size = 1024;
			int fn = (int)(f.length() / size);
			System.out.println(fn + 1);
			
			//define a 1Kb buffer
			byte[] buf = new byte[size];
			int len = 0;
			
			fos = null;
			int seq = 1;
			while((len = fis.read(buf)) != -1){
				fos = new FileOutputStream(new File(dir, (seq++) + ".part"));
				fos.write(buf, 0, len);
				fos.flush();
				fos.close();
			}
			
			RandomAccessFile raf = new RandomAccessFile(new File(dir, "log.txt"), "rwd");
			long position = raf.length();
			raf.seek(position);
			Date d = new Date();
			raf.write((f.getName() + " has been divided into " + (seq-1) + " parts @ " + d.toString() + "\n").getBytes());
			raf.close();
			
			//create a property file to indicate how many parts create and what extension the original file is
			Properties prop = new Properties();
			prop.setProperty("partscount", (seq-1) + "");
			prop.setProperty("filename", f.getName());
			
			fos = new FileOutputStream(new File(dir, seq + ".properties"));
			
			prop.store(fos, "original file info");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fis != null){	
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//combine the parts obtained above back to file
	@Test
	public void testFileCombiner(){
		File dir = new File("/home/alex/workspace/fileparts");
		
		SequenceInputStream sis = null;
		FileOutputStream fos = null;
		try {
			//get the property file first, if no property file then return no property file and not run the combine
			File[] files = dir.listFiles(new SuffixFilter(".properties"));
			if(files.length != 1){
				throw new RuntimeException("specific properties file cannot find");
			}
			
			File config = files[0];
			
			//obtain the properties info
			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream(config);
			prop.load(fis);
			String filename = prop.getProperty("filename");
			int partnum = Integer.parseInt(prop.getProperty("partscount"));
			
			//obtain all the info about part files
			File[] partfiles = dir.listFiles(new SuffixFilter(".part"));
			if(partfiles.length != partnum){
				throw new RuntimeException("do not have all the part files!");
			}
			
			//becareful here, if use number to name the file, the best way is to use the file name to make sure the order for
			//combining is correct
			ArrayList<FileInputStream> filelist = new ArrayList<FileInputStream>();
			for(int i = 0; i < partfiles.length; i++){
				filelist.add(new FileInputStream(new File(dir, (i+1)+".part")));
			}
			
			Enumeration<FileInputStream> en = Collections.enumeration(filelist);
			
			sis = new SequenceInputStream(en);
			
			fos = new FileOutputStream(new File(dir, filename));
			
			byte[] buf = new byte[512];
			int len = 0;
			while((len = sis.read(buf)) != -1){
				fos.write(buf, 0, len);
				fos.flush();
			}
			
			RandomAccessFile raf = new RandomAccessFile(new File(dir, "log.txt"), "rwd");
			long position = raf.length();
			raf.seek(position);
			Date d = new Date();
			raf.write(("Part files has been merged" + " as" + filename + "  @ " + d.toString() + "\n").getBytes());
			raf.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(sis != null){	
				try {
					sis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	//sequence input stream
	@Test
	public void testSequenceStream(){
		//combine 1.txt, 2.txt and 3.txt into one file
		//can easily combine several file into one file
		
		SequenceInputStream sis = null;
		FileOutputStream fos = null;
		try {
			
			// Vector<FileInputStream> v= new Vector<FileInputStream>();
			// v.add(new FileInputStream(new File("3.txt")));
			// v.add(new FileInputStream(new File("2.txt")));
			// v.add(new FileInputStream(new File("1.txt")));
	
			//Enumeration<FileInputStream> em = v.elements();
			
			//sis = new SequenceInputStream(em);
			
			ArrayList<FileInputStream> al = new ArrayList<FileInputStream>();
			for(int i = 1; i < 4; i++){
				al.add(new FileInputStream(new File(i + ".txt")));
			}
			
			/*
			//use iterator to replace the enumerator function
			final Iterator<FileInputStream> itr = al.iterator();
			
			//create enumerator obj used for SquenceInputStream
			Enumeration<FileInputStream> en = new Enumeration<FileInputStream>() {
				
				@Override
				public FileInputStream nextElement() {	
					return itr.next();
				}
				
				@Override
				public boolean hasMoreElements() {
					return itr.hasNext();
				}
			};
			*/
			
			//we have to return a enumerator interface, we found Collection.enumeration(collection) will return a obj of enumeration interface
			//this method is implemented as above using iterator and override enumeration
			Enumeration<FileInputStream> en = Collections.enumeration(al);
			
			sis = new SequenceInputStream(en);
			
			fos = new FileOutputStream(new File("1234.txt"));
			
			byte[] temp = new byte[256];
			
			int len = 0;
			
			while((len = sis.read(temp)) != -1){
				fos.write(temp, 0, len);
				fos.flush();
			}
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
			if(sis != null){
				try {
					sis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
