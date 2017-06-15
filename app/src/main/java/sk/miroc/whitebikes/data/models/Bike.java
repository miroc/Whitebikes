package sk.miroc.whitebikes.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Bike implements Parcelable {
    @NonNull
    private final String bikeNumber;
    @NonNull
    private final String note;

    private  boolean hasNote;


    public Bike(@NonNull String bikeNumber, boolean hasNote, @NonNull String note) {
        this.bikeNumber = bikeNumber;
        this.note = note;
        this.hasNote = hasNote;
    }

    @NonNull
    public String getBikeNumber() {
        return bikeNumber;
    }

    @NonNull
    public String getNote() {
        return note;
    }

    public boolean hasNote() {
        return hasNote;
    }

    protected Bike(Parcel in) {
        bikeNumber = in.readString();
        note = in.readString();
        hasNote = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bikeNumber);
        dest.writeString(note);
        dest.writeByte((byte) (hasNote ? 0x01 : 0x00));
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Bike> CREATOR = new Parcelable.Creator<Bike>() {
        @Override
        public Bike createFromParcel(Parcel in) {
            return new Bike(in);
        }

        @Override
        public Bike[] newArray(int size) {
            return new Bike[size];
        }
    };
}
