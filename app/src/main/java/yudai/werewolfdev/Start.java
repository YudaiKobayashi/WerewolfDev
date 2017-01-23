package yudai.werewolfdev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Start extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        findViewById(R.id.newGame).setOnClickListener(this);
        findViewById(R.id.savedSession).setOnClickListener(this);
        findViewById(R.id.howToPlay).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.newGame:
                Intent intent = new Intent(this, NumberOfPlayers.class);
                startActivity(intent);
                break;
            case R.id.savedSession:
                break;
            case R.id.howToPlay:
                break;
        }
    }
}
