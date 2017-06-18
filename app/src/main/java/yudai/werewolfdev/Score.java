package yudai.werewolfdev;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Score extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> players, roles, roles_default, added;
    private ArrayList<Integer> score;
    private int winner;
    private boolean played = false, status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        if (savedInstanceState != null) {
            players = savedInstanceState.getStringArrayList("players");
            roles = savedInstanceState.getStringArrayList("roles");
            roles_default = savedInstanceState.getStringArrayList("roles_default");
            added = savedInstanceState.getStringArrayList("added");
            score = savedInstanceState.getIntegerArrayList("score");
            winner = savedInstanceState.getInt("winner");
            played = savedInstanceState.getBoolean("played");
        } else {
            players = getIntent().getStringArrayListExtra("players");
            roles = getIntent().getStringArrayListExtra("roles");
            roles_default = getIntent().getStringArrayListExtra("roles_default");
            score = getIntent().getIntegerArrayListExtra("score");
            winner = getIntent().getIntExtra("winner", 0);
        }

        findViewById(R.id.newGame).setOnClickListener(this);

        if (!played) {
            addScore();
        }
        showScore();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("players", players);
        outState.putStringArrayList("roles", roles);
        outState.putStringArrayList("roles_default", roles_default);
        outState.putStringArrayList("added", added);
        outState.putIntegerArrayList("score", score);
        outState.putInt("winner", winner);
        outState.putBoolean("played", played);
    }

    /*@Override
    public void onBackPressed() {
        Toast.makeText(this, "Back button is disabled.", Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newGame:
                areYouReady();
        }
    }

    private void addScore() {
        added = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            added.add("");
        }
        if (winner == 1) {
            for (int i = 0; i < players.size(); i++) {
                if ((roles.get(i)).equals("fortuneteller")
                        || roles.get(i).equals("thief")
                        || roles.get(i).equals("hunter")
                        || roles.get(i).equals("villager")
                        || (noWerewolf() && roles.get(i).equals("madman"))) {
                    score.set(i, score.get(i) + 1);
                    added.set(i, "+1");
                }
            }
        } else if (winner == 2) {
            for (int i = 0; i < players.size(); i++) {
                if (roles.get(i).equals("werewolf")
                        || roles.get(i).equals("bigwolf")
                        || (!noWerewolf() && roles.get(i).equals("madman"))) {
                    score.set(i, score.get(i) + 2);
                    added.set(i, "+2");
                }
            }
        } else if (winner == 3) {
            for (int i = 0; i < players.size(); i++) {
                if (roles.get(i).equals("hangedman")) {
                    score.set(i, score.get(i) + 3);
                    added.set(i, "+3");
                }
            }
        }
        played = true;
    }

    private boolean noWerewolf() {
        for (int i = 0; i < players.size(); i++) {
            if (roles.get(i).equals("werewolf") || roles.get(i).equals("bigwolf")) {
                return false;
            }
        }
        return true;
    }

    // show* must be fixed.
    private void showScore() {
        showResultString((LinearLayout) findViewById(R.id.nameList), players);
        showResultInteger((LinearLayout) findViewById(R.id.score), score);
        showResultString((LinearLayout) findViewById(R.id.added), added);
    }

    private void showResultString(LinearLayout layout, ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            TextView string = new TextView(this);
            string.setText(list.get(i));
            string.setMaxLines(1);
            layout.addView(string);
        }
    }

    private void showResultInteger(LinearLayout layout, ArrayList<Integer> list) {
        for (int i = 0; i < list.size(); i++) {
            TextView integer = new TextView(this);
            integer.setText(String.valueOf(list.get(i)));
            layout.addView(integer);
        }
    }

    private void areYouReady() {
        status = false;
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_start))
                .setMessage(getString(R.string.message_start))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                status = true;
                            }
                        })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        if (status) {
                            startGame();
                        }
                    }
                })
                .show();
    }

    private void startGame() {
        Intent intent = new Intent(this, PlayRoles.class);
        intent.putStringArrayListExtra("players", players);
        intent.putStringArrayListExtra("roles", roles);
        intent.putStringArrayListExtra("roles_default", roles_default);
        intent.putIntegerArrayListExtra("score", score);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
