package TestClasses;

import kutschke.numberStreams.NaturalNumbersStream;
import kutschke.numberStreams.OddNumFilterStream;


public class Maintest {

	
	public static void main(String[] args)
	{
		Iterable<Integer> intStr = new OddNumFilterStream(new NaturalNumbersStream(500));
		for(Integer i : intStr)
		{
			System.out.println(i);
			if(i > 1000) break;
		}
	}
}
