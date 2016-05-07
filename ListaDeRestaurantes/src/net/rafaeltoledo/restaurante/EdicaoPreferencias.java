package net.rafaeltoledo.restaurante;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class EdicaoPreferencias extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferencias);
	}
}
