package yudai.werewolfdev;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Result extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> players, roles, roles_default;
    private ArrayList<Integer> score, votes, deadIndex = new ArrayList<>();
    private int stealing = -1, stolen = -1, winner, status = 0, answer = -1;
    // in resulting(); status = 0: thievishness,
    // 1: showDead, 2: hunting, 3: showResult.
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViewById(R.id.score).setOnClickListener(this);

        if (savedInstanceState != null) {
            players = savedInstanceState.getStringArrayList("players");
            roles = savedInstanceState.getStringArrayList("roles");
            roles_default = savedInstanceState.getStringArrayList("roles_default");
            score = savedInstanceState.getIntegerArrayList("score");
            votes = savedInstanceState.getIntegerArrayList("votes");
            deadIndex = savedInstanceState.getIntegerArrayList("deadIndex");
            stealing = savedInstanceState.getInt("stealing");
            stolen = savedInstanceState.getInt("stolen");
            status = savedInstanceState.getInt("status");
            answer = savedInstanceState.getInt("answer");
        } else {
            players = getIntent().getStringArrayListExtra("players");
            roles = getIntent().getStringArrayListExtra("roles");
            roles_default = getIntent().getStringArrayListExtra("roles_default");
            score = getIntent().getIntegerArrayListExtra("score");
            votes = getIntent().getIntegerArrayListExtra("votes");
            stealing = getIntent().getIntExtra("stealing", 0);
            stolen = getIntent().getIntExtra("stolen", 0);
            makeDeadIndex();
        }
        resulting();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("players", players);
        outState.putStringArrayList("roles", roles);
        outState.putStringArrayList("roles_default", roles_default);
        outState.putIntegerArrayList("score", score);
        outState.putIntegerArrayList("votes", votes);
        outState.putIntegerArrayList("deadIndex", deadIndex);
        outState.putInt("stealing", stealing);
        outState.putInt("stolen", stolen);
        outState.putInt("status", status);
        outState.putInt("answer", answer);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getString(R.string.back_disabled), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.score:
                Intent intent = new Intent(this, Score.class);
                intent.putStringArrayListExtra("players", players);
                intent.putStringArrayListExtra("roles", roles);
                intent.putStringArrayListExtra("roles_default", roles_default);
                intent.putIntegerArrayListExtra("score", score);
                intent.putExtra("winner", winner);
                // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
        }
    }

    private void makeDeadIndex() {
        if (votes.isEmpty()) { // peaceful
            deadIndex.clear();
        } else {
            ArrayList<Integer> voted = new ArrayList<>();
            for (int i = 0; i < players.size(); i++) {
                voted.add(0);
            }
            for (int i = 0; i < voted.size(); i++) {
                voted.set(votes.get(i), voted.get(votes.get(i)) + 1);
            }
            int max = voted.get(0);
            for (int i = 1; i < voted.size(); i++) {
                if (max < voted.get(i)) {
                    max = voted.get(i);
                }
            }
            for (int i = 0; i < voted.size(); i++) {
                if (voted.get(i).equals(max)) {
                    deadIndex.add(i);
                }
            }
            if (deadIndex.size() == players.size()) { // peaceful
                deadIndex.clear();
            }
        }
    }

    private void resulting() {
        switch (status) {
            case 0:
                thievishness();
                break;
            case 1:
                showDead();
                break;
            case 2:
                hunting();
                break;
            case 3:
                showResult();
                break;

        }
    }

    private void thievishness() {
        if (stealing != -1) {
            String buf = roles.get(stealing);
            roles.set(stealing, roles.get(stolen));
            roles.set(stolen, buf);
        }
        status += 1; // go to showDead()
        resulting();
    }

    private void showDead() {
        String message = "";
        if (deadIndex.size() == 0) {
            message = getString(R.string.no_dead);
        } else {
            for (int i = 0; i < deadIndex.size(); i++) {
                if (message.length() != 0) {
                    message += "\n";
                }
                message += String.format(getString(R.string.player_is_dead), players.get(deadIndex.get(i)));
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.title_dead));
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        answered = true;
                    }
                });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (answered) {
                    status += 1; // go to hunting()
                    answered = false;
                    resulting();
                } else {
                    showDead();
                }
            }
        });
        builder.create().show();
    }

    private void hunting() {
        int hunter = -1;
        // hunter == -1 means no hunter
        for (int i = 0; i < deadIndex.size(); i++) {
            if (roles.get(deadIndex.get(i)).equals("hunter")) {
                hunter = deadIndex.get(i);
            }
        }

        if (hunter != -1) {
            final ArrayList<String> items = new ArrayList<>();

            for (int i = 0; i < players.size(); i++) {
                if (isAlive(i) && i != hunter) {
                    items.add(players.get(i));
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(String.format(getString(R.string.title_hunting), players.get(hunter)));
            builder.setSingleChoiceItems(items.toArray(new String[items.size()]), answer,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int choice) {
                            answer = choice;
                        }
                    });
            builder.setPositiveButton(getString(R.string.yes),
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
                        for (int i = 0; i < players.size(); i++) {
                            if (players.get(i).equals(items.get(answer))) {
                                deadIndex.add(i);
                            }
                        }
                        answered = false;
                        status += 1; // go to showResult()
                        resulting();
                    } else {
                        hunting();
                    }
                }
            });
            builder.create().show();
        } else {
            status += 1; // go to showResult()
            resulting();
        }
    }

    private void showResult() {
        String result;
        if (hanged()) {
            winner = 3;
        } else if (!noWerewolf()) {
            if (!noVillager()) {
                if (deadIndex.isEmpty()) {
                    winner = 2;
                } else if (werewolfIsDead()) {
                    winner = 1;
                } else {
                    winner = 2;
                }
            } else { // no villager
                if (werewolfIsDead()) {
                    winner = 1;
                } else {
                    winner = 2;
                }
            }
        } else { // no werewolf
            if (deadIndex.isEmpty()) {
                winner = 1;
            } else {
                winner = 2;
            }
        }

        /*else if (werewolfIsDead() || noWerewolf()) {
            result = getString(R.string.villagers_win);
            winner = 1;
        } else {
            result = getString(R.string.werewolves_win);
            winner = 2;
        }*/

        // result
        switch (winner) {
            case 1:
                result = getString(R.string.villagers_win);
                break;
            case 2:
                result = getString(R.string.werewolves_win);
                break;
            case 3:
                result = getString(R.string.hangedman_win);
                break;
            default:
                result = "Error!";
        }

        ((TextView) findViewById(R.id.result)).setText(result);

        // dead
        if (deadIndex.size() != players.size()) {
            for (int i = 0; i < players.size(); i++) {
                if (!isAlive(i)) {
                    addImageView((LinearLayout) findViewById(R.id.dead), R.drawable.cross);
                } else {
                    addTextView((LinearLayout) findViewById(R.id.dead), "");
                }
            }
        }
        // players
        for (int i = 0; i < players.size(); i++) {
            addTextView((LinearLayout) findViewById(R.id.players), players.get(i));
        }
        // steal
        if (stealing != -1) {
            int up, down;
            if (stealing < stolen) {
                up = stealing;
                down = stolen;
            } else {
                up = stolen;
                down = stealing;
            }
            for (int i = 0; i < players.size(); i++) {
                if (i == up) {
                    addImageView((LinearLayout) findViewById(R.id.steal), R.drawable.arrow_up);
                } else if (i == down) {
                    addImageView((LinearLayout) findViewById(R.id.steal), R.drawable.arrow_down);
                } else {
                    addTextView((LinearLayout) findViewById(R.id.steal), "");
                }
            }
        }
        // roles
        for (int i = 0; i < roles.size(); i++) {
            addTextView((LinearLayout) findViewById(R.id.roles), role2string(roles.get(i)));
        }
        // votes
        for (int i = 0; i < votes.size(); i++) {
            addTextView((LinearLayout) findViewById(R.id.votes), players.get(votes.get(i)));
        }

        // adjust layouts
        /*findViewById(R.id.dead).setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        findViewById(R.id.players).setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        findViewById(R.id.steal).setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        findViewById(R.id.roles).setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        findViewById(R.id.votes).setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));*/
    }

    private boolean hanged() {
        for (int i = 0; i < deadIndex.size(); i++) {
            if (roles.get(deadIndex.get(i)).equals("hangedman")) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlive(int index) {
        for (int i = 0; i < deadIndex.size(); i++) {
            if (index == deadIndex.get(i)) {
                return false;
            }
        }
        return true;
    }

    private boolean werewolfIsDead() {
        for (int i = 0; i < deadIndex.size(); i++) {
            if (roles.get(deadIndex.get(i)).equals("werewolf") || roles.get(deadIndex.get(i)).equals("bigwolf")) {
                return true;
            }
        }
        return false;
    }

    private boolean noWerewolf() {
        for (int i = 0; i < players.size(); i++) {
            if (roles.get(i).equals("werewolf") || roles.get(i).equals("bigwolf")) {
                return false;
            }
        }
        return true;
    }

    private boolean noVillager() {
        for (int i = 0; i < players.size(); i++) {
            if (roles.get(i).equals("fortuneteller")
                    || roles.get(i).equals("thief")
                    || roles.get(i).equals("hunter")
                    || roles.get(i).equals("villager")) {
                return false;
            }
        }
        return true;
    }

    private void addTextView(LinearLayout layout, String text) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) ((32 * this.getResources().getDisplayMetrics().density) + 0.5)
        ));
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setMaxLines(1);
        layout.addView(textView);
    }

    private void addImageView(LinearLayout layout, int image) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int) ((32 * this.getResources().getDisplayMetrics().density) + 0.5)
        ));
        imageView.setImageResource(image);
        layout.addView(imageView);
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
}
