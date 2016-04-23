package bz.lenz.lwv.data;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Day implements Parcelable{
	String datum;
	ArrayList<Work> work;
	public ArrayList<Work> getWork() {
		return work;
	}
	public void setWork(ArrayList<Work> work) {
		this.work = work;
	}

	double sumHours;
	public double getSumHours() {
		return sumHours;
	}
	public void setSumHours(double sumHours) {
		this.sumHours = sumHours;
	}
	public Day(String datum, ArrayList<Work> work, double sumHours){
		this.datum=datum;
		this.work=work;
		this.sumHours=sumHours;
	}
	private Day(Parcel in) {
        datum = in.readString();
        work=new ArrayList<Work>();
        in.readTypedList(work, Work.CREATOR);
        sumHours=in.readDouble();
    }
	
	public Day(String datum) {
		this.datum=datum;
		work=new ArrayList<Work>();
		sumHours=0;
		
	}
	public int getIntDay(){
		String[] date= datum.split("\\.");
		return Integer.valueOf(date[0]);
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(datum);
		dest.writeTypedList(work);
		dest.writeDouble(sumHours);
	}
	
	public static final Parcelable.Creator<Day> CREATOR = 
            new Parcelable.Creator<Day>() {
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
    
    public String getDatum(){
    	return datum;
    }
	public void deleteWorks() {
		sumHours=0;
		work=new ArrayList<Work>();
	}
	public void add(String geber, int geberID, int wieseID, int arbeit, double stunden) {
		Work w=new Work(stunden, geber, geberID, wieseID, arbeit);
		work.add(w);
		sumHours+=stunden;
	}
}
