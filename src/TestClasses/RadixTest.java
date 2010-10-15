package TestClasses;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import kutschke.higherClass.Lambda;
import kutschke.higherClass.NoThrowLambda;
import kutschke.utility.RadixSort;

import org.junit.Test;

public class RadixTest {

	

	@Test
	public void SortTest(){
		Long[] array = {4L,1L,40L,35L,1342L,231L,3421L,211L,3212L,45321L,2344564L,223113L,2345L,11L,233L,2L,1L,0L, Long.MAX_VALUE};
		List<Long> lst = Arrays.asList(4L,1L,40L,35L,1342L,231L,3421L,211L,3212L,45321L,2344564L,223113L,2345L,11L,233L,2L,1L,0L, Long.MAX_VALUE);
		
		RadixSort.sort(array, new NoThrowLambda<Long,Long>(){

			@Override
			public Long apply(Long arg) {
				return arg;
			}
			
		});
		Collections.sort(lst);
		assertArrayEquals(lst.toArray(),array);
		
		
	}
	
	@Test
	public void FloatTest(){
		Double[] array = { 0.43d, 0.431d, 1.3d, 2.1d, 0.0d, 0.1d, 1.1d, 4.3d, 5.2d, 5.1d};
		List<Double> lst = Arrays.asList(0.43d, 0.431d, 1.3d, 2.1d, 0.0d, 0.1d, 1.1d, 4.3d, 5.2d, 5.1d);
		List<Double> lst2 = Arrays.asList(0.43d, 0.431d, 1.3d, 2.1d, 0.0d, 0.1d, 1.1d, 4.3d, 5.2d, 5.1d);
		
		RadixSort.sort(array, new NoThrowLambda <Double,Long>(){

			@Override
			public Long apply(Double arg) {
				return Double.doubleToLongBits(arg);
			}
			
		});
		
		Collections.sort(lst);
		assertArrayEquals(lst.toArray(),array);
		
		String log1 = Testing.getBufferString();
		Testing.clearBuffer();
				
		RadixSort.sort(lst2, new NoThrowLambda <Double,Long>(){

			@Override
			public Long apply(Double arg) {
				return Double.doubleToLongBits(arg);
			}
			
		});
		
		String log2 = Testing.getBufferString();
		
		System.out.println( log1.equals(log2));
				
		Testing.printarray((Double[]) lst.toArray());
		Testing.printarray((Double[])lst2.toArray());
		Testing.flushBuffer("log.txt");
		assertArrayEquals(lst.toArray(),lst2.toArray());
		
	}
	
	@Test
	public void RandomTestD(){
		Random rand = new Random(42);
		Double[] array = new Double[1024];
		List<Double> lst = new LinkedList<Double>();
		List<Double> lst2 = new LinkedList<Double>();

		
		for(int i =0; i < 1024; i++){
			double l = rand.nextDouble();
			array[i] = l;
			lst.add(l);
			lst2.add(l);
		}
		
		RadixSort.sort(array, new NoThrowLambda<Double,Long>(){

			@Override
			public Long apply(Double arg) {
				return Double.doubleToLongBits(arg);
			}
			
		});
		
		Collections.sort(lst);
		assertArrayEquals(lst.toArray(), array);
		
		Testing.printarray(lst.toArray());
		Testing.printarray(array);
		
		RadixSort.sort(lst2, new NoThrowLambda<Double,Long>(){

			@Override
			public Long apply(Double arg) {
				return Double.doubleToLongBits(arg);
			}
			
		});
		
		Testing.printarray(lst2.toArray());
		
		Testing.printarraytoBuf(lst.toArray());
		String str1 = Testing.getBufferString();
		Testing.clearBuffer();
		Testing.printarraytoBuf(lst2.toArray());
		String str2 = Testing.getBufferString();
		Testing.clearBuffer();
		assertEquals(str1, str2);

		
	}
}
