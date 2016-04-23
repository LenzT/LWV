package bz.lenz.lwv.editdata;

import bz.lenz.lwv.MainActivity;
import bz.lenz.lwv.R;
import bz.lenz.lwv.R.layout;
import bz.lenz.lwv.data.Datastore;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link EditSpritzmFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link EditSpritzmFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class EditSpritzmFragment extends Fragment {
	private Datastore ds;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ds=((EditData)getActivity()).getDatastore();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_edit_spritzm, container, false);
		
	}
	
	@Override
	public void onDestroy (){
		super.onDestroy();
	}
}
