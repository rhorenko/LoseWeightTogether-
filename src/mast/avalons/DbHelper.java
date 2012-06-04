package mast.avalons;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbHelper extends SQLiteOpenHelper 
        implements BaseColumns {
    
    public static final String TABLE_NAME = "contact";
    public static final String DATE = "date";
	public static final String WEIGHT = "weight";
	
    public DbHelper(Context context) {
        super(context, Provider.DB_HISTORY, null, 1);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME 
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                + DATE + " TEXT, " + WEIGHT + " TEXT);");
        
        ContentValues values = new ContentValues();
                      
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "59.511");
        db.insert(TABLE_NAME, DATE, values);  
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "59.6");
        db.insert(TABLE_NAME, DATE, values);             
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "59.6");
        db.insert(TABLE_NAME, DATE, values);      
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "59.800");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "59.700");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "59.999");
        db.insert(TABLE_NAME, DATE, values);  
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "59.999");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.034");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.143");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.112");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.033");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.2");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.3");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.02");
        db.insert(TABLE_NAME, DATE, values);
       
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.022");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.223");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.111");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.3");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.02");
        db.insert(TABLE_NAME, DATE, values);
       
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.022");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.223");
        db.insert(TABLE_NAME, DATE, values);
        
        values.put(DATE, "2012 February 12 16 : 59 : 28");
        values.put(WEIGHT, "60.111");
        db.insert(TABLE_NAME, DATE, values);

    }

    
    @Override
    public void onUpgrade(
            SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }   
}