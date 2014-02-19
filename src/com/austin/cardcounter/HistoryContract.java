package com.austin.cardcounter;

import android.provider.BaseColumns;

public final class HistoryContract {
	public HistoryContract() {}
	
	// defines columns of sql table
	public static abstract class HistoryEntry implements BaseColumns {
		public static final String TABLE_NAME = "history";
		public static final String COLUMN_NAME_WINNING_TEAM = "winningteam";
		public static final String COLUMN_NAME_WINNING_SCORE = "winningscore";
		public static final String COLUMN_NAME_LOSING_TEAM = "losingteam";
		public static final String COLUMN_NAME_LOSING_SCORE = "losingscore";

		private static final String TEXT_TYPE = " TEXT";
		private static final String INTEGER_TYPE = " INTEGER";
		private static final String COMMA_SEP = ",";
		
		public static final String SQL_CREATE_ENTRIES =
		    "CREATE TABLE " + HistoryEntry.TABLE_NAME + " (" +
    		HistoryEntry._ID + " INTEGER PRIMARY KEY," +
    		HistoryEntry.COLUMN_NAME_WINNING_TEAM + TEXT_TYPE + COMMA_SEP +
		    HistoryEntry.COLUMN_NAME_WINNING_SCORE + INTEGER_TYPE + COMMA_SEP +
		    HistoryEntry.COLUMN_NAME_LOSING_TEAM + TEXT_TYPE + COMMA_SEP +
		    HistoryEntry.COLUMN_NAME_LOSING_SCORE + INTEGER_TYPE +
		    " )";
		
		public static final String SQL_DELETE_ENTRIES =
		    "DROP TABLE IF EXISTS " + HistoryEntry.TABLE_NAME;
	}
}
