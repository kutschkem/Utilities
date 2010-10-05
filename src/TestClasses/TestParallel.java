package TestClasses;

import static org.junit.Assert.*;
import kutschke.utility.ParallelProcess;

import org.junit.Before;
import org.junit.Test;

public class TestParallel {
    int TestVar = 0;
    
    @Before
    public void init(){
    	TestVar = 0;
    }
	
	@Test
	public void TestParallelity(){
		long time = System.currentTimeMillis();
		new ParallelProcess(){

			@Override
			protected void execute1() {
				for(int j = 0; j < 20; j++) Math.random();
				TestVar = 1;
				
			}

			@Override
			protected void execute2() {
				for(int j = 0; j < 50; j++) Math.random();
				TestVar = 2;
				
			}
			
		}.start();
		time = time - System.currentTimeMillis();
		assertEquals(2, TestVar);
		
		long time2 = System.currentTimeMillis();
		for(int j = 0; j < 20; j++) Math.random();
		for(int j = 0; j < 50; j++) Math.random();
		time2 = time2 - System.currentTimeMillis();
		
		System.out.println(time - time2);
		
	}
}
