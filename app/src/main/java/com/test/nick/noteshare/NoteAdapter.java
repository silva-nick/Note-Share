package com.test.nick.noteshare;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";
    private Context context;
    private ArrayList<String> data;
    private GestureDetectorCompat gestureDetector;
    private ItemListener itemListener;

    public NoteAdapter(ArrayList<String> words, Context parentContext){
        super();
        data = words;
        context = parentContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        switch (type){
            case 0:
                return new ViewHolder((CardView) LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.check_note, viewGroup, false));
            case 1:
                return new ViewHolder((CardView) LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.sticky_note, viewGroup, false));
            default:
                return new ViewHolder((CardView) LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.sticky_note, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        viewHolder.text.setText(data.get(index));
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).equals("1") ? 0 : 1;
    }

    @Override
    public int getItemCount() {
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
            gestureDetector = new GestureDetectorCompat(context, new GestureListener());
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent event){
            Log.d(TAG, "onTouch: " + view.equals(cardView));
            gestureDetector.onTouchEvent(event);
            itemListener.sendEvent(view, getAdapterPosition());
            return true;
        }
    }

    public void setListeners(ItemListener iListener){
        this.itemListener = iListener;
    }

    public interface ItemListener{
        void onItemTap();
        void onItemFling(float xVelocity);
        void onItemHold();
        void sendEvent(View view, int position);
    }

    private class GestureListener extends android.view.GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent e){
            Log.d(TAG, "OnDown: ");
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e){
            itemListener.onItemTap();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e){
            itemListener.onItemHold();
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            itemListener.onItemFling(velocityX);
            return true;
        }
    }
}
