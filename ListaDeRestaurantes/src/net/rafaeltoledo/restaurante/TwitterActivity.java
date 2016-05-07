package net.rafaeltoledo.restaurante;

import java.util.List;

import twitter4j.Status;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TwitterActivity extends ListActivity {
	
	public static final String PERFIL = "net.rafaeltoledo.PERFIL";
	private StatusInstancia status = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		status = (StatusInstancia) getLastNonConfigurationInstance();
		
		if (status == null) {
			status = new StatusInstancia();
			status.handler = new HandlerTwitter(this);
			
			Intent i = new Intent(this, TwitterService.class);
			i.putExtra(TwitterService.PERFIL_EXTRA, getIntent().getStringExtra(PERFIL));
			i.putExtra(TwitterService.MESSENGER_EXTRA, new Messenger(status.handler));
			
			startService(i);
		} else {
			if (status.handler != null) {
				status.handler.anexar(this);
			}
			
			if (status.tweets != null) {
				atribuirTweets(status.tweets);
			}
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		if (status.handler != null) {
			status.handler.desanexar();
		}
		return status;
	}
	
	private void atribuirTweets(List<Status> tweets) {
		status.tweets = tweets;
		setListAdapter(new AdaptadorTweets(tweets));
	}
	
	private class AdaptadorTweets extends BaseAdapter {
		List<Status> status = null;
		
		AdaptadorTweets(List<Status> status) {
			super();
			this.status = status;
		}

		public int getCount() {
			return status.size();
		}

		public Object getItem(int position) {
			return status.get(position);
		}

		public long getItemId(int position) {			
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View linha = convertView;
			
			if (linha == null) {
				LayoutInflater inflater = getLayoutInflater();
				linha = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
			}
			
			Status item = (Status) getItem(position);
			((TextView) linha).setText(item.getText());
			
			return linha;
		}		
	}
	
	private void atirarErro(Throwable t) {
		Builder builder = new Builder(this);
		builder.setTitle("Erro!").setMessage(t.toString()).setPositiveButton("OK", null).show();
	}
	
	private static class HandlerTwitter extends Handler {		
		private TwitterActivity activity = null;
		
		HandlerTwitter(TwitterActivity activity) {
			anexar(activity);
		}
		
		void anexar(TwitterActivity activity) {
			this.activity = activity;
		}
		
		void desanexar() {
			activity = null;
		}
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == RESULT_OK) {
				activity.atribuirTweets((List<Status>) msg.obj);
			} else {
				activity.atirarErro((Exception) msg.obj);
			}
		}		
	}
	
	private static class StatusInstancia {
		List<Status> tweets = null;
		HandlerTwitter handler = null;
	}
}
