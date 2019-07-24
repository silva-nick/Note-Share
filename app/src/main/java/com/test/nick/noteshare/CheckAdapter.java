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
        if (type == 1){
            return new ViewHolder1((LinearLayout) LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.check_plus, viewGroup, false));
        }

        ViewHolder v = new ViewHolder((LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.check, viewGroup, false));
        v.text.setFocusedByDefault(true);
        return v;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).equals("-100")){return 1;}
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int index) {
        if (data.get(index).equals("-100")){
            return;
        }
        ((ViewHolder)viewHolder).listener.setPosition(index);
        ((ViewHolder)viewHolder).text.setText(data.get(index));
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
                    data.add(data.size() - 1, "");
                    //update the arraylist
                    getAdapter().notifyDataSetChanged();
                    return false;
                }
            });
            text.setFocusedByDefault(true);
            layout = view;

            view.findViewById(R.id.check_delete).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    data.remove(0);
                    notifyDataSetChanged();
                }
            });

            box = view.findViewById(R.id.check);
            view.setOnClickListener(this);
            listener = new TextListener();
            this.text.addTextChangedListener(listener);
        }

        @Override
        public void onClick(View view) {
            box.setChecked(!box.isChecked());
            layout.findViewById(R.id.check_delete).setVisibility(View.VISIBLE);
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder{
        ViewHolder1(LinearLayout view){
            super(view);
            Log.d(TAG, "ViewHolder: " + view.toString());
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
            data.set(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    }
}
