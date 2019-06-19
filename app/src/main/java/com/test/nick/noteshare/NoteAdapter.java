package com.test.nick.noteshare;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";
    private String[] bigData;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public ViewHolder(View view){
            super(view);
            cardView = (CardView)view;
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View V){
                    Log.d(TAG, "ViewHolder: Clicked view");
                }
            });
        }
    }

    public NoteAdapter(String[] dataSet){
        bigData = dataSet;

    }

}
