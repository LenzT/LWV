package bz.lenz.lwv.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Application;
import android.util.Log;
import bz.lenz.lwv.UpdateListener;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxDatastore;
import com.dropbox.sync.android.DbxDatastoreManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFields;
import com.dropbox.sync.android.DbxRecord;
import com.dropbox.sync.android.DbxTable;
import com.dropbox.sync.android.DbxTable.QueryResult;

public class Datastore extends Application{
	private DbxDatastore mStore;
	private DbxDatastoreManager mDatastoreManager;
	//private ArrayList<ArrayList<Day>> dataset;
	private ArrayList<ArrayList<ArrayList<Day>>> dataset;
	private ArrayList<String> arbeiten;
	private ArrayList<Wiesen> wiesen;
	private ArrayList<String> geber;
	private ArrayList<UpdateListener> listeners = new ArrayList<UpdateListener> ();
	
	public Datastore(){
		//gebraucht wegen Application
	}
	
	public void setDatastoreManger(DbxDatastoreManager mDatastoreManager){
		this.mDatastoreManager=mDatastoreManager;
	}
	
	public void initDatastore(DbxDatastore mStore) throws Exception{
		this.mStore=mStore;
		init();
	}
	
	public void registerUpdateEventListener (UpdateListener listener) 
    {
        // Store the listener object
        this.listeners.add(listener);
    }
	
	public void launchDataChangeEvent(){
		notifyListenersAboutUpdate();
	}
	
	private void notifyListenersAboutUpdate(){
		for(int i=0;i<listeners.size();i++)
		{
		    listeners.get(i).onUpdate();
		}
	}
	
	private void init() throws Exception{
		try {
			//braucht kuane eigene connection weil olm lei in main activity gecallt bis jetz
			DbxTable azTab = mStore.getTable("arbeitszeiten");
			DbxTable arbeitenTab = mStore.getTable("arbeiten");
			DbxTable wiesenTab = mStore.getTable("wiesen");
			DbxTable geberTab = mStore.getTable("arbeitgeber");
			Log.d("TEST", ""+azTab.query().asList().size());
			Log.d("TEST", ""+arbeitenTab.query().asList().size());
			Log.d("TEST", ""+wiesenTab.query().asList().size());
			Log.d("TEST", ""+geberTab.query().asList().size());
			
			List<DbxRecord> results=arbeitenTab.query().asList();
			DbxRecord zeile;
			arbeiten=new ArrayList<String>(results.size());
			for(int i=0;i<results.size();i++){
				zeile = results.get(i);
				while((int)zeile.getLong("id")>=arbeiten.size()){
		    		arbeiten.add(null); 
		    	}
				arbeiten.set((int)zeile.getLong("id"), zeile.getString("name"));
			}
			
			results=geberTab.query().asList();
			geber=new ArrayList<String>(results.size());
			//Log.d("TEST", "#"+results.size());
			for(int i=0;i<results.size();i++){
				zeile = results.get(i);
				//Log.d("TEST", "#"+zeile.getString("name"));
				while((int)zeile.getLong("id")>=geber.size()){
					geber.add(null); 
		    	}
				geber.set((int)zeile.getLong("id"), zeile.getString("name"));
			}
			
			results=wiesenTab.query().asList();
			wiesen=new ArrayList<Wiesen>();
			for(int i=0;i<geber.size();i++){
				wiesen.add(new Wiesen(i));
			}
			for(int i=0;i<results.size();i++){
				zeile = results.get(i);
				wiesen.get((int)zeile.getLong("geber")).addWiese((int)zeile.getLong("idInt"),new Wiese(zeile.getString("sorte"),zeile.getDouble("hektar"),(int)zeile.getLong("idInt"),zeile.getString("name")));
				//wiesen.get((int)zeile.getLong("geber")).addWiese((int)zeile.getLong("idInt"),zeile.getString("name"));
			}
			
			//dataset= new ArrayList<ArrayList<Day>>();
			dataset= new ArrayList<ArrayList<ArrayList<Day>>>();
			Calendar cal = Calendar.getInstance();
			System.out.println("##########"+cal.get(Calendar.YEAR));
			for(int j=0;j<cal.get(Calendar.YEAR)-2010+5;j++){ //bis zrug zun johr 2010 und 5 johr in die zukunft
				dataset.add(new ArrayList<ArrayList<Day>>());
				for(int i=0;i<12;i++){
					dataset.get(j).add(new ArrayList<Day>()); //in jedem jahr 12 monate
				}
			}
			
			results=azTab.query().asList();
			for(int j=0;j<results.size();j++){
				zeile = results.get(j);
				String datum;
				ArrayList<Work> work=new ArrayList<Work>();
				double sumHours;
				datum=zeile.getString("datum");
				String[] date= datum.split("\\.");
				int year=Integer.valueOf(date[2])-10; //jahr 0 isch 2010
				int month=Integer.valueOf(date[1])-1;
				String sumH=zeile.getString("summe");
				if(sumH.indexOf(",")==-1){
					sumHours=Double.valueOf(sumH);
				}else{
					sumHours=Double.valueOf(sumH.substring(0, sumH.indexOf(",")))+0.5;
				}
				int i=0;
				while(zeile.hasField(Integer.toString(i))){
					String[] wo= zeile.getString(Integer.toString(i)).split("\\.");
					double h=0;
					if(wo[3].indexOf(",")==-1){
						h=Double.valueOf(wo[3]); 
					}else{
						h=Double.valueOf(wo[3].substring(0, wo[3].indexOf(",")))+0.5;
					}
					Work w=new Work(h,geber.get(Integer.valueOf(wo[1])),Integer.valueOf(wo[1]),Integer.valueOf(wo[2]),Integer.valueOf(wo[0]));
					work.add(w);
					i++;
				}
				appendNewDay(year, month, datum, work, sumHours);
			}
			//store.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Connection to Datastore failed");
		}
		//notifyListenersAboutUpdate();
	}
	
