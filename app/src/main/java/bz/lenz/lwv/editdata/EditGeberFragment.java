package bz.lenz.lwv.editdata;

import bz.lenz.lwv.MainActivity;
import bz.lenz.lwv.R;
import bz.lenz.lwv.R.layout;
import bz.lenz.lwv.data.Datastore;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link EditGeberFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link EditGeberFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class EditGeberFragment extends Fragment {
	private Datastore ds;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ds=((EditData)getActivity()).getDatastore();
	}  

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_edit_geber, container, false);
			
	    ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.listView);
	    MyExpandableListAdapter adapter = new MyExpandableListAdapter(inflater,ds.getGeber(),ds.getWiesen());
	    listView.setAdapter(adapter);
	    
	    //edit geber, new geber(hinten), edit wiese, new wiese(hinten)
	    //bu die wiesen a di hektar
	    //bun löschen isch aufzupassen mit die ids
	    
	    return rootView;
	}
	
	@Override
	public void onDestroy (){
		super.onDestroy();
	}
}
