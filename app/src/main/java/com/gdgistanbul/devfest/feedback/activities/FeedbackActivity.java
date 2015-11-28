package com.gdgistanbul.devfest.feedback.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gdgistanbul.devfest.feedback.R;
import com.gdgistanbul.devfest.feedback.models.Session;
import com.gdgistanbul.devfest.feedback.util.PrefUtils;

import butterknife.ButterKnife;
import timber.log.Timber;

public class FeedbackActivity extends AppCompatActivity {

  public static final String TAG = FeedbackActivity.class.getSimpleName();

  private Session session;

  public static Intent newInstance(Context context) {
    Intent intent = new Intent(context, FeedbackActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback);
    Timber.tag(TAG);
    ButterKnife.bind(this);
    readSession();
    setupViews();
  }

  private void readSession() {
    session = PrefUtils.getCurrentSession(this);
    if (session == null) {
      Toast.makeText(this, R.string.feedback_session_error, Toast.LENGTH_SHORT).show();
      onBackPressed();
      finish();
      return;
    }
  }

  private void setupViews() {

  }

  @Override
  public void onBackPressed() {
    startActivity(SessionsActivity.newInstance(this));
  }
}
