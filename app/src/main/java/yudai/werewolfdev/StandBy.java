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

public class StandBy extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> players, roles;
    private boolean status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_by);

        players = getIntent().getStringArrayListExtra("players");
        roles = getIntent().getStringArrayListExtra("roles");

        for (int i = 0; i < players.size(); i++) {
            addTextView((LinearLayout) findViewById(R.id.players), players.get(i));
        }
        for (int i = 0; i < roles.size(); i++) {
            addTextView((LinearLayout) findViewById(R.id.roles), role2string(roles.get(i)));
        }

        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start:
                areYouReady();
                break;

            case R.id.back:
                super.onBackPressed();
                break;
        }
    }

    private void addTextView(LinearLayout layout, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setMaxLines(1);
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

    private void areYouReady() {
        status = false;
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_start))
                .setMessage(getString(R.string.message_start))
                .setNegativeButton(getString(R.string.no), null)
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
        ArrayList<Integer> score = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            score.add(0);
        }
        Intent intent = new Intent(this, PlayRoles.class);
        intent.putStringArrayListExtra("players", players);
        intent.putStringArrayListExtra("roles", roles);
        intent.putStringArrayListExtra("roles_default", roles);
        intent.putIntegerArrayListExtra("score", score);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
