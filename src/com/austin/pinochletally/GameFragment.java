package com.austin.pinochletally;

import com.austin.pinochletally.HistoryContract.HistoryEntry;

import android.R.color;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GameFragment extends Fragment implements OnClickListener {
		
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
		
		EditText name1 = (EditText)getActivity().findViewById(R.id.team1name);
		String name1val;
		if(name1.getText().length() < 1) {
			name1val = "Team 1";
		}
		else {
			name1val = name1.getText().toString();
		}
		
		EditText name2 = (EditText)getActivity().findViewById(R.id.team2name);
		String name2val;
		if(name2.getText().length() < 1) {
			name2val = "Team 2";
		}
		else {
			name2val = name2.getText().toString();
		}
		
		EditText meld1 = (EditText)getActivity().findViewById(R.id.team1_meld);
		if(meld1.getText().length() < 1) {
			// error
			err.setText("Invalid team 1 meld amount");
			err.invalidate();
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
		
		// check if game has ended
		if((team1 && mTeam1Score > 1500) || (mTeam1Score > 1500 && mTeam2Score < 1500)) {
			// bid and go out, or don't bid and go out when the other team doesn't
			
			HistoryDbHelper mDbHelper = new HistoryDbHelper(getActivity());
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			
			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(HistoryEntry.COLUMN_NAME_WINNING_TEAM, name1val);
			values.put(HistoryEntry.COLUMN_NAME_WINNING_SCORE, mTeam1Score);
			values.put(HistoryEntry.COLUMN_NAME_LOSING_TEAM, name2val);
			values.put(HistoryEntry.COLUMN_NAME_LOSING_SCORE, mTeam2Score);
			
			// Insert the new row, returning the primary key value of the new row
			long newRowId;
			newRowId = db.insert(
			         HistoryEntry.TABLE_NAME,
			         null,
			         values);
			
			err.setTextColor(color.holo_green_light);
			err.setText("Team 1 wins! Game saved to History");
		}
		else if(mTeam2Score > 1500) {
			// other team didn't win, and we went out, so we win
			
			HistoryDbHelper mDbHelper = new HistoryDbHelper(getActivity());
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			
			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(HistoryEntry.COLUMN_NAME_WINNING_TEAM, name2val);
			values.put(HistoryEntry.COLUMN_NAME_WINNING_SCORE, mTeam2Score);
			values.put(HistoryEntry.COLUMN_NAME_LOSING_TEAM, name1val);
			values.put(HistoryEntry.COLUMN_NAME_LOSING_SCORE, mTeam1Score);
			
			// Insert the new row, returning the primary key value of the new row
			long newRowId;
			newRowId = db.insert(
			         HistoryEntry.TABLE_NAME,
			         null,
			         values);
			
			err.setTextColor(color.holo_green_light);
			err.setText("Team 2 wins! Game saved to History");
		}
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