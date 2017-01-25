package yudai.werewolfdev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class NumberOfPlayers extends AppCompatActivity implements View.OnClickListener {

    private TextView numberOfPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_players);

        numberOfPlayers = (TextView) findViewById(R.id.numberOfPlayers);

        findViewById(R.id.minus).setOnClickListener(this);
        findViewById(R.id.plus).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);

        if (savedInstanceState != null) {
            numberOfPlayers.setText(savedInstanceState.getString("numberOfPlayers"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("numberOfPlayers", numberOfPlayers.getText().toString());
    }

    @Override
    public void onClick(View view) {
        int minPlayers = 3, maxPlayers = 10;

        switch (view.getId()) {
            case R.id.minus:
                minusButton(minPlayers);
                break;
            case R.id.plus:
                plusButton(maxPlayers);
                break;
            case R.id.next:
                Intent intent = new Intent(this, PlayerNames.class);
                intent.putExtra("numberOfPlayers", Integer.parseInt(numberOfPlayers.getText().toString()));
                startActivity(intent);
                break;
        }
    }

    private void minusButton(int min) {
        if (min < Integer.parseInt(numberOfPlayers.getText().toString())) {
            numberOfPlayers.setText(String.valueOf(Integer.parseInt(numberOfPlayers.getText().toString()) - 1));
        }
    }

    private void plusButton(int max) {
        if (Integer.parseInt(numberOfPlayers.getText().toString()) < max) {
            numberOfPlayers.setText(String.valueOf(Integer.parseInt(numberOfPlayers.getText().toString()) + 1));
        }
    }
}
