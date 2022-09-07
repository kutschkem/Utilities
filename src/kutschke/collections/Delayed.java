package kutschke.collections;

import java.util.concurrent.TimeUnit;

public class Delayed<T> implements java.util.concurrent.Delayed {
	
	private T wrapped;
	private long startTime = System.currentTimeMillis();
	private long msDelay;
	
	public Delayed(T wrapped, long msDelay){
		this.wrapped = wrapped;
		this.msDelay = msDelay;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(startTime + msDelay - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	@Override
	public int compareTo(java.util.concurrent.Delayed o) {
		return Long.valueOf(msDelay).compareTo(Long.valueOf(o.getDelay(TimeUnit.MILLISECONDS)));
	}
	
	public T unwrap(){
		return wrapped;
	}

}
