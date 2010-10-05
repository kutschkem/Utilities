package kutschke.utility;

public abstract class ParallelProcess {

	
	private Boolean lock = new Boolean(false);
	private boolean start = false;
	public final void start(){
		new Thread(){
			@Override
			public void run(){
				start = true;
				synchronized(lock){
					execute2();
				}
				
			}
		}.start();
		while(!start);
		execute1();
		synchronized(lock){
			lock = true;
		}
	}
	
	protected abstract void execute1();
	protected abstract void execute2();
}
