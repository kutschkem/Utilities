package kutschke.utility;

import java.util.Iterator;

public class MultiIterator<T> implements Iterator<T> {
	
	Iterator<? extends Iterable<? extends T>> superIterator;
	Iterator<? extends T> currentIterator;
	
	public MultiIterator(Iterable<? extends Iterable<? extends T>> collection){
		superIterator = collection.iterator();
		currentIterator = nextIterator();
	}
	
	private Iterator<? extends T> nextIterator(){
		while(superIterator.hasNext()){
			currentIterator = superIterator.next().iterator();
			if(currentIterator.hasNext()) return currentIterator;
		}
		return null;
	}

	@Override
	public boolean hasNext() {
		if(currentIterator == null)
		return false;
		if(currentIterator.hasNext()) return true;
		currentIterator = nextIterator();
		return hasNext();
	}

	@Override
	public T next() {
		if(! hasNext())
		return null;
		return currentIterator.next();
	}

	@Override
	public void remove() {
		currentIterator.remove();

	}
	
	public static <T> Iterable<T> iterable(final Iterable<? extends Iterable<? extends T>> collection){
		return new Iterable<T>(){

			@Override
			public Iterator<T> iterator() {
				return new MultiIterator<T>(collection);
			}
			
		};
	}

}
