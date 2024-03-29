package com.test.nick.noteshare;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.nick.noteshare.data.Note;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";
    private Context context;
    private ArrayList<Note> data;
    private GestureDetectorCompat gestureDetector;
    private ItemListener itemListener;

    public NoteAdapter(ArrayList<Note> words, Context parentContext){
        super();
        data = words;
        context = parentContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        switch (type){
            case 0:
                return new StickyViewHolder((CardView) LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.sticky_note, viewGroup, false));
            case 1:
                return new CheckViewHolder((CardView) LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.check_note, viewGroup, false));
            case 2:
                return new EventViewHolder((CardView) LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.event_note, viewGroup, false));
            case 3:
                return new GoalViewHolder((CardView)LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.goal_note, viewGroup, false));
            default:
                return new StickyViewHolder((CardView) LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.sticky_note, viewGroup, false));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        switch (viewHolder.getItemViewType()){
            case 0:
                viewHolder.title.setText(data.get(index).title);
                ((StickyViewHolder)viewHolder).bodyText.setText(data.get(index).body);
                break;
            case 1:
                CheckViewHolder c = (CheckViewHolder) viewHolder;
                c.title.setText(data.get(index).title);
                String [] miniArray = data.get(index).body.split(MainActivity.breakCode);
                c.miniList.removeAllViews();
                for (int i = 0; i< miniArray.length; i++){
                    LinearLayout temp = (LinearLayout) LayoutInflater.from(context)
                            .inflate(R.layout.check_note_check, c.miniList, false);
                    ((TextView)temp.findViewById(R.id.check_text)).setText(miniArray[i].substring(1));
                    ((CheckBox)temp.findViewById(R.id.check)).setChecked((miniArray[i].charAt(0)== 't'));
                    c.miniList.addView(temp);
                }
                break;
            case 2:
                EventViewHolder e = (EventViewHolder)viewHolder;
                e.title.setText(data.get(index).title);
                String [] temp = data.get(index).body.split(MainActivity.breakCode);
                if(!temp[0].equals("")){
                    String [] s = temp[0].split("/");
                    e.date.setText(s[1]);
                    e.time.setText(temp[1]);
                } else {
                    LocalDateTime dt = LocalDateTime.now();
                    e.date.setText(dt.now().getDayOfMonth() + "");
                    e.time.setText(dt.now().getHour() + ":" + dt.now().getMinute());
                }
                break;
            case 3:
                GoalViewHolder g = (GoalViewHolder)viewHolder;
                g.title.setText(data.get(index).title);
                String [] tempString = data.get(index).extra.split(MainActivity.breakCode);
                g.runner.animate().setDuration(0).translationX(g.runner.getX() + ((float)Integer.parseInt(tempString[0]))/Integer.parseInt(tempString[1]) * 200);
                g.completion.setText(Integer.parseInt(tempString[1])- Integer.parseInt(tempString[0]) + " more times this " + data.get(index).body.split(MainActivity.breakCode)[0]);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: " + data.get(position).toString());
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{
        private CardView cardView;
        public TextView title;

        ViewHolder(CardView view){
            super(view);
            Log.d(TAG, "ViewHolder: " + view.toString());
            cardView = view;
            title = cardView.findViewById(R.id.note_title);
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

    private class StickyViewHolder extends NoteAdapter.ViewHolder{
        private TextView bodyText;

        StickyViewHolder(CardView view){
            super(view);
            bodyText = view.findViewById(R.id.note_text);
        }
    }

    private class CheckViewHolder extends NoteAdapter.ViewHolder{
        private LinearLayout miniList;

        CheckViewHolder(CardView view){
            super(view);
            miniList = view.findViewById(R.id.mini_check_list);
        }
    }

    private class EventViewHolder extends NoteAdapter.ViewHolder{
        private TextView date;
        private TextView time;

        EventViewHolder(CardView view){
            super(view);
            date = view.findViewById(R.id.event_note_date);
            time = view.findViewById(R.id.event_note_time);
        }
    }

    private class GoalViewHolder extends NoteAdapter.ViewHolder{
        private ImageView runner;
        private TextView completion;

        GoalViewHolder(CardView view){
            super(view);
            runner = view.findViewById(R.id.goal_runner);
            completion = view.findViewById(R.id.goal_completion);
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
