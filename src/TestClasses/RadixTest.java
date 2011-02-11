package TestClasses;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
				
		RadixSort.sort(lst2, new NoThrowLambda <Double,Long>(){

			@Override
			public Long apply(Double arg) {
				return Double.doubleToLongBits(arg);
			}
			
		});

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
		
		RadixSort.sort(lst2, new NoThrowLambda<Double,Long>(){

			@Override
			public Long apply(Double arg) {
				return Double.doubleToLongBits(arg);
			}
			
		});
		
		assertArrayEquals(lst2.toArray(), array);

		
	}
}
