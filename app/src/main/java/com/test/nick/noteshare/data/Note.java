package com.test.nick.noteshare.data;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public int nid;

    @ColumnInfo(name = "type")
    public int type;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "extra")
    public String extra;

    public Note(int type, String title, String body, String extra){
        this.type = type;
        this.title = title;
        this.body = body;
        this.extra = extra;
    }

    protected Note(Parcel in) {
        nid = in.readInt();
        type = in.readInt();
        title = in.readString();
        body = in.readString();
        extra = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nid);
        dest.writeInt(type);
        dest.writeString(title);
        dest.writeString(body);
        dest.writeString(extra);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public String toString(){
        return "id: " + nid + " type: "+ type + " title: " + title;
    }
}

