package com.deepak.speechconverter;

import java.util.HashMap;
import java.util.Locale;

import com.deepak.speechconverter.SpeechConverter.MyPhoneStateListener;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NumberTeller extends Service{

	
	TextToSpeech txtospeech;
	MyPhoneStateListener myPhoneListener;
	HashMap<String, String> hs;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		myPhoneListener = new MyPhoneStateListener();

		TelephonyManager telManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		telManager.listen(myPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

	}
	
	 public class MyPhoneStateListener extends PhoneStateListener{
	    	
	    	@Override
	    	public void onCallStateChanged(int state, final String incomingNumber) {
	    		// TODO Auto-generated method stub
	    		Log.d("Deepak", "Deepak: onCallState changed");
	    		super.onCallStateChanged(state, incomingNumber);
	    		
	    		hs = new HashMap<String, String>();
	            hs.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_RING));

	    		if(state == TelephonyManager.CALL_STATE_RINGING){
	    			
	    			
	    			Log.d("Deepak", "Deepak: Number = "+incomingNumber);
	    			
	    			Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));
	    			
	    			Cursor c = getContentResolver().query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.NUMBER}, null, null, null);
	    			if(c != null && c.getCount() >= 1){
	    				c.moveToFirst();
	    				final String name = c.getString(0);
	    				Log.d("Deepak", "Deepak: Cursor != null. Name = "+name);
	    				if(name != null){
	    					txtospeech = new TextToSpeech(NumberTeller.this, new OnInitListener() {

	    		    			public void onInit(int status) {
	    		    				// TODO Auto-generated method stub
	    		    				Log.d("Deepak", "Deepak: Status = "+status);
	    		    				txtospeech.setLanguage(Locale.FRENCH);
	    		    				txtospeech.setSpeechRate((float) 0.75);
	    		    				txtospeech.speak("You have a call from "+name, TextToSpeech.QUEUE_FLUSH, hs);
	    		    				
	    		    			}
	    		    		});
			    			
	    				}
	    				else {
	    					txtospeech = new TextToSpeech(NumberTeller.this, new OnInitListener() {

	    		    			public void onInit(int status) {
	    		    				// TODO Auto-generated method stub
	    		    				Log.d("Deepak", "Deepak: Status = "+status);
	    		    				txtospeech.setLanguage(Locale.US);
	    		    				txtospeech.speak(incomingNumber, TextToSpeech.QUEUE_FLUSH, hs);
	    		    			}
	    		    		});
	    				}
	    			}
	    			else
	    				txtospeech = new TextToSpeech(NumberTeller.this, new OnInitListener() {

    		    			public void onInit(int status) {
    		    				// TODO Auto-generated method stub
    		    				Log.d("Deepak", "Deepak: Status = "+status);
    		    				txtospeech.speak(incomingNumber, TextToSpeech.QUEUE_FLUSH, hs);
    		    			}
    		    		});
	    			
	    			txtospeech.setOnUtteranceCompletedListener(new OnUtteranceCompletedListener() {

		    			public void onUtteranceCompleted(String utteranceId) {
		    				// TODO Auto-generated method stub
		    				Log.d("Deepak", "Deepak: utterance completed");
		    				txtospeech.stop();
		    				txtospeech.shutdown();
		    			}
		    		});
	    		}
	    		/*else if(state == TelephonyManager.CALL_STATE_OFFHOOK){
	    			SpeechRecognizer speechRecog = SpeechRecognizer.createSpeechRecognizer(getBaseContext());
	    			speechRecog.setRecognitionListener()
	    		}*/
	    	}
	    }

}
