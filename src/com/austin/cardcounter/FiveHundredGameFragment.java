package com.austin.cardcounter;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class FiveHundredGameFragment extends Fragment implements
		OnClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_500_game,
				container, false);
		
		return rootView;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

}
