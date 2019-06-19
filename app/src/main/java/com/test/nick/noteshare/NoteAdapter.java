package com.test.nick.noteshare;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";
    private List<CardView> bigData;


    public NoteAdapter(List<CardView> dataSet){
        bigData = dataSet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticky_note, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.cardView = bigData.get(i);
    }

    @Override
    public int getItemCount() {
        return bigData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;

        public ViewHolder(CardView view){
            super(view);
            Log.d(TAG, "ViewHolder: " + view.toString());
            cardView = (CardView)view;
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View V){
                    Log.d(TAG, "ViewHolder: Clicked view");
                }
            });
        }
    }
}
