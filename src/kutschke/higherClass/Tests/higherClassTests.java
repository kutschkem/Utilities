package kutschke.higherClass.Tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import kutschke.higherClass.AbstractFun;
import kutschke.higherClass.Lambda;

import org.junit.Assert;
import org.junit.Test;

public class higherClassTests {

	@Test
	public void filterTest() {
		Collection<Integer> intlst = new LinkedList<Integer>(Arrays.asList(1,
				2, 3));
		Collection<Integer> intlst2 = AbstractFun.filter(intlst,
				new Lambda<Integer, Boolean>() {
					public Boolean apply(Integer i) {
						return (i % 2) == 1;
					}
				});
		Integer[] compare = { 1, 3 };
		Assert.assertArrayEquals(compare, intlst2.toArray());
	}

	@Test
	public void mapTest() {
		Collection<Integer> intlst = new LinkedList<Integer>(Arrays.asList(3,
				5, 7));
		Collection<Boolean> boollst = AbstractFun.map(intlst,
				new Lambda<Integer, Boolean>() {
					@Override
					public Boolean apply(Integer i) {
						return i > 5;
					}
				});
		Boolean[] compare = { false, false, true };
		Assert.assertArrayEquals(compare, boollst.toArray());
	}
}
