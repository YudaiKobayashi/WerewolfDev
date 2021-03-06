package yudai.werewolfdev;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Conversation extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> players, roles, roles_default;
    private ArrayList<Integer> score;
    private int stealing, stolen;

    private CountDownTimer timer;
    private long countMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        findViewById(R.id.minus).setOnClickListener(this);
        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.vote).setOnClickListener(this);
        findViewById(R.id.execution).setOnClickListener(this);

        if (savedInstanceState != null) {
            players = savedInstanceState.getStringArrayList("players");
            roles = savedInstanceState.getStringArrayList("roles");
            roles_default = savedInstanceState.getStringArrayList("roles_default");
            score = savedInstanceState.getIntegerArrayList("score");
            stealing = savedInstanceState.getInt("stealing");
            stolen = savedInstanceState.getInt("stolen");

            countDown(savedInstanceState.getLong("countMillis"));
        } else {
            players = getIntent().getStringArrayListExtra("players");
            roles = getIntent().getStringArrayListExtra("roles");
            roles_default = getIntent().getStringArrayListExtra("roles_default");
            score = getIntent().getIntegerArrayListExtra("score");
            stealing = getIntent().getIntExtra("stealing", 0);
            stolen = getIntent().getIntExtra("stolen", 0);

            countDown(180000);
        }

        for (int i = 0; i < roles_default.size(); i++) {
            addTextView((LinearLayout) findViewById(R.id.roles), role2string(roles_default.get(i)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("players", players);
        outState.putStringArrayList("roles", roles);
        outState.putStringArrayList("roles_default", roles_default);
        outState.putIntegerArrayList("score", score);
        outState.putInt("stealing", stealing);
        outState.putInt("stolen", stolen);

        outState.putLong("countMillis", countMillis);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.plus:
                timer.cancel();
                timer = null;
                countDown(countMillis + 10000);
                break;

            case R.id.minus:
                timer.cancel();
                timer = null;
                countDown(countMillis - 10000);
                break;

            case R.id.vote:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.title_vote))
                        .setMessage(getString(R.string.message_vote))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        vote();
                                    }
                                })
                        .create().show();
                break;

            case R.id.execution:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.title_execution))
                        .setMessage(getString(R.string.message_execution))
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        execution();
                                    }
                                })
                        .create().show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getString(R.string.back_disabled), Toast.LENGTH_SHORT).show();
    }

    private void addTextView(LinearLayout layout, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setMaxLines(1);
        textView.setGravity(Gravity.CENTER);
        layout.addView(textView);
    }

    private String role2string(String role) {
        switch (role) {
            case "werewolf":
                return getString(R.string.werewolf);
            case "bigwolf":
                return getString(R.string.bigwolf);
            case "madman":
                return getString(R.string.madman);
            case "fortuneteller":
                return getString(R.string.fortuneteller);
            case "thief":
                return getString(R.string.thief);
            case "hunter":
                return getString(R.string.hunter);
            case "hangedman":
                return getString(R.string.hangedman);
            case "villager":
                return getString(R.string.villager);
            default:
                return "Error!";
        }
    }

    private void countDown(long count) {
        timer = new CountDownTimer(count, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                ((TextView) findViewById(R.id.timer)).setText(String.format(Locale.getDefault(), "%02d:%02d", time / 60, time % 60));
                countMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                makeToast(getString(R.string.finish_conversation));
            }
        }.start();
    }

    private void vote() {
        timer.cancel();
        Intent intent = new Intent(this, Vote.class);
        intent.putStringArrayListExtra("players", players);
        intent.putStringArrayListExtra("roles", roles);
        intent.putStringArrayListExtra("roles_default", roles_default);
        intent.putIntegerArrayListExtra("score", score);
        intent.putExtra("stealing", stealing);
        intent.putExtra("stolen", stolen);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void execution() {
        timer.cancel();
        Intent intent = new Intent(this, Execution.class);
        intent.putStringArrayListExtra("players", players);
        intent.putStringArrayListExtra("roles", roles);
        intent.putStringArrayListExtra("roles_default", roles_default);
        intent.putIntegerArrayListExtra("score", score);
        intent.putExtra("stealing", stealing);
        intent.putExtra("stolen", stolen);
        startActivity(intent);
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
