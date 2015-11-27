package com.gdgistanbul.devfest.feedback.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gdgistanbul.devfest.feedback.R;
import com.gdgistanbul.devfest.feedback.model.Session;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SessionItemViewHolder extends RecyclerView.ViewHolder {

  public static final String TAG = SessionItemViewHolder.class.getSimpleName();

  @Bind(R.id.title)
  protected TextView titleTextView;
  @Bind(R.id.description)
  protected TextView descriptionTextView;

  public SessionItemViewHolder(View view) {
    super(view);
    ButterKnife.bind(this, view);
  }

  public void bind(Session session) {
    titleTextView.setText(session.title);
    descriptionTextView.setText(session.description);
  }
}

