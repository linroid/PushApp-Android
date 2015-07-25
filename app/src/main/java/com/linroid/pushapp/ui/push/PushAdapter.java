package com.linroid.pushapp.ui.push;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Push;
import com.linroid.pushapp.ui.base.DataAdapter;

/**
 * Created by linroid on 7/20/15.
 */
public class PushAdapter extends DataAdapter<Push, PushAdapter.PushHolder> {

    @Override
    public int getItemCount() {
//        return data==null ? 0 : data.size();
        return 3;
    }

    @Override
    public PushHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_push, viewGroup, false);
        return new PushHolder(view);
    }

    @Override
    public void onBindViewHolder(PushHolder pushHolder, int i) {

    }


    class PushHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public PushHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
