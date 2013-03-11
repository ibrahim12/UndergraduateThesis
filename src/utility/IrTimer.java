/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

/**
 * 
 * @author Ibrahim
 */
public class IrTimer {

	private long startTime = 0;
	private long endTime = 0;
	private long elapsedTime = 0;
	private long timeout = -1;
	private boolean isRunning = false;

	public IrTimer(long timeout) {
		this.timeout = timeout;
	}
	
	public IrTimer() {
		
	}

	public void Start() {
		isRunning = true;
		startTime = System.currentTimeMillis();
	}

	public void Restart() {
		isRunning = true;
		startTime = System.currentTimeMillis();
	}

	public void End() {
		endTime = System.currentTimeMillis();
		elapsedTime = endTime - startTime;
		isRunning = false;
	}

	public boolean isTimedOut() {
		elapsedTime = System.currentTimeMillis() - startTime;
		return (elapsedTime >= timeout);
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
}
