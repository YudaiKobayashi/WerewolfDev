package yudai.werewolfdev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class SetRoles extends AppCompatActivity implements View.OnClickListener {

    private TextView
            werewolf, bigwolf, madman, fortuneteller, thief, hunter, hangedman, villager;

    private ArrayList<String> players = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_roles);

        players = getIntent().getStringArrayListExtra("players");

        werewolf = (TextView) findViewById(R.id.werewolf);
        bigwolf = (TextView) findViewById(R.id.bigwolf);
        madman = (TextView) findViewById(R.id.madman);
        fortuneteller = (TextView) findViewById(R.id.fortuneteller);
        thief = (TextView) findViewById(R.id.thief);
        hunter = (TextView) findViewById(R.id.hunter);
        hangedman = (TextView) findViewById(R.id.hangedman);
        villager = (TextView) findViewById(R.id.villager);

        findViewById(R.id.werewolf_minus).setOnClickListener(this);
        findViewById(R.id.werewolf_plus).setOnClickListener(this);
        findViewById(R.id.bigwolf_minus).setOnClickListener(this);
        findViewById(R.id.bigwolf_plus).setOnClickListener(this);
        findViewById(R.id.madman_minus).setOnClickListener(this);
        findViewById(R.id.madman_plus).setOnClickListener(this);
        findViewById(R.id.fortuneteller_minus).setOnClickListener(this);
        findViewById(R.id.fortuneteller_plus).setOnClickListener(this);
        findViewById(R.id.thief_minus).setOnClickListener(this);
        findViewById(R.id.thief_plus).setOnClickListener(this);
        findViewById(R.id.hunter_minus).setOnClickListener(this);
        findViewById(R.id.hunter_plus).setOnClickListener(this);
        findViewById(R.id.hangedman_minus).setOnClickListener(this);
        findViewById(R.id.hangedman_plus).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);

        if (savedInstanceState == null) {
            werewolf.setText("1");
            bigwolf.setText("0");
            madman.setText("0");
            fortuneteller.setText("0");
            thief.setText("0");
            hunter.setText("0");
            hangedman.setText("0");
        } else {
            werewolf.setText(savedInstanceState.getString("werewolf"));
            bigwolf.setText(savedInstanceState.getString("bigwolf"));
            madman.setText(savedInstanceState.getString("madman"));
            fortuneteller.setText(savedInstanceState.getString("fortuneteller"));
            thief.setText(savedInstanceState.getString("thief"));
            hunter.setText(savedInstanceState.getString("hunter"));
            hangedman.setText(savedInstanceState.getString("hangedman"));
        }
        setVillager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("werewolf", werewolf.getText().toString());
        outState.putString("bigwolf", bigwolf.getText().toString());
        outState.putString("madman", madman.getText().toString());
        outState.putString("fortuneteller", fortuneteller.getText().toString());
        outState.putString("thief", thief.getText().toString());
        outState.putString("hunter", hunter.getText().toString());
        outState.putString("hangedman", hangedman.getText().toString());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.werewolf_plus:
                plusButton(werewolf, maxWerewolf(3));
                break;
            case R.id.werewolf_minus:
                minusButton(werewolf, minWerewolf());
                break;
            case R.id.bigwolf_plus:
                plusButton(bigwolf, maxWerewolf(3));
                break;
            case R.id.bigwolf_minus:
                minusButton(bigwolf, minWerewolf());
                break;
            case R.id.madman_plus:
                plusButton(madman, maxWerewolf(3));
                break;
            case R.id.madman_minus:
                minusButton(madman, 0);
                break;
            case R.id.fortuneteller_plus:
                plusButton(fortuneteller, 3);
                break;
            case R.id.fortuneteller_minus:
                minusButton(fortuneteller, 0);
                break;
            case R.id.thief_plus:
                plusButton(thief, 1);
                break;
            case R.id.thief_minus:
                minusButton(thief, 0);
                break;
            case R.id.hunter_plus:
                plusButton(hunter, 1);
                break;
            case R.id.hunter_minus:
                minusButton(hunter, 0);
                break;
            case R.id.hangedman_plus:
                plusButton(hangedman, 1);
                break;
            case R.id.hangedman_minus:
                minusButton(hangedman, 0);
                break;
            case R.id.next:
                ArrayList<String> roles = getRolesArrayList();
                Intent intent = new Intent(this, StandBy.class);
                intent.putStringArrayListExtra("players", players);
                intent.putStringArrayListExtra("roles", roles);
                startActivity(intent);
                break;
        }
    }

    private void setVillager() {
        villager.setText(String.valueOf(players.size() + 2 -
                (Integer.parseInt(werewolf.getText().toString()) +
                        Integer.parseInt(bigwolf.getText().toString()) +
                        Integer.parseInt(madman.getText().toString()) +
                        Integer.parseInt(fortuneteller.getText().toString()) +
                        Integer.parseInt(thief.getText().toString()) +
                        Integer.parseInt(hunter.getText().toString()) +
                        Integer.parseInt(hangedman.getText().toString()))));
    }

    private int minWerewolf() {
        if (Integer.parseInt(werewolf.getText().toString()) + Integer.parseInt(bigwolf.getText().toString()) == 1) {
            return 1;
        } else {
            return 0;
        }
    }

    private int maxWerewolf(int max) {
        if (Integer.parseInt(werewolf.getText().toString()) +
                Integer.parseInt(bigwolf.getText().toString()) +
                Integer.parseInt(madman.getText().toString()) == players.size() + 1) {
            return 0;
        } else {
            return max;
        }
    }

    private void plusButton(TextView textView, int max) {
        if (Integer.parseInt(textView.getText().toString()) < max) {
            if (Integer.parseInt(villager.getText().toString()) > 0) {
                textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) + 1));
                setVillager();
            }
        }
    }

    private void minusButton(TextView textView, int min) {
        if (Integer.parseInt(textView.getText().toString()) > min) {
            textView.setText(String.valueOf(Integer.parseInt(textView.getText().toString()) - 1));
            setVillager();
        }
    }

    private ArrayList<String> getRolesArrayList() {
        ArrayList<String> roles = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(werewolf.getText().toString()); i++) {
            roles.add("werewolf");
        }
        for (int i = 0; i < Integer.parseInt(bigwolf.getText().toString()); i++) {
            roles.add("bigwolf");
        }
        for (int i = 0; i < Integer.parseInt(madman.getText().toString()); i++) {
            roles.add("madman");
        }
        for (int i = 0; i < Integer.parseInt(fortuneteller.getText().toString()); i++) {
            roles.add("fortuneteller");
        }
        for (int i = 0; i < Integer.parseInt(thief.getText().toString()); i++) {
            roles.add("thief");
        }
        for (int i = 0; i < Integer.parseInt(hunter.getText().toString()); i++) {
            roles.add("hunter");
        }
        for (int i = 0; i < Integer.parseInt(hangedman.getText().toString()); i++) {
            roles.add("hangedman");
        }
        for (int i = 0; i < Integer.parseInt(villager.getText().toString()); i++) {
            roles.add("villager");
        }
        return roles;
    }
}
