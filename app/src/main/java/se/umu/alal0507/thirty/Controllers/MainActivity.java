package se.umu.alal0507.thirty.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.umu.alal0507.thirty.R;

// Main activity that starts when the app runs, shows the rules and button that navigates to the game
//when clicked.
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button newGame = findViewById(R.id.newGame);
        newGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this, InGameActivity.class));
    }
}