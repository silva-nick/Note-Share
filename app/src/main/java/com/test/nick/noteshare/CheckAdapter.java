package com.test.nick.noteshare;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckAdapter extends RecyclerView.Adapter {
    private static final String TAG = "NoteAdapter";

    private ArrayList<String> data;

    public CheckAdapter(ArrayList<String> words){
        super();
        data = words;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {

        ViewHolder v = new ViewHolder((LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.check, viewGroup, false));
        v.text.setFocusedByDefault(true);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int index) {
        ((ViewHolder)viewHolder).listener.setPosition(index);
        ((ViewHolder)viewHolder).text.setText(data.get(index).substring(1));
        ((ViewHolder)viewHolder).box.setChecked(data.get(index).charAt(0) == 't');
        Log.d(TAG, "onBindViewHolder:        " + data.get(index));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private CheckAdapter getAdapter(){
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        EditText text;
        CheckBox box;
        LinearLayout layout;
        TextListener listener;

        ViewHolder(LinearLayout view){
            super(view);
            Log.d(TAG, "ViewHolder: " + view.toString());

            text = view.findViewById(R.id.check_text);
            text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    data.add(data.size(), "t");
                    //update the arraylist
                    getAdapter().notifyDataSetChanged();
                    return false;
                }
            });

            layout = view;

            box = view.findViewById(R.id.check);
            box.setOnClickListener(this);
            listener = new TextListener();
            this.text.addTextChangedListener(listener);
        }

        @Override
        public void onClick(View view) {
            boolean val = box.isChecked();
            if(val){
                Button delete = new Button(layout.getContext());
                delete.setAlpha(0f);
                layout.addView(delete);
                delete.animate().alpha(1f).setDuration(750);
                delete.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        data.remove(0);
                        layout.removeViewAt(layout.getChildCount() - 1);
                        notifyDataSetChanged();
                    }
                });
            } else {layout.removeViewAt(layout.getChildCount() - 1);}
            data.set(getAdapterPosition(),  data.get(getAdapterPosition()).replaceFirst(val ? "f" : "t", val ? "t" : "f"));
            Log.d(TAG, "onClick: " + val + "-----------" + data.get(getAdapterPosition()));
        }
    }

    private class TextListener implements TextWatcher {
        private int position;

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) { }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            data.set(position, data.get(position).charAt(0) + charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    }
}
