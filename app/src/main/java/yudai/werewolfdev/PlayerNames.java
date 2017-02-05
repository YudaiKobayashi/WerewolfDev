package yudai.werewolfdev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class PlayerNames extends AppCompatActivity implements View.OnClickListener {

    private int numberOfPlayers, index[];
    private ArrayList<String> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_names);

        numberOfPlayers = getIntent().getIntExtra("numberOfPlayers", 0);
        index = new int[numberOfPlayers];
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.players);

        for (int i = 0; i < numberOfPlayers - 1; i++) {
            index[i] = View.generateViewId();
            newPlayer(linearLayout, i + 1, index[i], EditorInfo.IME_ACTION_NEXT);
        }
        index[numberOfPlayers - 1] = View.generateViewId();
        newPlayer(linearLayout, numberOfPlayers, index[numberOfPlayers - 1], EditorInfo.IME_ACTION_DONE);

        if (savedInstanceState != null) {
            for (int i = 0; i < numberOfPlayers; i++) {
                players = savedInstanceState.getStringArrayList("players");
                ((EditText) findViewById(index[i])).setText(players.get(i));
            }
        }

        findViewById(R.id.next).setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("players", getPlayersArrayList());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                players = getPlayersArrayList();
                if (containEmpty(players)) {
                    Toast.makeText(this, getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
                } else if (duplicated(players)) {
                    Toast.makeText(this, getString(R.string.duplicated_names), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(this, SetRoles.class);
                    intent.putExtra("numberOfPlayers", numberOfPlayers);
                    intent.putStringArrayListExtra("players", players);
                    startActivity(intent);
                }
                break;
        }
    }

    private void newPlayer(LinearLayout layout, int number, int id, int action) {
        LinearLayout linearLayout = new LinearLayout(this);
        TextView index = new TextView(this);
        EditText name = new EditText(this);

        index.setLayoutParams(new LinearLayout.LayoutParams(
                (int) ((32 * this.getResources().getDisplayMetrics().density) + 0.5),
                LinearLayout.LayoutParams.WRAP_CONTENT));
        index.setText(String.valueOf(number));
        index.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                (int) ((0 * this.getResources().getDisplayMetrics().density) + 0.5),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        nameParams.weight = 1;
        name.setId(id);
        name.setLayoutParams(nameParams);
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setHint("Player " + String.valueOf(number));
        //String playerName = "Player " + String.valueOf(number);
        //name.setText(playerName);
        name.setGravity(Gravity.CENTER_VERTICAL);
        name.setImeOptions(action);

        linearLayout.addView(index);
        linearLayout.addView(name);
        layout.addView(linearLayout);
    }

    private ArrayList<String> getPlayersArrayList() {
        EditText input;
        ArrayList<String> players = new ArrayList<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            input = (EditText) findViewById(index[i]);
            players.add(input.getText().toString());
        }
        return players;
    }

    private boolean duplicated(ArrayList<String> arrayList) {
        Set<String> set = new HashSet<>(arrayList);
        return set.size() != arrayList.size();
    }

    private boolean containEmpty(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).length() == 0) {
                return true;
            }
        }
        return false;
    }
}
