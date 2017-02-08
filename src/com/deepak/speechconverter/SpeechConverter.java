package com.deepak.speechconverter;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SpeechConverter extends Activity {
	
	 TextToSpeech txtospeech;
	 MyPhoneStateListener myPhoneListener;
	 HashMap<String, String> hs;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        myPhoneListener = new MyPhoneStateListener();
        
        TelephonyManager telManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        //telManager.listen(myPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        
        
        
     // Check to see if a recognition activity is present
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(
          new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
          Log.d("Deepak", "Deepak: Contains Speech recognizwer");
        } else {
        	Log.d("Deepak", "Deepak: Does not contain Speech recognizwer");
        }

        
        
        
        startService(new Intent(this, NumberTeller.class));
        /*txtospeech = new TextToSpeech(this, new OnInitListener() {
			
			public void onInit(int status) {
				// TODO Auto-generated method stub
				Log.d("Deepak", "Deepak: Status = "+status);
				txtospeech.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {
					
					public void onUtteranceCompleted(String utteranceId) {
						// TODO Auto-generated method stub
						Log.d("Deepak", "Deepak: utterance completed");
					}
				});
				txtospeech.setLanguage(Locale.US);
		        txtospeech.speak("Hello. How are you? 9964759905", TextToSpeech.QUEUE_ADD, null);
			}
		});
        
        hs = new HashMap<String, String>();
        hs.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_RING));
        */
//        txtospeech.setLanguage(Locale.US);
//        txtospeech.speak("Hello. How are you?", TextToSpeech.QUEUE_FLUSH, null);
        
        
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	//txtospeech.stop();
    	//txtospeech.shutdown();
    }
    
    public class MyPhoneStateListener extends PhoneStateListener{
    	
    	@Override
    	public void onCallStateChanged(int state, String incomingNumber) {
    		// TODO Auto-generated method stub
    		Log.d("Deepak", "Deepak: onCallState changed");
    		super.onCallStateChanged(state, incomingNumber);
    		if(state == TelephonyManager.CALL_STATE_RINGING){
    			txtospeech.setLanguage(Locale.US);
    			txtospeech.speak(incomingNumber, TextToSpeech.QUEUE_FLUSH, hs);
    			
    			String number = PhoneNumberUtils.toCallerIDMinMatch(incomingNumber);
    			Log.d("Deepak", "Deepak: Number = "+number);
    			
    			
    		}
    		
    	}
    }
}