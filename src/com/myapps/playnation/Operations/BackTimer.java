package com.myapps.playnation.Operations;

import java.util.Timer;
import java.util.TimerTask;

public class BackTimer {
	Timer timer;
	private boolean canBack;

    public BackTimer() {  
    	canBack = false;
	}
    
    public void setTimer(int seconds)
    {
    	 timer = new Timer();
    	 canBack = true;
         timer.schedule(new RemindTask(), seconds*1000);
    }
    
    public boolean canBack()
    {
    	return canBack;
    }

    class RemindTask extends TimerTask {
        public void run() {
            canBack = false;
            timer.cancel(); //Terminate the timer thread
        }
    }
}
