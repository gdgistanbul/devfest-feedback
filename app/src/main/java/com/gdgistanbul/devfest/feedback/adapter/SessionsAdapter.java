package com.gdgistanbul.devfest.feedback.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gdgistanbul.devfest.feedback.R;
import com.gdgistanbul.devfest.feedback.holder.SessionItemViewHolder;
import com.gdgistanbul.devfest.feedback.model.Session;

import java.util.List;

public class SessionsAdapter extends RecyclerView.Adapter<SessionItemViewHolder> {

  private static final String TAG = SessionsAdapter.class.getSimpleName();

  private List<Session> list;

  public SessionsAdapter(List<Session> list) {
    setList(list);
  }

  @SuppressWarnings("InflateParams")
  @Override
  public SessionItemViewHolder onCreateViewHolder(ViewGroup parent, int type) {
    return new SessionItemViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_session, null));
  }

  @Override
  public void onBindViewHolder(SessionItemViewHolder viewHolder, int position) {
    viewHolder.bind(list.get(position));
  }

  public void setList(List<Session> list) {
    this.list = list;
  }

  @Override
  public int getItemCount() {
    return list.size();
  }
}