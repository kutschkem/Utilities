package kutschke.utility;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kutschke.higherClass.NoThrowLambda;

public class RadixSort {

	final private static int base = 8;
	
	public static <T> void sort(T[] Objects, final NoThrowLambda<T,Long> fkt){
		final int k = 1 << base;
		for(int i = 0; i < 64/base; i++){
			final int j = i;
			CountingSort(Objects, new NoThrowLambda<T,Integer>(){

				@Override
				public Integer apply(T arg) {
					return Long.valueOf((fkt.apply(arg) >>> (base*j)) % k).intValue();
				}
				
			}, k);
		}
	}
	
	
	/**
	 * f�hrt einen RadixSort auf einer Liste durch
	 * @param <T>
	 * @param Objects
	 * @param fkt
	 * @see sort
	 */
	@SuppressWarnings("unchecked")
	public static <T> void sort(List<T> Objects, final NoThrowLambda<T,Long> fkt){
		T[] arr = (T[]) Objects.toArray();
		sort(arr, fkt);
		
		try{
			Objects.clear();
			Objects.addAll(Arrays.asList(arr));
		}catch(UnsupportedOperationException ex){
			for(int i=0; i < arr.length;i++)
			Objects.set(i, arr[i]);
			}
		/*final int k = 1 << base;
		List<T> lst = Objects;
		for(int i = 0; i < 64/base; i++){
			final int j = i;
			lst = BucketSort(lst, new Lambda<T,Integer>(){

				@Override
				public Integer apply(T arg) {
					return Long.valueOf((fkt.apply(arg) >>> (base*j)) % k).intValue();
				}
				
			}, k);
		}
		Objects.clear();
		Objects.addAll(lst);*/
	}
	
	@SuppressWarnings("unchecked")
	private static <T> void CountingSort(T[] Objects, NoThrowLambda<T,Integer> fkt, int maxBuckets){
		int[] counter = new int[maxBuckets];
		for(T object :Objects){						//Anzahlen zaehlen
			int i = fkt.apply(object);
			counter[i]++;
		}

		for(int i = 1; i < counter.length; i++){    // kleiner-gleich Elemente z�hlen
			counter[i] += counter[i-1];
		}

		Object[] temp = new Object[Objects.length];					//Objekte an die richtige Position tun
		System.arraycopy(Objects, 0, temp, 0, Objects.length);
		for(int i = Objects.length -1; i >= 0; i--){
			int j = fkt.apply((T)temp[i]);
			counter[j]--;										// hier abziehen, da das Objekt selbst mitgez�hlt wurde (offset 1)
			Objects[counter[j]] = (T) temp[i];
		}
			
	}
	
	@SuppressWarnings("unchecked")
	private static <T> List<T> BucketSort(List<T> lst, NoThrowLambda<T,Integer> fkt, int maxbuckets){
		List<T>[] lists = new LinkedList[maxbuckets];
		for(int i=0; i < maxbuckets; i++)
			lists[i] = new LinkedList<T>();
		for(T t: lst)
			lists[fkt.apply(t) % maxbuckets].add(t);
		List<T> result = new LinkedList<T>();
		for(List<T> l : lists)
			result.addAll(l);
		return result;
	}
}
