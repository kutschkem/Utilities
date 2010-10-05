package TestClasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Testing {

	static StringBuffer buf = new StringBuffer(1024);
	
	public static <T> void printarray(int[] arr){
		for(int o: arr){
			System.out.print(o);
			System.out.print(",");
		}
		System.out.println();
	}
	
	public static <T> void printarray(Number[] arr){
		for(Number o: arr){
			System.out.print(o.toString());
			System.out.print(",");
		}
		System.out.println();
	}
	
	public static <T> void printarray(Object[] arr){
		for(Object o: arr){
			System.out.print(o.toString());
			System.out.print(",");
		}
		System.out.println();
	}
	
	public static <T> void printarraytoBuf(int[] arr){
		for(int o: arr){
			buf.append(o);
			print(",");
		}
		println("");
	}
	
	public static <T> void printarraytoBuf(T[] arr){
		for(T o: arr){
			print(o.toString());
			print(",");
		}
		println("");
	}
	
	public static void print(String str){
		buf.append(str);
	}
	public static void println(String str){
		buf.append(str);
		buf.append(System.getProperty("line.separator"));
	}
	public static void flushBuffer(String filename){
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
	
	public static String getBufferString(){
		return buf.toString();
	}
	
	public static void clearBuffer(){
		buf = new StringBuffer(1024);
	}
	
}
