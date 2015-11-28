package com.gdgistanbul.devfest.feedback.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.gdgistanbul.devfest.feedback.R;
import com.gdgistanbul.devfest.feedback.holders.SessionItemViewHolder;
import com.gdgistanbul.devfest.feedback.interfaces.SessionItemListener;
import com.gdgistanbul.devfest.feedback.models.Session;

import java.util.List;

public class SessionsAdapter extends RecyclerView.Adapter<SessionItemViewHolder> {

  private static final String TAG = SessionsAdapter.class.getSimpleName();

  private List<Session> list;
  private SessionItemListener listener;

  public SessionsAdapter(List<Session> list, SessionItemListener listener) {
    setList(list);
    this.listener = listener;
  }

  @SuppressWarnings("InflateParams")
  @Override
  public SessionItemViewHolder onCreateViewHolder(ViewGroup parent, int type) {
    return new SessionItemViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_session, null), listener);
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