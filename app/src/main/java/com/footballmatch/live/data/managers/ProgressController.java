package com.footballmatch.live.data.managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import com.footballmatch.live.R;


public class ProgressController {
	private static final long TIMER_DELAY = 20000;
	
    private static ProgressDialog progDialog;
    private static boolean running;
    
    private static Handler handler;
    private static Runnable runnable;

    private ProgressController() {
    }

    /**
     * Start an ProgressDialog if there's is no other dialog running.
     * @param context
     */
    public static void startDialog(final Context context) {
    	try{
    		if (!running) {
                progDialog = ProgressDialog.show(context, "", context.getResources().getString(R.string.progress_loading));
                running = true;
            }
    	} catch(Exception e){
    		
    	}
        
    }
    
    public static void startDialogWithDelay(final Context context, boolean delayRemove) {
    	try{
    		if (!running) {
                progDialog = ProgressDialog.show(context, "", context.getResources().getString(R.string.progress_loading));
                running = true;
                if(delayRemove)
                	timerDelayRemoveDialog(TIMER_DELAY, progDialog);
            }
    	} catch(Exception e){
    		
    	}
        
    }

    /**
     * Stop current progress dialog. This should be called on stop of each activity.
     */
    public static void stopDialog() {
    	try{
    		if (progDialog != null) {
                if (progDialog.isShowing()) {
                    try
                    {
                        progDialog.dismiss(); 
                    }
                    catch(Exception e)
                    {
                        
                    }
                }
            }
    	} catch(Exception e){
    		
    	}
        
        running = false;
        progDialog = null;
        stopDelay();
    }
    
    private static void timerDelayRemoveDialog(long time, final ProgressDialog d){
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
            	try{
            		if(running){
                		if(d != null && d.isShowing())
                		{
                		    try
                		    {
                		        d.dismiss();
                		    }
                		    catch(Exception e)
                		    {
                		        
                		    }
                		}
                			
                		running = false;
                		progDialog = null;
                	}
            	} catch(Exception e){
            		
            	}
            }
        };
        
        handler.postDelayed(runnable, time); 
    }
    
    private static void stopDelay(){
    	try{
    		if(handler != null && runnable != null){
        		handler.removeCallbacks(runnable);
        	}
    	}
    	catch(Exception e){
    		
    	}
    	
    	handler = null;
    	runnable = null;
    }
}
