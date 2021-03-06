package com.gdgistanbul.devfest.feedback.activities;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gdgistanbul.devfest.feedback.R;
import com.gdgistanbul.devfest.feedback.adapters.SessionsAdapter;
import com.gdgistanbul.devfest.feedback.interfaces.SessionItemListener;
import com.gdgistanbul.devfest.feedback.models.Session;
import com.gdgistanbul.devfest.feedback.util.FileUtil;
import com.gdgistanbul.devfest.feedback.util.PrefUtils;
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

public class SessionsActivity extends AppCompatActivity implements SessionItemListener {

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

  public static Intent newInstance(Context context) {
    Intent intent = new Intent(context, SessionsActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sessions);
    Timber.tag(TAG);
    ButterKnife.bind(this);
    setupViews();
    readSessions();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.export, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_open_file:
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setDataAndType(Uri.fromFile(FileUtil.getExportFile(this)), "text/csv");
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
          startActivity(newIntent);
        } catch (ActivityNotFoundException e) {
          Toast.makeText(this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
        break;
      case R.id.menu_item_copy_file_path:
        ClipboardManager clipboard = (ClipboardManager)
            getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Export File", FileUtil.getExportFile(this).getAbsolutePath());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Dosya : " + FileUtil.getExportFile(this).getAbsolutePath(), Toast.LENGTH_SHORT).show();
        break;
      case R.id.menu_item_send_file:
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(FileUtil.getExportFile(this)));
        shareIntent.setType("text/csv");
        startActivity(Intent.createChooser(shareIntent, "Feedback File"));
        break;
    }
    return true;
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
    adapter = new SessionsAdapter(new ArrayList<Session>(), this);
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
        Timber.e(e, "readSessions Error");
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

  @Override
  public void onSessionSelect(Session session) {
    PrefUtils.setCurrentSession(this, session);
    startActivity(FeedbackActivity.newInstance(this));
  }
}