	private void appendNewDay(int year, int month,String datum, ArrayList<Work> work, double sumHours){
		ArrayList<ArrayList<Day>> selectedYear=dataset.get(year); //start bei 2010
		ArrayList<Day> cur=selectedYear.get(month);
		cur.add(new Day(datum,work,sumHours));
	}
	
	public ArrayList<Day> getMonthOfYear(int month,int year){
		ArrayList<ArrayList<Day>> selectedYear=dataset.get(year-2010);
		return selectedYear.get(month);
	}

	public Day getDay(int day, int month, int year) { 
		ArrayList<ArrayList<Day>> selectedYear=dataset.get(year-2010);
		ArrayList<Day> currmon=selectedYear.get(month);
		for(int i=0;i<currmon.size();i++){
			if(currmon.get(i).datum.equals(""+day+"."+(month+1)+"."+(year-2000))){
				return currmon.get(i);
			}
		}
		return null;
		
		/*ArrayList<Day> currmon=dataset.get(month);
		for(int i=0;i<currmon.size();i++){
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			if(currmon.get(i).datum.equals(""+day+"."+(month+1)+"."+(year-2000))){
				return currmon.get(i);
			}
		}
		return null;*/
	}
	
	/** tag speichern, neu wen nicht vorhanden, sonst ersetzen **/
	public void setDay(int year, int month, Day day) {
		Double sum=day.getSumHours();
		int sumInt=sum.intValue();
		String sumS=""+sumInt;
		if(sum>(double)sumInt){
			sumS+=",5";
		}
		DbxDatastore store;
		try {
			store=mDatastoreManager.openDefaultDatastore();
			DbxTable azTab = store.getTable("arbeitszeiten");
			DbxFields queryParams = new DbxFields().set("datum", day.getDatum());
			QueryResult results = azTab.query(queryParams);
			
			ArrayList<ArrayList<Day>> selectedYear=dataset.get(year-2010);
			ArrayList<Day> currmon=selectedYear.get(month);
			//ArrayList<Day> currmon=dataset.get(month);
			int treffer=0;
			for(int i=0;i<currmon.size();i++){
				if(currmon.get(i).datum.equals(""+day.datum)){ //bereits vorhanden
					treffer=1;
					//local
					currmon.set(i, day);
					//dropbox datastore
					DbxRecord dayRecord = results.iterator().next();
					for(int j=0;j<day.getWork().size();j++){
    					dayRecord.deleteField(Integer.toString(j)); 
    					dayRecord.set(Integer.toString(j),day.getWork().get(j).getCompleteString());
    				}
    				dayRecord.set("summe", sumS);
					break;
				}
			}
			if(treffer==0){ //noch nicht vorhanden
				//local
				currmon.add(day);
				//dropbox datastore
				DbxRecord dayRecord=azTab.insert();
				dayRecord.set("datum", day.getDatum());
				for(int i=0;i<day.getWork().size();i++){
					dayRecord.set(Integer.toString(i),day.getWork().get(i).getCompleteString());
				}
				dayRecord.set("summe", sumS);
			}
			store.sync();
			store.close();
		} catch (DbxException e) {
			e.printStackTrace();
		}
		//notifyListenersAboutUpdate();
	}
	
	/** loeschen, falls existent **/
	public void deleteDay(int year, int month, Day day) {
		ArrayList<ArrayList<Day>> selectedYear=dataset.get(year-2010);
		ArrayList<Day> currmon=selectedYear.get(month);
		//ArrayList<Day> currmon=dataset.get(month);
		for(int i=0;i<currmon.size();i++){
			if(currmon.get(i).datum.equals(""+day.datum)){
				//local
				currmon.remove(i);
				//dropbox datastore
				DbxDatastore store;
				try {
					store=mDatastoreManager.openDefaultDatastore();
					DbxTable azTab = store.getTable("arbeitszeiten");
					DbxFields queryParams = new DbxFields().set("datum", day.getDatum());
					QueryResult results = azTab.query(queryParams);
    				DbxRecord dayRecord = results.iterator().next();
					dayRecord.deleteRecord();
					store.sync();
					store.close();
				} catch (DbxException e) {
					e.printStackTrace();
				}
				break;
			}
		}
		//notifyListenersAboutUpdate();
	}

	public ArrayList<String> getArbeiten() {
		return arbeiten;
	}

	public ArrayList<Wiesen> getWiesen() {
		return wiesen;
	}

	public ArrayList<String> getGeber() {
		return geber;
	}
	
}