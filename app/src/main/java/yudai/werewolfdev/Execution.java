package yudai.werewolfdev;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class Execution extends AppCompatActivity {

    private final ArrayList<Integer> votes = new ArrayList<>();
    private ArrayList<String> players, roles, roles_default;
    private ArrayList<Integer> score;
    private int stealing, stolen, answer = -1;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_execution);

        if (savedInstanceState != null) {
            players = savedInstanceState.getStringArrayList("players");
            roles = savedInstanceState.getStringArrayList("roles");
            roles_default = savedInstanceState.getStringArrayList("roles_default");
            score = savedInstanceState.getIntegerArrayList("score");
            stealing = savedInstanceState.getInt("stealing");
            stolen = savedInstanceState.getInt("stolen");

            answer = savedInstanceState.getInt("answer");
        } else {
            players = getIntent().getStringArrayListExtra("players");
            roles = getIntent().getStringArrayListExtra("roles");
            roles_default = getIntent().getStringArrayListExtra("roles_default");
            score = getIntent().getIntegerArrayListExtra("score");
            stealing = getIntent().getIntExtra("stealing", 0);
            stolen = getIntent().getIntExtra("stolen", 0);
        }

        execute();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("players", players);
        outState.putStringArrayList("roles", roles);
        outState.putStringArrayList("roles_default", roles_default);
        outState.putIntegerArrayList("score", score);
        outState.putInt("stealing", stealing);
        outState.putInt("stolen", stolen);

        outState.putInt("answer", answer);
    }

    private void execute() {
        final ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            items.add(players.get(i));
        }
        items.add(getString(R.string.peaceful));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_execution));
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
                    if (answer == players.size()) {
                        votes.clear();
                    } else {
                        for (int i = 0; i < players.size(); i++) {
                            votes.add(answer);
                        }
                    }
                    result();
                } else {
                    execute();
                }
            }
        });
        builder.create().show();
    }

    private void result() {
        Intent intent = new Intent(this, Result.class);
        intent.putStringArrayListExtra("players", players);
        intent.putStringArrayListExtra("roles", roles);
        intent.putStringArrayListExtra("roles_default", roles_default);
        intent.putIntegerArrayListExtra("score", score);
        intent.putIntegerArrayListExtra("votes", votes);
        intent.putExtra("stealing", stealing);
        intent.putExtra("stolen", stolen);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
