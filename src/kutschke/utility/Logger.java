package kutschke.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {

	protected StringBuffer buf = new StringBuffer(1024);
	
	public <T> void printarray(T[] arr){
		for(Object o: arr){
			System.out.print(o.toString());
			System.out.print(",");
		}
		System.out.println();
	}
	
	public <T> void printarraytoBuf(T[] arr){
		for(T o: arr){
			print(o.toString());
			print(",");
		}
		println("");
	}
	
	public void print(String str){
		buf.append(str);
	}
	public void println(String str){
		buf.append(str);
		buf.append(System.getProperty("line.separator"));
	}
	public void flushBuffer(String filename){
		File f = new File(filename);
		try{
		f.createNewFile();
		
		BufferedWriter w = new BufferedWriter(new FileWriter(f),8192);
		w.write(buf.toString());
		w.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	public String getBufferString(){
		return buf.toString();
	}
	
	public void clearBuffer(){
		buf = new StringBuffer(1024);
	}
	
}
