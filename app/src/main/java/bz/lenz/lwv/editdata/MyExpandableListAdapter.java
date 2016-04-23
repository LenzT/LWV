package bz.lenz.lwv.editdata;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import bz.lenz.lwv.R;
import bz.lenz.lwv.data.Wiese;
import bz.lenz.lwv.data.Wiesen;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

  private LayoutInflater inflater;
  private ArrayList<String> geber;
  private ArrayList<Wiesen> wiesen;
  
  	public MyExpandableListAdapter(LayoutInflater inf, ArrayList<String> geber, ArrayList<Wiesen> wiesen) {
  		inflater=inf;
  		this.geber=geber;
  		this.wiesen=wiesen;
	 }

  @Override
  public Object getChild(int groupPosition, int childPosition) {
    return wiesen.get(groupPosition+1).getWiese(childPosition);
  }

  @Override
  public long getChildId(int groupPosition, int childPosition) {
    return 0;
  }

  @Override
  public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
    final Wiese curWiese=(Wiese)getChild(groupPosition,childPosition);
	final String name = curWiese.getName();
	final double hektar =curWiese.getHektar();
	final String sorte =curWiese.getSorte();
	
    EditText text = null;
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.listrow_details, null);
    }
    text = (EditText) convertView.findViewById(R.id.list_item_text_view107);
    if(name.equals("")){
    	 text.setText("<Platzhalter, keine Wiese>");
    }else{
    	 text.setText(name);
    }
    text.setInputType( InputType.TYPE_NULL );
    text.setBackgroundColor(Color.TRANSPARENT);
    //text.setClickable(false);
    //text.setFocusable(false);
    
    text = (EditText) convertView.findViewById(R.id.list_item_text_view10888);
    text.setText(sorte);
    text.setInputType( InputType.TYPE_NULL );
    text.setBackgroundColor(Color.TRANSPARENT);
    
    text = (EditText) convertView.findViewById(R.id.list_item_text_view10999);
    text.setText(""+hektar);
    text.setInputType( InputType.TYPE_NULL );
    text.setBackgroundColor(Color.TRANSPARENT);
    
    ImageButton edit=(ImageButton) convertView.findViewById(R.id.buttonDeleteGeber108);
    edit.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) { 
        	LinearLayout parent=(LinearLayout)v.getParent();
    		ViewGroup row = (ViewGroup) parent;
    		EditText name=(EditText)row.getChildAt(0);
    		EditText sorte=(EditText)row.getChildAt(1);
    		EditText hektar=(EditText)row.getChildAt(2);
    		Log.d("TEST", "click");
    		if(name.isClickable()==false){ 
    			Log.d("TEST", "false");
    			name.setClickable(true);
    			name.setFocusable(true);
    			name.setInputType(InputType.TYPE_CLASS_TEXT);
    			name.setCursorVisible(true);
    			name.setBackgroundColor(Color.WHITE);
    			name.setLongClickable(true);
    			name.setFocusableInTouchMode(true); //des lost die tastatur zua ober hautse glei wieder weck
    			name.requestFocus();
    			name.setSelection(name.getText().length());
    			InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT);
    			
    			/*sorte.setClickable(true);
    			sorte.setFocusable(true);
    			sorte.setInputType(InputType.TYPE_CLASS_TEXT);
    			sorte.setCursorVisible(true);
    			sorte.setBackgroundColor(Color.WHITE);
    			sorte.setLongClickable(true);
    			sorte.setFocusableInTouchMode(true);
    			//text.requestFocus();
    			//text.setSelection(text.getText().length());
    			
    			hektar.setClickable(true);
    			hektar.setFocusable(true);
    			hektar.setInputType(InputType.TYPE_CLASS_NUMBER);
    			hektar.setCursorVisible(true);
    			hektar.setBackgroundColor(Color.WHITE);
    			hektar.setLongClickable(true);
    			hektar.setFocusableInTouchMode(true);*/
    			//text.requestFocus();
    			//text.setSelection(text.getText().length());
    			
    			((ImageButton) v).setImageResource(R.drawable.ic_action_save);
    		}else{
    			Log.d("TEST", "true");
    			name.setClickable(false);
    			name.setFocusable(false);
    			name.setInputType( InputType.TYPE_NULL );
    			name.setCursorVisible(false);
    			name.setBackgroundColor(Color.TRANSPARENT);
    			name.setLongClickable(false);
    			name.setFocusableInTouchMode(false);
    			InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(name, InputMethodManager.HIDE_IMPLICIT_ONLY);
    			
    			/*sorte.setClickable(false);
    			sorte.setFocusable(false);
    			sorte.setInputType( InputType.TYPE_NULL );
    			sorte.setCursorVisible(false);
    			sorte.setBackgroundColor(Color.TRANSPARENT);
    			sorte.setLongClickable(false);
    			sorte.setFocusableInTouchMode(false);
    			
    			hektar.setClickable(false);
    			hektar.setFocusable(false);
    			hektar.setInputType( InputType.TYPE_NULL );
    			hektar.setCursorVisible(false);
    			hektar.setBackgroundColor(Color.TRANSPARENT);
    			hektar.setLongClickable(false);
    			hektar.setFocusableInTouchMode(false);*/
    			
    			((ImageButton) v).setImageResource(R.drawable.ic_action_edit_black);
    			//jo speichern holt und add nui fahlt no
    			//und den fehler sobold di tastatur kimp beseitigen
    		}
    		
    		
    		/*EditText text=(EditText)row.getChildAt(0);
    		if(text.isClickable()==false){
    			text.setClickable(true);
    			text.setFocusable(true);
    			text.setInputType(InputType.TYPE_CLASS_TEXT);
    			text.setCursorVisible(true);
    			text.setBackgroundColor(Color.WHITE);
    			text.setLongClickable(true);
    			text.setFocusableInTouchMode(true);
    			text.requestFocus();
    			text.setSelection(text.getText().length());
    			((ImageButton) v).setImageResource(R.drawable.ic_action_save);
    		}else{
    			text.setClickable(false);
    			text.setFocusable(false);
    			text.setInputType( InputType.TYPE_NULL );
    			text.setCursorVisible(false);
    			text.setBackgroundColor(Color.TRANSPARENT);
    			text.setLongClickable(false);
    			text.setFocusableInTouchMode(false);
    			((ImageButton) v).setImageResource(R.drawable.ic_action_edit_black);
    			//jo speichern holt und add nui fahlt no
    			//und den fehler sobold di tastatur kimp beseitigen
    		}*/
        }
    });
    edit.setFocusable(false);
    return convertView;
  }

  @Override
  public int getChildrenCount(int groupPosition) {
    return wiesen.get(groupPosition+1).getWiesen().size();
  }

  @Override
  public Wiesen getGroup(int groupPosition) {
    return wiesen.get(groupPosition+1);
  }

  @Override
  public int getGroupCount() {
    return wiesen.size()-1;
  }

  @Override
  public void onGroupCollapsed(int groupPosition) {
    super.onGroupCollapsed(groupPosition);
  }

  @Override
  public void onGroupExpanded(int groupPosition) {
    super.onGroupExpanded(groupPosition);
  }

  @Override
  public long getGroupId(int groupPosition) {
    return 0;
  }

  @Override
  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
	if (convertView == null) {
      convertView = inflater.inflate(R.layout.listrow_group, null);
    }
    final Wiesen group = getGroup(groupPosition);
    EditText text = (EditText) convertView.findViewById(R.id.list_item_text_view104);
    text.setText(geber.get(group.getGeber()));
    text.setInputType( InputType.TYPE_NULL );
    text.setBackgroundColor(Color.TRANSPARENT);
    ImageButton edit=(ImageButton) convertView.findViewById(R.id.buttonDeleteGeber105);
    edit.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) { 
        	LinearLayout parent=(LinearLayout)v.getParent();
    		ViewGroup row = (ViewGroup) parent;
    		EditText text=(EditText)row.getChildAt(0);
    		if(text.isClickable()==false){
    			text.setClickable(true);
    			text.setFocusable(true);
    			text.setInputType(InputType.TYPE_CLASS_TEXT);
    			text.setCursorVisible(true);
    			text.setBackgroundColor(Color.WHITE);
    			text.setLongClickable(true);
    			text.setFocusableInTouchMode(true);
    			text.requestFocus();
    			text.setSelection(text.getText().length());
    			((ImageButton) v).setImageResource(R.drawable.ic_action_save);
    		}else{
    			text.setClickable(false);
    			text.setFocusable(false);
    			text.setInputType( InputType.TYPE_NULL );
    			text.setCursorVisible(false);
    			text.setBackgroundColor(Color.TRANSPARENT);
    			text.setLongClickable(false);
    			text.setFocusableInTouchMode(false);
    			((ImageButton) v).setImageResource(R.drawable.ic_action_edit_black);
    			//speichern von den nuin wert in dor lokalen liste
    			//geber.set(group.getGeber(), text.getText());
    			//jetz fahlt no add geber irgendwo
    			//i kannt des schun in editdata drausen handln mit dor funktion in xml, ober sem werts unubersichtlich hat ober olle daten sem
    		}
        }
    });
    edit.setFocusable(false);
    return convertView;
  }

  @Override
  public boolean hasStableIds() {
    return false;
  }

  @Override
  public boolean isChildSelectable(int groupPosition, int childPosition) {
    return false;
  }
} 
