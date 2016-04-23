package bz.lenz.lwv.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Work implements Parcelable{
	double Hours;
	public double getHours() {
		return Hours;
	}

	public void setHours(double hours) {
		Hours = hours;
	}
	int geberID;
	public int getGeberID() {
		return geberID;
	}

	public void setGeberID(int geberID) {
		this.geberID = geberID;
	}
	String geber;
	public String getGeber() {
		return geber;
	}

	public void setGeber(String geber) {
		this.geber = geber;
	}

	int Arbeit;
	public int getArbeit() {
		return Arbeit;
	}

	public void setArbeit(int arbeit) {
		Arbeit = arbeit;
	}
	
	int wieseID;
	public int getWieseID() {
		return wieseID;
	}

	public void setWieseID(int wieseID) {
		this.wieseID = wieseID;
	}

	public Work(double Hours, String geber,int geberID, int wieseID, int Arbeit){
		this.Hours=Hours;
		this.geber=geber;
		this.geberID=geberID;
		this.wieseID=wieseID;
		this.Arbeit=Arbeit;
	}
	
	private Work(Parcel in) {
		Hours=in.readDouble();
		geber = in.readString();
		geberID = in.readInt();
		wieseID = in.readInt();
		Arbeit = in.readInt();
    }
	
	public String getCompleteString(){
		Double sum=Hours;
		int sumInt=sum.intValue();
		String sumS=""+sumInt;
		if(sum>(double)sumInt){
			sumS+=",5";
		}
		//return Arbeit+"."+geber+"."+sumS;
		return Arbeit+"."+geberID+"."+wieseID+"."+sumS;
	}
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(Hours);
		dest.writeString(geber);
		dest.writeInt(geberID);
		dest.writeInt(wieseID);
		dest.writeInt(Arbeit);
	}
	
	public static final Parcelable.Creator<Work> CREATOR = 
            new Parcelable.Creator<Work>() {
        public Work createFromParcel(Parcel in) {
            return new Work(in);
        }

        public Work[] newArray(int size) {
            return new Work[size];
        }
    };
}
