package bz.lenz.lwv;

import java.util.ArrayList;

import bz.lenz.lwv.data.Day;
import bz.lenz.lwv.data.Wiese;
import bz.lenz.lwv.data.Wiesen;
import bz.lenz.lwv.data.Work;
import android.os.Bundle;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;

public class ModifyDateActivity extends Activity {
	Day day;
	String date;
	ArrayList<String> arbeiten;
	ArrayList<String> geber;
	ArrayList<Wiesen> wiesen;
	// Parent view for all rows and the add button.
    private LinearLayout mContainerView;
    // The "Add new" button
    private Button mAddButton;
    private View lineAboveAdd;
    private ScrollView scroll;

    // There always should be only one empty row, other empty rows will
    // be removed.
    private View mExclusiveEmptyView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modify_date);
		// Show the Up button in the action bar.
		setupActionBar();
		
		mContainerView = (LinearLayout) findViewById(R.id.ParentView);
        mAddButton = (Button) findViewById(R.id.btnAddNewGeber);
        lineAboveAdd = (View) findViewById(R.id.lineAboveAdd);
        scroll=(ScrollView) findViewById(R.id.scrollView1);
		
		day = (Day) getIntent().getExtras().getParcelable("Day");
		date= getIntent().getExtras().getString("Date");
		arbeiten=new ArrayList<String>();
		arbeiten=getIntent().getStringArrayListExtra("Arbeiten");
		geber=new ArrayList<String>();
		geber=getIntent().getStringArrayListExtra("Geber");
		wiesen = getIntent().getParcelableArrayListExtra("Wiesen");
		setTitle("    "+date);
		
		if(day==null){
			inflateEditRow(0);
		}else{
			ArrayList<Work> work=day.getWork();
			String gs=null;
			View geber=null;
			for(int i=0;i<work.size();i++){
				if(gs==null){
					geber=inflateEditRow(work.get(i).getGeberID());
					gs=work.get(i).getGeber();
					inflateEditWork(work.get(i).getArbeit(),geber,work.get(i).getHours(),work.get(i).getGeberID(),work.get(i).getWieseID());
				}else{
					if(gs.equals(work.get(i).getGeber())){
						inflateEditWork(work.get(i).getArbeit(),geber,work.get(i).getHours(),work.get(i).getGeberID(),work.get(i).getWieseID());
					}else{
						geber=inflateEditRow(work.get(i).getGeberID());
						gs=work.get(i).getGeber();
						inflateEditWork(work.get(i).getArbeit(),geber,work.get(i).getHours(),work.get(i).getGeberID(),work.get(i).getWieseID());
					}
				}
			}
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modify_date, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//onClick handler for the "Add new" button;
    public void onAddNewClicked(View v) {
            // Inflate a new row and hide the button self.
            inflateEditRow(0); //null vorher
            v.setVisibility(View.GONE);
            lineAboveAdd.setVisibility(View.GONE);
            scroll.fullScroll(View.FOCUS_DOWN);
    }
    
    //onClick handler for the "Add new Work" button;
    public void onAddNewWorkClicked(View v) {
            // Inflate a new row and hide the button self.
            inflateEditWork(v);
            v.setVisibility(View.GONE);
            scroll.fullScroll(View.FOCUS_DOWN);
    }

    // onClick handler for the "X" button of each row
    public void onDeleteClicked(View v) {
            // remove the row by calling the getParent on button
            mContainerView.removeView((View) v.getParent().getParent());
    }
    
    public void onDeleteWorkClicked(View v) {
        // remove the row by calling the getParent on button
    	LinearLayout geberView=(LinearLayout)v.getParent().getParent();
        geberView.removeView((View) v.getParent());
}

    // Helper for inflating a row
    private View inflateEditRow(int geberID) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.row_geber, null);
            rowView.setTag(111);
            final ImageButton deleteButton = (ImageButton) rowView
                            .findViewById(R.id.buttonDeleteGeber);
            final Spinner chGeber = (Spinner) rowView
                            .findViewById(R.id.spinnerGeber);
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, geber);
            chGeber.setAdapter(adapter);
            final Button butWork = (Button) rowView
                    .findViewById(R.id.btnAddNewWork); 

            if (geberID>0) {
            	chGeber.setSelection(geberID);
                chGeber.setEnabled(false);
                chGeber.setClickable(false);
            } else {
                mExclusiveEmptyView = rowView;
                deleteButton.setVisibility(View.INVISIBLE);
                butWork.setVisibility(View.INVISIBLE);
            }
            // A TextWatcher to control the visibility of the "Add new" button and
            // handle the exclusive empty view.
            chGeber.setOnItemSelectedListener(new OnItemSelectedListener() {
            	@Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            		//((TextView) parentView.getChildAt(0)).setTextColor(Color.parseColor("#26BFED"));
            		//((TextView) parentView.getChildAt(0)).setTypeface(null, Typeface.BOLD);
          
            		if (position==0) {
                        mAddButton.setVisibility(View.GONE);
                        lineAboveAdd.setVisibility(View.GONE);
                        deleteButton.setVisibility(View.INVISIBLE);
                        butWork.setVisibility(View.INVISIBLE);

                        if (mExclusiveEmptyView != null
                                        && mExclusiveEmptyView != rowView) {
                                mContainerView.removeView(mExclusiveEmptyView);
                        }
                        mExclusiveEmptyView = rowView;
            		} else {
            			chGeber.setEnabled(false);
        				chGeber.setClickable(false);
        				if (mExclusiveEmptyView == rowView) {
        					mExclusiveEmptyView = null;
        				}
        				butWork.setVisibility(View.VISIBLE);
        				mAddButton.setVisibility(View.VISIBLE);
        				lineAboveAdd.setVisibility(View.VISIBLE);
        				deleteButton.setVisibility(View.VISIBLE);
            		}
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }
            });
            // Inflate at the end of all rows but before the "Add new" button
            mContainerView.addView(rowView, mContainerView.getChildCount() - 2);
            scroll.fullScroll(View.FOCUS_DOWN);
            return rowView;
    }
    
    private void inflateEditWork(int work, View v, double stunden, int geberID, int wieseID){
    	LinearLayout parent=(LinearLayout)v;
    	
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowViewWork = inflater.inflate(R.layout.row_work, null);
        rowViewWork.setTag(222);
        final ImageButton deleteButton = (ImageButton) rowViewWork
                        .findViewById(R.id.buttonDeleteWork);
        final Spinner chWork = (Spinner) rowViewWork
                        .findViewById(R.id.spinnerWork);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arbeiten);
        chWork.setAdapter(adapter);
        
        final Spinner chWiesen = (Spinner) rowViewWork
                .findViewById(R.id.spinnerWiese);
        ArrayAdapter<Wiese> adapterWiesen = new ArrayAdapter<Wiese>(parent.getContext(),android.R.layout.simple_spinner_item, wiesen.get(geberID).getWiesen());
		chWiesen.setAdapter(adapterWiesen);
        
        final EditText edWork = (EditText) rowViewWork
                .findViewById(R.id.timePickerEdit);
        edWork.setInputType(InputType.TYPE_NULL);
        edWork.setClickable(true);
        final int volleStunden=((Double)stunden).intValue();
        Double tmp=(stunden-((double)volleStunden))*60;
        final int halbeStunden=tmp.intValue();
        edWork.setText( ""+volleStunden + ":" + halbeStunden);
        edWork.setFocusable(false);
        edWork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener(){
            		@Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edWork.setText( ""+selectedHour + ":" + selectedMinute);
                    }
            	}, volleStunden, 
                   CustomTimePickerDialog.getRoundedMinute(halbeStunden), 
                   true
            );
            timePickerDialog.setTitle("Stunden");
            timePickerDialog.show();
            }
        });
        edWork.setOnFocusChangeListener(new OnFocusChangeListener() {
        	@Override
        	public void onFocusChange(View v, boolean hasFocus) {
        	    if(hasFocus){
        	    	CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener(){
                		@Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            edWork.setText( ""+selectedHour + ":" + selectedMinute);
                        }
                	}, volleStunden, 
                       CustomTimePickerDialog.getRoundedMinute(halbeStunden), 
                       true
                );
                timePickerDialog.setTitle("Stunden");
                timePickerDialog.show();
        	    }
        	   }
        	});
        
        chWork.setSelection(work);
        chWiesen.setSelection(wieseID);
        // A TextWatcher to control the visibility of the "Add new" button and
        // handle the exclusive empty view.
        chWork.setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        		LinearLayout parent=(LinearLayout)selectedItemView.getParent().getParent().getParent();
        		ViewGroup row = (ViewGroup) parent;
        		Button addBut=(Button)row.getChildAt(parent.getChildCount()-1);
        		if (position==0) {
                    addBut.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.INVISIBLE);

                    /*if (mExclusiveEmptyViewWork != null
                                    && mExclusiveEmptyViewWork != rowViewWork) {
                            mContainerView.removeView(mExclusiveEmptyViewWork);##
                    }
                    mExclusiveEmptyViewWork = rowViewWork;*/
            } else {
                    /*if (mExclusiveEmptyViewWork == rowViewWork) {
                    	mExclusiveEmptyViewWork = null;
                    }*/

                    addBut.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // Inflate at the end of all rows but before the "Add new" button
        parent.addView(rowViewWork, parent.getChildCount() - 1);
    }
    
    private void inflateEditWork(View v) {
    	LinearLayout parent=(LinearLayout)v.getParent();
    	
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowViewWork = inflater.inflate(R.layout.row_work, null);
        rowViewWork.setTag(222);
        final ImageButton deleteButton = (ImageButton) rowViewWork
                        .findViewById(R.id.buttonDeleteWork);
        final Spinner chWork = (Spinner) rowViewWork
                        .findViewById(R.id.spinnerWork);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arbeiten);
        chWork.setAdapter(adapter);
        final Spinner chWiesen = (Spinner) rowViewWork
                .findViewById(R.id.spinnerWiese);
		ViewGroup row = (ViewGroup) parent;
		LinearLayout parentLayout2=(LinearLayout)row.getChildAt(0);
		Spinner parentGeber=(Spinner)parentLayout2.getChildAt(0);; 
        ArrayAdapter<Wiese> adapterWiesen = new ArrayAdapter<Wiese>(parent.getContext(),android.R.layout.simple_spinner_item, wiesen.get(parentGeber.getSelectedItemPosition()).getWiesen());
		chWiesen.setAdapter(adapterWiesen);
        
        final EditText edWork = (EditText) rowViewWork
                .findViewById(R.id.timePickerEdit);
        edWork.setInputType(InputType.TYPE_NULL);
        edWork.setClickable(true);
        edWork.setFocusable(false);
        edWork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	CustomTimePickerDialog timePickerDialog = new CustomTimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener(){
            		@Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edWork.setText( ""+selectedHour + ":" + selectedMinute);
                    }
            	}, 0, 
                   CustomTimePickerDialog.getRoundedMinute(0), 
                   true
            );
            timePickerDialog.setTitle("Stunden");
            timePickerDialog.show();
            }
        });
        
        deleteButton.setVisibility(View.INVISIBLE);
        
        // A TextWatcher to control the visibility of the "Add new" button and
        // handle the exclusive empty view.
        chWork.setOnItemSelectedListener(new OnItemSelectedListener() {
        	@Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
        		LinearLayout parent=(LinearLayout)selectedItemView.getParent().getParent().getParent();
        		ViewGroup row = (ViewGroup) parent;
        		Button addBut=(Button)row.getChildAt(parent.getChildCount()-1);
        		if (position==0) {
                    addBut.setVisibility(View.GONE);
                    deleteButton.setVisibility(View.INVISIBLE);
            } else {
                    addBut.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // Inflate at the end of all rows but before the "Add new" button
        parent.addView(rowViewWork, parent.getChildCount() - 1);
    }
    
    public void exitClicked(View v) {
    	finish();
    }
    
    public void saveClicked(View v) {
    	if(day==null){
    		day=new Day(date);
    	}else{
    		day.deleteWorks();
    	}
    	String[] geber_array = getResources().getStringArray(R.array.geber);
    	for(int i=0;i<mContainerView.getChildCount();i++){
    		View lv =mContainerView.getChildAt(i);
    		if((lv.getTag()!=null)&&((Integer)lv.getTag()==111)){
    			LinearLayout ll=(LinearLayout) ((LinearLayout )lv).getChildAt(0);
    			Spinner ls=(Spinner)ll.getChildAt(0);
    			int geberID=ls.getSelectedItemPosition();
    			if(geberID!=0){
    				String geber=geber_array[geberID];
    				LinearLayout row_geber=(LinearLayout)lv;
    				for(int j=1;j<(row_geber.getChildCount()-1);j++){
    					LinearLayout row_work=(LinearLayout)row_geber.getChildAt(j);
    					Spinner lsi=(Spinner)row_work.getChildAt(0);
        				int arbeitID=lsi.getSelectedItemPosition();
        				if(arbeitID!=0){
        					EditText ed=(EditText)row_work.getChildAt(2);
        					String st=ed.getText().toString();
        					if((st!=null)&&(st.length()>1)){
        						double stunden=Double.parseDouble(st.substring(0, st.indexOf(":"))); 
            					if(Double.parseDouble(st.substring(st.indexOf(":")+1))>0){
            						stunden+=0.5;
            					}
            					if(stunden>0){
            						Spinner wiesesp=(Spinner)row_work.getChildAt(1);
            						int wieseID=(int)wiesesp.getSelectedItemId();
            						day.add(geber, geberID, wieseID, arbeitID, stunden);
            					}
        					}
        				}
        			}
    			}
    		}
    	}
    	Intent intent = new Intent();
    	intent.putExtra("Day",day);
    	setResult(Activity.RESULT_OK,intent);
    	finish();
    }
    
}
