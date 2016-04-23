package bz.lenz.lwv.data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Wiesen implements Parcelable{
	private int geber;
	public int getGeber() {
		return geber;
	}

	//private ArrayList<String> wiesen;
	private ArrayList<Wiese> wiesen;
	
	public ArrayList<Wiese> getWiesen() {
		return wiesen;
	}
	
	public Wiese getWiese(int i){
		return wiesen.get(i);
	}

	public void setWiesen(ArrayList<Wiese> wiesen) {
		this.wiesen = wiesen;
	}

	public Wiesen(int geber){
		this.geber=geber;
		wiesen=new ArrayList<Wiese>();
	}
	
	public Wiesen(int geber, int numElements){
		this.geber=geber;
		wiesen=new ArrayList<Wiese>(numElements);
	}
	
	public Wiesen(ArrayList<Wiese> wiesen,int geber){
		this.wiesen=wiesen;
		this.geber=geber;
	}
	private Wiesen(Parcel in) {
		wiesen=new ArrayList<Wiese>();
		in.readTypedList(wiesen, Wiese.CREATOR);
        geber=in.readInt();
    }
	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(wiesen);
		dest.writeInt(geber);
	}
	
	public static final Parcelable.Creator<Wiesen> CREATOR = 
            new Parcelable.Creator<Wiesen>() {
        public Wiesen createFromParcel(Parcel in) {
            return new Wiesen(in);
        }

        public Wiesen[] newArray(int size) {
            return new Wiesen[size];
        }
    };
    
    public void addWiese(Wiese wiese){
    	wiesen.add(wiese);
    }
    
    public void addWiese(int idInt, Wiese wiese){
    	while(idInt>=wiesen.size()){
    		wiesen.add(null); 
    	}
    	wiesen.set(idInt,wiese);
    }
}
