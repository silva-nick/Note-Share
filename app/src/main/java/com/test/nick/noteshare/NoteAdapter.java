package com.test.nick.noteshare;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";
    private List<CardView> bigData;
    private ArrayList<String> data;
    private ItemClickListener clickListener;
    private ItemHoldListener holdListener;

    public NoteAdapter(List<CardView> dataSet, ArrayList<String> words){
        super();
        bigData = dataSet;
        data = words;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sticky_note, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.text.setText(data.get(i));
    }

    @Override
    public int getItemCount() {
        //return bigData.size();
        return data.size();
    }

    public void setClickListener(ItemClickListener listener){
        this.clickListener = listener;
    }

    public void setLongClickListener(ItemHoldListener listener){
        this.holdListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        CardView cardView;
        TextView text;

        ViewHolder(CardView view){
            super(view);
            Log.d(TAG, "ViewHolder: " + view.toString());
            cardView = view;
            text = cardView.findViewById(R.id.sticky_text);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view){
            if(holdListener != null) holdListener.onItemHold(view, getAdapterPosition());
            return true;
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface ItemHoldListener{
        void onItemHold(View view, int position);
    }
}
