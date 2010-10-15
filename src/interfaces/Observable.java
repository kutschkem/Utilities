package interfaces;

public interface Observable<T> {

	public void addObserver(Observer<T> observer);
	public void deleteObserver(Observer<T> observer);
	
}
