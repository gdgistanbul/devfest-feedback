package com.gdgistanbul.devfest.feedback;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gdgistanbul.devfest.feedback.adapter.SessionsAdapter;
import com.gdgistanbul.devfest.feedback.model.Session;
import com.google.gson.Gson;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class SessionsActivity extends AppCompatActivity {

  public static final String TAG = SessionsActivity.class.getSimpleName();

  @Bind(R.id.progress)
  protected View progressLayoutView;
  @Bind(R.id.list)
  protected View listLayoutView;
  @Bind(R.id.result)
  protected RecyclerView resultView;
  @Bind(R.id.query)
  protected EditText queryEditText;

  private List<Session> sessions;
  private List<Session> filteredSessions;
  private SessionsAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sessions);
    Timber.tag(TAG);
    ButterKnife.bind(this);
    setupViews();
    readSessions();
  }

  private void setupViews() {
    // layouts
    progressLayoutView.setVisibility(View.VISIBLE);
    listLayoutView.setVisibility(View.GONE);
    // resultView
    resultView.setHasFixedSize(true);
    resultView.setItemAnimator(new DefaultItemAnimator());
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    resultView.setLayoutManager(linearLayoutManager);
    adapter = new SessionsAdapter(new ArrayList<Session>());
    resultView.setAdapter(adapter);
    // query editText
    RxTextView
        .textChanges(queryEditText)
        .debounce(200, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<CharSequence>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            Timber.e(e, "RxTextView queryEditText");
          }

          @Override
          public void onNext(CharSequence charSequence) {
            String query = charSequence.toString().trim().toLowerCase();
            if (TextUtils.isEmpty(query)) {
              filteredSessions = sessions;
            } else {
              filteredSessions = new ArrayList<>();
              for (Session session : sessions) {
                if (session.contains(query)) {
                  filteredSessions.add(session);
                }
              }
            }
            adapter.setList(filteredSessions);
            adapter.notifyDataSetChanged();
          }
        });
  }

  private void readSessions() {
    Observable.create(new Observable.OnSubscribe<List<Session>>() {
      @Override
      public void call(Subscriber<? super List<Session>> subscriber) {
        try {
          InputStream is = getResources().openRawResource(R.raw.sessions);
          subscriber.onNext(Arrays.asList(new Gson().fromJson(new InputStreamReader(is), Session[].class)));
        } catch (Exception e) {
          subscriber.onError(e);
        }
        subscriber.onCompleted();
      }
    }).subscribe(new Subscriber<List<Session>>() {
      @Override
      public void onCompleted() {
        progressLayoutView.setVisibility(View.GONE);
        listLayoutView.setVisibility(View.VISIBLE);
      }

      @Override
      public void onError(Throwable e) {
        Timber.e(e, "Error");
        Toast.makeText(SessionsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onNext(List<Session> sessions) {
        SessionsActivity.this.sessions = sessions;
        adapter.setList(sessions);
        adapter.notifyDataSetChanged();
      }
    });
  }
}
