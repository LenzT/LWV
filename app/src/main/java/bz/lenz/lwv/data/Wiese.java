package bz.lenz.lwv.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Wiese implements Parcelable{
	String sorte;
	double hektar;
	int id;
	String name;
	
	public Wiese(String sorte, double hektar, int id, String name){
		this.sorte=sorte;
		this.hektar=hektar;
		this.id=id;
		this.name=name;
	}
	private Wiese(Parcel in) {
        sorte = in.readString();
        hektar=in.readDouble();
        id=in.readInt();
        name=in.readString();
    }
	
	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(sorte);
		dest.writeDouble(hektar);
		dest.writeInt(id);
		dest.writeString(name);
	}
	
	public static final Parcelable.Creator<Wiese> CREATOR = 
            new Parcelable.Creator<Wiese>() {
        public Wiese createFromParcel(Parcel in) {
            return new Wiese(in);
        }

        public Wiese[] newArray(int size) {
            return new Wiese[size];
        }
    };
    
    @Override
    public String toString(){
    	return name;
    }
	public String getName() {
		return name;
	}
	public double getHektar() {
		return hektar;
	}
	public String getSorte() {
		return sorte;
	}
}
