package collabedit.document;

public class TimeStamp {
	private int timestamp[] = new int[50];
	
	/****
	 * default timestamp construction 
	 * intialist the timestamp
	 */
	public TimeStamp() {
		for(int i = 0; i < 50 ; i ++)
			timestamp[i] = 0;
	}
	public TimeStamp(int timestamp[]) {
		for(int i = 0 ; i < 50 ; i ++) {
			this.timestamp[i] = timestamp[i];
		}
	}
	
	public int[] getStateVector() {
		return timestamp;
	}
	
	public boolean isCasualityBefore(TimeStamp sv) {
		boolean flag = true;
		for(int i = 0 ; i < 50; i++) {
			if(timestamp[i] >sv.getStateVector()[i]) {
				flag = false;
			}
		}
		return flag;
	}
}
