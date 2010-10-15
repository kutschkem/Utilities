package interfaces;

public interface Observer<T> {

	public void update(Observable<T> observable, T arg );
	
}
