package com.gdgistanbul.devfest.feedback.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gdgistanbul.devfest.feedback.R;
import com.gdgistanbul.devfest.feedback.interfaces.SessionItemListener;
import com.gdgistanbul.devfest.feedback.models.Session;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionItemViewHolder extends RecyclerView.ViewHolder {

  public static final String TAG = SessionItemViewHolder.class.getSimpleName();

  @Bind(R.id.title)
  protected TextView titleTextView;
  @Bind(R.id.description)
  protected TextView descriptionTextView;

  private SessionItemListener listener;
  private Session session;

  public SessionItemViewHolder(View view, SessionItemListener listener) {
    super(view);
    ButterKnife.bind(this, view);
    this.listener = listener;
  }

  public void bind(Session session) {
    this.session = session;
    titleTextView.setText(session.title);
    descriptionTextView.setText(session.description);
  }

  @OnClick(R.id.session_item)
  protected void onClickSession() {
    if (listener != null) {
      listener.onSessionSelect(session);
    }
  }
}

