package com.test.nick.noteshare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";
    private Context context;
    private List<CardView> bigData;
    private ArrayList<String> data;
    private GestureDetectorCompat gestureDetector;
    private ItemListener itemListener;

    public NoteAdapter(List<CardView> dataSet, ArrayList<String> words, Context parentContext){
        super();
        bigData = dataSet;
        data = words;
        context = parentContext;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{
        CardView cardView;
        TextView text;

        ViewHolder(CardView view){
            super(view);
            Log.d(TAG, "ViewHolder: " + view.toString());
            cardView = view;
            text = cardView.findViewById(R.id.sticky_text);
            gestureDetector = new GestureDetectorCompat(context, new GestureListener(view, getAdapterPosition()));
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent event){
            gestureDetector.onTouchEvent(event);
            return true;
        }
    }

    public void setListeners(ItemListener iListener){
        this.itemListener = iListener;
    }

    public interface ItemListener{
        void onItemTap(View view, int position);
        void onItemFling(View view, int position, float xVelocity);
        void onItemHold(View view, int position);
    }

    private class GestureListener extends android.view.GestureDetector.SimpleOnGestureListener{
        private View view;
        private int position;

        private GestureListener(View v, int pos){
            super();
            view = v;
            position = pos;
        }

        @Override
        public boolean onDown(MotionEvent e){
            Log.d(TAG, "OnDown: " + e.toString());
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e){
            itemListener.onItemTap(view, position);
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e){
            itemListener.onItemHold(view, position);
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            itemListener.onItemFling(view, position, velocityX);
            return true;
        }
    }
}
