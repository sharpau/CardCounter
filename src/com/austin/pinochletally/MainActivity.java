package com.austin.pinochletally;

import java.util.Locale;

import junit.framework.Assert;

import android.R.color;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		mViewPager.setCurrentItem(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			Fragment fragment;
			Bundle args;
			switch(position) {
			case 0:
				fragment = new HistoryFragment();
				args = new Bundle();
				fragment.setArguments(args);
				break;
			case 1:
				fragment = new GameFragment();
				args = new Bundle();
				fragment.setArguments(args);
				break;
			case 2:
				fragment = new ScoringFragment();
				args = new Bundle();
				fragment.setArguments(args);
				break;
			default:
				// Return a DummySectionFragment (defined as a static inner class
				// below) with the page number as its lone argument.
				fragment = new DummySectionFragment();
				args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	public static class HistoryFragment extends Fragment {

		public HistoryFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_history,
					container, false);

			return rootView;
		}
	}
	
	public static class GameFragment extends Fragment implements OnClickListener {
		
		Integer mTeam1Score = 0;
		Integer mTeam2Score = 0;

		public GameFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_game,
					container, false);
			
			// click listener
	        Button addBtn = (Button) rootView.findViewById(R.id.add_scores);
	        addBtn.setOnClickListener(this);
			
			return rootView;
		}
		
		@Override
		public void onClick(View view) {
			switch(view.getId()) {
			case R.id.add_scores:
				addScores(view);
				break;
			default:
				break;
			
			}
			
		}
		
		public void addScores(View view) {
			/*
			 * lots of error checking
			 * 		melds & tricks have numbers (fail = do nothing)
			 * 		tricks add to 250 (fail = do nothing)
			 * 		bid value entered (fail = do nothing)
			 * 
			 * add meld & trick scores to each team's scroll, recalc total score
			 * if game over, announce win, ask about archiving game
			 * 
			 */
			TextView err = (TextView)getActivity().findViewById(R.id.error_msg);
			err.setTextColor(color.holo_red_dark);
			
			EditText meld1 = (EditText)getActivity().findViewById(R.id.team1_meld);
			if(meld1.getText().length() < 1) {
				// error
				err.setText("Invalid team 1 meld amount");
				return;
			}
			int meld1val = Integer.parseInt(meld1.getText().toString());

			EditText meld2 = (EditText)getActivity().findViewById(R.id.team2_meld);
			if(meld2.getText().length() < 1) {
				// error
				err.setText("Invalid team 2 meld amount");
				return;
			}
			int meld2val = Integer.parseInt(meld2.getText().toString());

			EditText trick1 = (EditText)getActivity().findViewById(R.id.team1_tricks);
			if(trick1.getText().length() < 1) {
				// error
				err.setText("Invalid team 1 trick amount");
				return;
			}
			int trick1val = Integer.parseInt(trick1.getText().toString());

			EditText trick2 = (EditText)getActivity().findViewById(R.id.team2_tricks);
			if(trick2.getText().length() < 1) {
				// error
				err.setText("Invalid team 2 trick amount");
				return;
			}
			int trick2val = Integer.parseInt(trick2.getText().toString());

			EditText bid = (EditText)getActivity().findViewById(R.id.bid);
			if(bid.getText().length() < 3) {
				// error
				err.setText("Invalid bid amount");
				return;
			}
			int bidval = Integer.parseInt(bid.getText().toString());
			
			ToggleButton team = (ToggleButton)getActivity().findViewById(R.id.bidding_team);
			Boolean team1 = team.isChecked();
			
			
			if(trick1val + trick2val != 250) {
				// error
				err.setText("Trick values do not add to 250");
				return;
			}
			if(bidval < 250) {
				// error
				err.setText("Bid value below 250");
				return;
			}
			
			if(team1) {
				if(bidval > meld1val + trick1val) {
					// set
					addTeam1Score(0);
					addTeam1Score(-1 * bidval);
				}
				else {
					// made it
					addTeam1Score(meld1val);
					addTeam1Score(trick1val);
				}
				if(trick2val > 0) {
					// saved meld
					addTeam2Score(meld2val);
					addTeam2Score(trick2val);
				}
			}
			else {
				if(bidval > meld2val + trick2val) {
					// set
					addTeam2Score(0);
					addTeam2Score(-1 * bidval);
				}
				else {
					// made it
					addTeam2Score(meld2val);
					addTeam2Score(trick2val);
				}
				if(trick1val > 0) {
					// saved meld
					addTeam1Score(meld1val);
					addTeam1Score(trick1val);
				}
			}

			// update total scores
			TextView score1 = (TextView)getActivity().findViewById(R.id.team1_score);
			score1.setText(mTeam1Score.toString());
			
			TextView score2 = (TextView)getActivity().findViewById(R.id.team2_score);
			score2.setText(mTeam2Score.toString());
			
			// zero out various entries
			meld1.setText("");
			trick1.setText("");
			meld2.setText("");
			trick2.setText("");
			bid.setText("");
			
			err.setText("");
		}
		
		private void addTeam1Score(Integer value) {
			mTeam1Score += value;
			LinearLayout scrollArea = (LinearLayout)getActivity().findViewById(R.id.team1_list);
			// add textview
			TextView newScore = new TextView(getActivity());
			newScore.setText(value.toString());
			
			scrollArea.addView(newScore);
		}
		
		private void addTeam2Score(Integer value) {
			mTeam2Score += value;
			LinearLayout scrollArea = (LinearLayout)getActivity().findViewById(R.id.team2_list);
			// add textview
			TextView newScore = new TextView(getActivity());
			newScore.setText(value.toString());
			
			scrollArea.addView(newScore);
		}
	}
	
	public static class ScoringFragment extends Fragment implements OnClickListener {

		public ScoringFragment() {
		}
		
		static Boolean mSimpleScoring = true;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_scoring,
					container, false);
			
			// click listener
	        Button switchBtn = (Button) rootView.findViewById(R.id.switch_scoring_button);
	        switchBtn.setOnClickListener(this);
			
			// layout for meld items
			GridLayout meldLayout = (GridLayout)rootView.findViewById(R.id.fragment_scoring_melds);

			String[] melds = getResources().getStringArray(R.array.melds);
			String[] meldVals = getResources().getStringArray(R.array.meld_values);
			
			Assert.assertEquals(melds.length, meldVals.length);
			
			for(int i = 0; i < melds.length; i++) {
				TextView meld = new TextView(getActivity());
				meld.setText(melds[i]);
				meldLayout.addView(meld, new GridLayout.LayoutParams());
				
				TextView meldVal = new TextView(getActivity());
				meldVal.setText(meldVals[i]);
				LayoutParams rightJust = new GridLayout.LayoutParams();
				rightJust.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.RIGHT);
				meldLayout.addView(meldVal, rightJust);
			}
			
			// layout for simple trick scoring
			GridLayout simpleTrickLayout = (GridLayout)rootView.findViewById(R.id.fragment_scoring_tricks_simple);

			String[] simpleTricks = getResources().getStringArray(R.array.tricks);
			String[] simpleTrickValues = getResources().getStringArray(R.array.trick_values);
			
			Assert.assertEquals(simpleTricks.length, simpleTrickValues.length);
			
			for(int i = 0; i < simpleTricks.length; i++) {
				TextView trick = new TextView(getActivity());
				trick.setText(simpleTricks[i]);
				simpleTrickLayout.addView(trick, new GridLayout.LayoutParams());
				
				TextView val = new TextView(getActivity());
				val.setText(simpleTrickValues[i]);
				LayoutParams rightJust = new GridLayout.LayoutParams();
				rightJust.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.RIGHT);
				simpleTrickLayout.addView(val, rightJust);
			}
			
			
			// layout for non-simple trick scoring
			GridLayout trickLayout = (GridLayout)rootView.findViewById(R.id.fragment_scoring_tricks);

			String[] tricks = getResources().getStringArray(R.array.tricks_alt);
			String[] trickValues = getResources().getStringArray(R.array.trick_alt_values);
			
			Assert.assertEquals(tricks.length, trickValues.length);

			for(int i = 0; i < tricks.length; i++) {
				TextView trick = new TextView(getActivity());
				trick.setText(tricks[i]);
				trickLayout.addView(trick, new GridLayout.LayoutParams());
				
				TextView val = new TextView(getActivity());
				val.setText(trickValues[i]);
				LayoutParams rightJust = new GridLayout.LayoutParams();
				rightJust.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.RIGHT);
				trickLayout.addView(val, rightJust);
			}
			
			trickLayout.setVisibility(View.GONE);
			
			return rootView;
		}
		
		// swap the two tricks layouts
		public void SwitchTricks(View view) {
			View root = getView();
			if(mSimpleScoring) {
				mSimpleScoring = false;
				
				GridLayout trickLayout = (GridLayout)root.findViewById(R.id.fragment_scoring_tricks);
				trickLayout.setVisibility(View.VISIBLE);
				
				GridLayout simpleTrickLayout = (GridLayout)root.findViewById(R.id.fragment_scoring_tricks_simple);
				simpleTrickLayout.setVisibility(View.GONE);
			}
			else {
				mSimpleScoring = true;
				
				GridLayout trickLayout = (GridLayout)root.findViewById(R.id.fragment_scoring_tricks);
				trickLayout.setVisibility(View.GONE);
				
				GridLayout simpleTrickLayout = (GridLayout)root.findViewById(R.id.fragment_scoring_tricks_simple);
				simpleTrickLayout.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onClick(View view) {
			switch(view.getId()) {
			case R.id.switch_scoring_button:
				SwitchTricks(view);
				break;
			default:
				break;
			
			}
			
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

}
