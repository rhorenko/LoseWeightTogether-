package mast.avalons;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryActivity extends ListActivity {
    private static final int IDM_ADD = 101;
    private static final int IDM_EDIT = 102;
    private static final int IDM_DELETE = 103;
    public String date;
    private Cursor mCursor; 
    private ListAdapter mAdapter;
    
    
    private static final String[] mContent = new String[] {
            DbHelper._ID, DbHelper.DATE,
            DbHelper.WEIGHT};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);   
        //�������� ������ � ������� - ���������� (������ ����� ���� �� �������� � ��� ����)
        //�������� _ID DESC ��������� ������ � ������� � ���� ������ �� ��������
        mCursor = managedQuery(
                Provider.CONTENT_URI, mContent, null, null,"_ID DESC");
        //������� �������� ������ ��� ListView
       //String [] bufferDate=new String[mCursor.getCount()];
       //String [] bufferWeight=new String[mCursor.getCount()];
       //for (int i1 = 0; i1 < mCursor.getCount(); i1++) {
			//mCursor.moveToPosition(i1);
			//bufferDate[i1]=mCursor.getString(0);
			//bufferWeight[i1]=mCursor.getString(1);
			//Log.d("----", "--"+Integer.toString(i1)+"--"+mCursor.getString(0)); 
		//}
       //String [] buffer=new String[]{bufferDate,bufferWeight};
        mAdapter = new SimpleCursorAdapter(this, 
                R.layout.history, mCursor, 
                new String[] {DbHelper.DATE, DbHelper.WEIGHT}, 
                new int[] {R.id.name, R.id.phone});
        setListAdapter(mAdapter);
   }
  //������� ���� ��� ��� ����������, ��������������, �������� ������� � ���� ������
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, IDM_ADD, Menu.NONE, R.string.menu_add)
            .setIcon(R.drawable.ic_menu_add);
        menu.add(Menu.NONE, IDM_EDIT, Menu.NONE, R.string.menu_edit)
            .setIcon(R.drawable.ic_menu_edit);
        menu.add(Menu.NONE, IDM_DELETE, Menu.NONE, R.string.menu_delete)
            .setIcon(R.drawable.ic_menu_delete);

        return(super.onCreateOptionsMenu(menu));
    }
    //������������ ������� �� ������ ����
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final long id = this.getSelectedItemId();
        
        switch (item.getItemId()) {
            case IDM_ADD: {
            	String[] months = getResources().getStringArray(R.array.month);
            	Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
                final String date = Integer.toString(calendar.get(Calendar.YEAR))+"  "+
             			  months[calendar.get(Calendar.MONTH)]+"  "+
             			  Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))+"  "+
             		      Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)+4)+" : "+
             			  Integer.toString(calendar.get(Calendar.MINUTE))+" : "+
             			  Integer.toString(calendar.get(Calendar.SECOND))+"  ";
                CallAddDialog(date);
            }  
                break;
            case IDM_EDIT:
                if (id > 0) {     
                    CallEditDialog(id);
                }
                else {
                    Toast.makeText(this, R.string.toast_notify, Toast.LENGTH_SHORT)
                        .show();
                }
                break;
            case IDM_DELETE:
                if (id > 0) {
                    CallDeleteDialog(id);
                }
                else {
                    Toast.makeText(this, R.string.toast_notify, Toast.LENGTH_SHORT)
                        .show();
                }
                break;
        }
        return(super.onOptionsItemSelected(item));
    }
    //����� ������� ��� ���������� ������� � ���� ������
    private void CallAddDialog(final String date) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View root = inflater.inflate(R.layout.dialog, null);
        final TextView output_date = (TextView)root.findViewById(R.id.output_date_text);
        final EditText textWeight = (EditText)root.findViewById(R.id.weight);
        
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        root.setBackgroundResource(R.drawable.mastbackground);
        output_date.setText("Date:       "+date);
        b.setView(root);
        b.setTitle(R.string.title_add);
        b.setPositiveButton(
                R.string.btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ContentValues values = new ContentValues(2);
                
                values.put(DbHelper.DATE, date);
                values.put(DbHelper.WEIGHT, textWeight.getText().toString());
                
                getContentResolver().insert(Provider.CONTENT_URI, values);
                mCursor.requery();
            }
        });
        b.setNegativeButton(
                R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        b.show(); 
    }
  //����� ������� ��� ��������������  ������� � ���� ������
    private void CallEditDialog(final long id) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View root = inflater.inflate(R.layout.dialog, null);
        
        final TextView output_date = (TextView)root.findViewById(R.id.output_date_text);               
        final EditText textWeight = (EditText)root.findViewById(R.id.weight);
        
        mCursor.moveToPosition(this.getSelectedItemPosition());
        output_date.setText(mCursor.getString(1));
        textWeight.setText(mCursor.getString(2));
        
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setView(root);
        b.setTitle(R.string.title_edit);
        
        b.setPositiveButton(
                R.string.btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              ContentValues values = new ContentValues(2);
              
              values.put(DbHelper.DATE, output_date.getText().toString());
              values.put(DbHelper.WEIGHT, textWeight.getText().toString());
              
              getContentResolver().update(
            		  Provider.CONTENT_URI, values, "_ID=" + id, null);
              mCursor.requery();
            }
        });
        
        b.setNegativeButton(
                R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        
        b.show(); 
    }
  //����� ������� ��� ��������  ������� � ���� ������
    private void CallDeleteDialog(final long id) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.title_delete);
        
        b.setPositiveButton(
                R.string.btn_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                getContentResolver().delete(
                		Provider.CONTENT_URI, "_ID=" + id, null);
                mCursor.requery();
            }
        });
        
        b.setNegativeButton(
                R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });
        
        b.show();
    }
}
