package com.test.mydemotest2;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class JokeAdapter extends RecyclerView.Adapter<JokeAdapter.ViewHolder>{
    private List<Joke> mJokeList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View jokeView;
        TextView jokeTitle;
        public ViewHolder(View view){
            super(view);
            jokeView = view;
            jokeTitle = (TextView)view.findViewById(R.id.joke_list);
        }
    }
    public JokeAdapter(List<Joke>jokeList){
        mJokeList = jokeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.joke_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.jokeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Joke joke = mJokeList.get(position);
                Intent intent = new Intent(v.getContext(),JokeActivity.class);
                intent.putExtra("joke",joke);
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Joke j = mJokeList.get(position);
        holder.jokeTitle.setText(j.getTitle());
    }

    @Override
    public int getItemCount() {
        return mJokeList.size();
    }
}
