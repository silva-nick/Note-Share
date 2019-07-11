package com.test.nick.noteshare.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey
    public int nid;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "extra")
    public String extra;

    public Note(int nid, String type, String title, String body, String extra){
        this.nid = nid;
        this.type = type;
        this.title = title;
        this.body = body;
        this.extra = extra;
    }
}

