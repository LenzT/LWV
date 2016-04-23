package bz.lenz.lwv;

import java.util.Locale;

import bz.lenz.lwv.data.Datastore;
import bz.lenz.lwv.editdata.EditData;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxDatastoreStatus;
import com.dropbox.sync.android.DbxException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity implements ActionBar.TabListener,DbxDatastore.SyncStatusListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	private static final int REQUEST_LINK_TO_DBX = 0;
	//private DbxAccount acc;
	private Datastore ds;
	private DbxDatastore mStore;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private DbxAccountManager mAccountManager;
	private DbxDatastoreManager mDatastoreManager;
	private static final String appKey = "wrte2awnsn87kt3";
    private static final String appSecret = "l5emaoy20ex28wb";
    
    private static final int EDITDATA = 102;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//load credentials
		mAccountManager = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
		// Set up the datastore manager
	    if (mAccountManager.hasLinkedAccount()) {
	        try {
	            // Use Dropbox datastores
	            mDatastoreManager = DbxDatastoreManager.forAccount(mAccountManager.getLinkedAccount());
	            createView();
	        	try {
					ds=(Datastore)getApplication();
					ds.setDatastoreManger(mDatastoreManager);
					//mStore=mDatastoreManager.openDefaultDatastore();
					//ds.initDatastore(mStore);
				} catch (Exception e) {
					connectionFailedHandler(e);
				}
	        } catch (DbxException.Unauthorized e) {
	        	e.printStackTrace();
	            System.out.println("Account was unlinked remotely");
	        }
	    }else{
	    	mAccountManager.startLink(this, REQUEST_LINK_TO_DBX); //neuen link aufbauen
	    }
	}

	private void createView(){
		setContentView(R.layout.activity_main);
		
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_LINK_TO_DBX) {
    		if (resultCode == Activity.RESULT_OK) {
            	createView();
            	try {
            		mDatastoreManager = DbxDatastoreManager.forAccount(mAccountManager.getLinkedAccount());
    				ds=(Datastore)getApplication();
    				ds.setDatastoreManger(mDatastoreManager);
    				mStore=mDatastoreManager.openDefaultDatastore();
        			mStore.addSyncStatusListener(this);
    				ds.initDatastore(mStore);
    			} catch (Exception e) {
    				connectionFailedHandler(e);
    			}
            } else {
            	connectionFailedHandler(new Exception("No Connection to Dropbox"));
            }
        }
    	if(requestCode==EDITDATA){
    		if(resultCode==Activity.RESULT_OK){
    			//TODO ds wos von den returned wert oblegen (ober wia dorfrog no es punchclockfragment von die aenderungen??
    		}
    	}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	            //TODO nimer notwendig, loeschen oder wos onders drauflegen af den knopf
	            return true;
	        case R.id.action_data:
	        	Intent intent = new Intent(this, EditData.class);
	        	//intent.putExtra("Datastore",ds);
	        	startActivityForResult(intent,EDITDATA);
	            return true;
	        case R.id.action_stat:
	            // TODO activity mit die statistiken starten, werts nitmol an return brauchn
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		
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
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			/*Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);*/
			
			switch (position) {
				case 0:
					return new PunchClockFragment();
				case 1:
					return new ToDoFragment();
				case 2:
					return new SpritzungenFragment();
			}
			return null;
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
	
	private void connectionFailedHandler(Exception e) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Verbindungsfehler");
		alertDialogBuilder
			.setMessage(e.getMessage())
			.setCancelable(false)
			.setPositiveButton("Beenden",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					MainActivity.this.finish();
				}
			});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}
	
	public Datastore getDatastore(){
		return ds;
	}
	
	@Override
    public void onResume() {
        super.onResume();
        System.out.println("############onResume");
        if ((mAccountManager.hasLinkedAccount())&&(mStore==null)) {
        	try {
    			mStore=mDatastoreManager.openDefaultDatastore();
    			mStore.addSyncStatusListener(this);
    			mStore.sync();
    			Log.d("TEST", ""+mStore.getSyncStatus());
    			ds.initDatastore(mStore);
    		} catch (Exception e) {
    			e.printStackTrace();
    			connectionFailedHandler(e);
    		}
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("############onPause");
        try {
        	if(mStore!=null){
        		mStore.removeSyncStatusListener(this);
                mStore.close();
                mStore=null;
        	}
		} catch (Exception e) {
			e.printStackTrace();
			connectionFailedHandler(e);
		}
    }
	
	@Override
	public void onDatastoreStatusChange(DbxDatastore datastore) {
		System.out.println("####################changelistener");
		if (mStore.getSyncStatus().hasIncoming) {
            try {
            	System.out.println("####################epas geat");
               if(mStore!=null){
            	   mStore.sync();
            	   ds.initDatastore(mStore);
            	   ds.launchDataChangeEvent();
               }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
		try {
			mStore.sync();
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	 @Override
	 public void onStop() {
		 super.onStop();
	     if(mStore!=null){
	    	 mStore.close();
	    	 mStore=null;
	     }
	}
    
}
