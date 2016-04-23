package bz.lenz.lwv;

import java.util.Calendar;

import bz.lenz.lwv.data.Datastore;
import bz.lenz.lwv.data.Day;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class PunchClockFragment extends Fragment {

	private Datastore ds;
    private Calendar month;
	private CalendarAdapter adapter;
	private Handler handler;
	private View rootView;
    
	private static final int REQUEST = 101;
	
	@Override
	public void onCreate (Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		ds=((MainActivity)getActivity()).getDatastore();
		ds.registerUpdateEventListener(
				new UpdateListener(){
					@Override
					public void onUpdate() {
						refreshCalendar(); //des ament als thread???
						/*
						 * runOnUIThread(new Runnable() {
                				public void run() {
                    				// refresh list view
                				}
            				}
						 */
					}
				}
				);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.punch_clock_fragment, container, false);
	    month = Calendar.getInstance();
	    
	    adapter = new CalendarAdapter(getActivity(), month);
	    
	    GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
	    gridview.setAdapter(adapter);
	    
	    handler = new Handler();
	    handler.post(calendarUpdater);
	    
	    TextView title  = (TextView) rootView.findViewById(R.id.title);
	    title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	    
	    TextView previous  = (TextView) rootView.findViewById(R.id.previous);
	    previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMinimum(Calendar.MONTH)) {				
					month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
				}
				refreshCalendar();
			}
		});
	    
	    TextView next  = (TextView) rootView.findViewById(R.id.next);
	    next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) {				
					month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
				} else {
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
				}
				refreshCalendar();
			}
		});
	    
		gridview.setOnItemClickListener(new OnItemClickListener() {
		    @Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		    	TextView date = (TextView)v.findViewById(R.id.date);
		        if(date instanceof TextView && !date.getText().equals("")) {
		        	String day = date.getText().toString();
		        	if(day.length()==1) {
		        		day = "0"+day;
		        	}
		        	Intent intent = new Intent(getActivity(), ModifyDateActivity.class);
		        	intent.putExtra("Day",ds.getDay(Integer.valueOf(date.getText().toString()),month.get(Calendar.MONTH),month.get(Calendar.YEAR)));
		        	intent.putExtra("Date", date.getText().toString()+"."+(month.get(Calendar.MONTH)+1)+"."+(month.get(Calendar.YEAR)-2000));
		        	intent.putStringArrayListExtra("Arbeiten", ds.getArbeiten());
		        	intent.putStringArrayListExtra("Geber", ds.getGeber());
		        	intent.putParcelableArrayListExtra("Wiesen", ds.getWiesen());
		        	startActivityForResult(intent,REQUEST);
		        }
		    }
		});
		return rootView;
    }
	
	@Override
	public void onDestroy (){
		super.onDestroy();
	}
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode==REQUEST){
    		if(resultCode==Activity.RESULT_OK){
    			Day day = (Day) data.getExtras().getParcelable("Day");
    			if(day!=null){
					if(day.getSumHours()!=0){
						//neuen tag hinzufuegen oder bereits vorhandenen aendern
        				ds.setDay(month.get(Calendar.YEAR),month.get(Calendar.MONTH),day);
        				refreshCalendar();
    				}else{
    					//bereits vorhondenen tog loeschen oder ignorieren wenn keine daten eingegeben und tag noch nicht vorhanden
    					ds.deleteDay(month.get(Calendar.YEAR),month.get(Calendar.MONTH),day);
        				refreshCalendar(); 
    				}
    			}
    		}
    	}
    }
    
    public void refreshCalendar()
	{
    	TextView title  = (TextView) rootView.findViewById(R.id.title);
		
		adapter.refreshDays();
		adapter.notifyDataSetChanged();				
		handler.post(calendarUpdater); //fuern aktuellen monat di besetzten tage markieren			
		
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}
	
	public void onNewIntent(Intent intent) {
		String date = intent.getStringExtra("date");
		String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
		month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
	}
	
	public Runnable calendarUpdater = new Runnable() {
		@Override
		public void run() {
			//adapter.setItems(ds.getMonth(month.get(Calendar.MONTH)));
			adapter.setItems(ds.getMonthOfYear(month.get(Calendar.MONTH),month.get(Calendar.YEAR)));
			adapter.notifyDataSetChanged();
		}
	};
}
