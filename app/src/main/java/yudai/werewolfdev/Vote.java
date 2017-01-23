package yudai.werewolfdev;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class Vote extends AppCompatActivity {

    private ArrayList<String> players, roles;
    private ArrayList<Integer> score, votes = new ArrayList<>();
    private int stealing, stolen;

    private int counter = 0, answer = -1;
    private boolean confirmed = false, answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        if (savedInstanceState != null) {
            players = savedInstanceState.getStringArrayList("players");
            roles = savedInstanceState.getStringArrayList("roles");
            score = savedInstanceState.getIntegerArrayList("score");
            stealing = savedInstanceState.getInt("stealing");
            stolen = savedInstanceState.getInt("stolen");

            votes = savedInstanceState.getIntegerArrayList("votes");
            counter = savedInstanceState.getInt("counter");
            answer = savedInstanceState.getInt("answer");
            confirmed = savedInstanceState.getBoolean("confirmed");
        } else {
            players = getIntent().getStringArrayListExtra("players");
            roles = getIntent().getStringArrayListExtra("roles");
            score = getIntent().getIntegerArrayListExtra("score");
            stealing = getIntent().getIntExtra("stealing", 0);
            stolen = getIntent().getIntExtra("stolen", 0);
        }

        confirmPlayer();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("players", players);
        outState.putStringArrayList("roles", roles);
        outState.putIntegerArrayList("score", score);
        outState.putInt("stealing", stealing);
        outState.putInt("stolen", stolen);

        outState.putIntegerArrayList("votes", votes);
        outState.putInt("counter", counter);
        outState.putInt("answer", answer);
        outState.putBoolean("confirmed", confirmed);
    }

    private void confirmPlayer() {
        if (counter < players.size()) {
            if (confirmed) {
                vote();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm");
                builder.setMessage(String.format("Are you really %s?", players.get(counter)));
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                confirmed = true;
                            }
                        });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (confirmed) {
                            vote();
                        } else {
                            confirmPlayer();
                        }
                    }
                });
                builder.create().show();
            }
        } else {
            Intent intent = new Intent(this, Result.class);
            intent.putStringArrayListExtra("players", players);
            intent.putStringArrayListExtra("roles", roles);
            intent.putIntegerArrayListExtra("score", score);
            intent.putIntegerArrayListExtra("votes", votes);
            intent.putExtra("stealing", stealing);
            intent.putExtra("stolen", stolen);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void vote() {
        final ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (i != counter) {
                items.add(players.get(i));
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.vote));
        builder.setSingleChoiceItems(items.toArray(new String[items.size()]), answer,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        answer = i;
                    }
                });
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        answered = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (answered && answer != -1) {
                    if (answer >= counter) {
                        answer += 1;
                    }
                    votes.add(answer);
                    confirmed = false;
                    answered = false;
                    answer = -1;
                    counter += 1;
                    confirmPlayer();
                } else {
                    vote();
                }
            }
        });
        builder.create().show();
    }
}
