package net.rafaeltoledo.restaurante;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

public class TwitterService extends IntentService {
	
	public static final String PERFIL_EXTRA = "net.rafaeltoledo.PERFIL_EXTRA";
	public static final String MESSENGER_EXTRA = "net.rafaeltoledo.MESSENGER_EXTRA";

	public TwitterService() {
		super("TwitterService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Twitter t = new TwitterFactory().getInstance();
		Messenger messenger = (Messenger) intent.getExtras().get(MESSENGER_EXTRA);
		Message msg = Message.obtain();
		
		try {
			List<Status> resultado = t.getUserTimeline(intent.getStringExtra(PERFIL_EXTRA));
			
			msg.arg1 = Activity.RESULT_OK;
			msg.obj = resultado;
		} catch (Exception ex) {
			Log.e("ListaRestaurantes", "Erro manipulando timeline twitter", ex);
			msg.arg1 = Activity.RESULT_CANCELED;
			msg.obj = ex;
		}
		
		try {
			messenger.send(msg);
		} catch (Exception ex) {
			Log.w("ListaRestaurantes", "Erro enviando dados para a Activity", ex);
		}
	}
}
