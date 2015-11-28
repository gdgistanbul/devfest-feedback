package com.gdgistanbul.devfest.feedback.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gdgistanbul.devfest.feedback.R;
import com.gdgistanbul.devfest.feedback.models.Session;
import com.gdgistanbul.devfest.feedback.util.PrefUtils;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class FeedbackActivity extends AppCompatActivity {

  public static final String TAG = FeedbackActivity.class.getSimpleName();

  @Bind(R.id.feedback_good)
  protected View feedbackGoodView;
  @Bind(R.id.feedback_neutral)
  protected View feedbackNeutralView;
  @Bind(R.id.feedback_bad)
  protected View feedbackBadView;

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
    RxView
        .clicks(feedbackBadView)
        .debounce(1000, TimeUnit.MILLISECONDS)
        .subscribe(new Subscriber<Void>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
          }

          @Override
          public void onNext(Void aVoid) {
          }
        });
  }

  @Override
  public void onBackPressed() {
    startActivity(SessionsActivity.newInstance(this));
  }
}
