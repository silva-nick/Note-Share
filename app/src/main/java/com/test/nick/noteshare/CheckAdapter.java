package com.test.nick.noteshare;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> {
    private static final String TAG = "NoteAdapter";

    private ArrayList<String> data;

    public CheckAdapter(ArrayList<String> words){
        super();
        data = words;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        ViewHolder v =new ViewHolder((LinearLayout) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.check, viewGroup, false));
        v.text.setFocusedByDefault(true);
        return v;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        viewHolder.text.setText(data.get(index));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    CheckAdapter getAdapter(){
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        EditText text;
        CheckBox box;

        ViewHolder(LinearLayout view){
            super(view);
            Log.d(TAG, "ViewHolder: " + view.toString());

            text = view.findViewById(R.id.check_text);
            text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    data.add("");
                    Log.d(TAG, "onEditorAction: 12873461029836032817560327856876407216509813659782134y77320856143869821658719326");
                    getAdapter().notifyDataSetChanged();
                    return false;
                }
            });

            box = view.findViewById(R.id.check);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            box.setChecked(!box.isChecked());
        }
    }
}
