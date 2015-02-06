package cyber_homework.game;

/**
 * 時間を計測するインスタンスを生成するクラスです
 * @author Masayuki Yarimizu
 *
 */
public class Timer {
	
	private long time;
	private long sec;
	private long min;
	private long hour;
	private long startTime;
	private long finishTime;
	
	public void setTime() {
		this.startTime = System.currentTimeMillis();
	}
	
	public String getPastTime() {
		
		this.finishTime = System.currentTimeMillis();
		time = finishTime - startTime;
		sec = ((time % (1000*60*60*60)) /1000) % 60;
		min = ((time % (1000*60*60))/1000/60) % 60;
		hour = (time / (1000*60*60));
		
		if (hour == 0 && min == 0) {
			return sec + "秒";
		}
		
		if (hour == 0) {
			return min + "分" + sec + "秒";
		} 
		
		return hour + "時間" + min + "分" + sec + "秒";
	}
	
}
