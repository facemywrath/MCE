package facejup.mce.util;

public class Marker<T>{
	
	private T item;
	private Long time;
	
	public Marker(T item)
	{
		this.item = item;
		this.time = System.currentTimeMillis();
	}
	
	public int getSecondsPassedSince()
	{
		return (int) ((System.currentTimeMillis() - time)/1000.0);
	}
	
	public T getItem()
	{
		return this.item;
	}
	
	public Long getMillisPassedSince()
	{
		return System.currentTimeMillis() - time;
	}

}
