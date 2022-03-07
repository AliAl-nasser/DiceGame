package se.umu.alal0507.thirty.Controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import se.umu.alal0507.thirty.R;

// Handles the score view and displays the results.
public class ScoreActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String STATE_CHOICE = "STATE_CHOICE";
    private static final String STATE_POINTS = "STATE_POINTS";
    private ArrayList<String> selectedChoices;
    private ArrayList<Integer> listOfPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            this.listOfPoints = savedInstanceState.getIntegerArrayList(STATE_POINTS);
            this.selectedChoices = savedInstanceState.getStringArrayList(STATE_CHOICE);
        }
        else {
            Intent i = getIntent();
            //Passed from InGameActivity
            listOfPoints = i.getIntegerArrayListExtra("pointsList");
            selectedChoices = i.getStringArrayListExtra("choiceList");
        }
        setContentView(R.layout.activity_score);
        Button newGame = findViewById(R.id.startNewGame);
        newGame.setOnClickListener(this);
        Button menu = findViewById(R.id.menu);
        menu.setOnClickListener(this);
        getScores();
    }
    //On click function that performs a action depending on which button was clicked in the view
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.startNewGame:
                startActivity(new Intent(this, InGameActivity.class));
                finish();
                break;
            case R.id.menu:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putSerializable(STATE_POINTS, listOfPoints);
        savedInstanceState.putSerializable(STATE_CHOICE, selectedChoices);
        super.onSaveInstanceState(savedInstanceState);
    }
    //Shows the choices made and the corresponding points in the table, also the the total points
    private void getScores() {
        int total = 0;
        for(int i = 0; i < selectedChoices.size(); i++){
            String textviewID_a = "row" + (i+1) + "_a";
            String textviewID_b = "row" + (i+1) + "_b";
            int resID_a = getResources().getIdentifier(textviewID_a, "id", getPackageName());
            int resID_b = getResources().getIdentifier(textviewID_b, "id", getPackageName());
            TextView a = findViewById(resID_a);
            TextView b = findViewById(resID_b);
            a.setText(selectedChoices.get(i));
            b.setText(String.valueOf(listOfPoints.get(i)));
            total += listOfPoints.get(i);
        }
        TextView totalScore = findViewById(R.id.totalScore);
        totalScore.setText(String.format("Total Score: %s", total));
    }
}