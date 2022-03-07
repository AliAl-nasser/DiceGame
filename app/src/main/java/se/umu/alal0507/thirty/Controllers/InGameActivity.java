package se.umu.alal0507.thirty.Controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;

import se.umu.alal0507.thirty.Models.GameLogic;
import se.umu.alal0507.thirty.R;

public class InGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String STATE_GAME = "STATE_GAME";
    private static final String STATE_POINTS = "STATE_POINTS";
    private static final String STATE_CHOICE = "STATE_CHOICE";
    private static final String STATE_VALUES = "STATE_VALUES";
    private GameLogic gameLogic;

    private ArrayList<String> selectedChoices;
    private ArrayList<Integer> listOfPoints;
    private TextView numberOfRolls;
    private int points;
    private TextView Points;
    private String selectedChoice;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private ArrayList<ImageView> Dices;
    private ArrayList<Integer> randomValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingame);
        if (savedInstanceState != null) {
            this.gameLogic = savedInstanceState.getParcelable(STATE_GAME);
            this.listOfPoints = savedInstanceState.getIntegerArrayList(STATE_POINTS);
            this.selectedChoices = savedInstanceState.getStringArrayList(STATE_CHOICE);
            this.randomValues = savedInstanceState.getIntegerArrayList(STATE_VALUES);
        }
        else {
            gameLogic = new GameLogic();
            selectedChoices = new ArrayList<>();
            listOfPoints = new ArrayList<>();
            randomValues = new ArrayList<>();
        }
        setSpinnerListener(savedInstanceState);
        setRollButton();
        setCollectPointsButton();
        setDices();
        generateDices(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelable(STATE_GAME, gameLogic);
        savedInstanceState.putSerializable(STATE_POINTS, listOfPoints);
        savedInstanceState.putSerializable(STATE_CHOICE, selectedChoices);
        savedInstanceState.putSerializable(STATE_VALUES, randomValues);
        super.onSaveInstanceState(savedInstanceState);
    }

    //Initializing spinner and on item selected listener
    private void setSpinnerListener(Bundle check) {
        spinner = findViewById(R.id.spinner1);
        String[] choices = getResources().getStringArray(R.array.points);
        if(check != null){
            for(int i=0; i < selectedChoices.size(); i++){
                choices = ArrayUtils.removeElement(choices, selectedChoices.get(i));
            }
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>(Arrays.asList(choices)));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    //Initializing roll button and on click listener
    private void setRollButton() {
        Button roll = findViewById(R.id.rollDices);
        roll.setOnClickListener(this);
        numberOfRolls = findViewById(R.id.numberOfRolls);
        numberOfRolls.setText(gameLogic.getNumberOfRolls());
        TextView numberOfRounds = findViewById(R.id.rounds);
        numberOfRounds.setText(String.format("Rounds: %s", gameLogic.nRounds));

    }

    //Initializing collect points button and on click listener
    private void setCollectPointsButton() {
        Button collectPoints = findViewById(R.id.collectPoints);
        collectPoints.setOnClickListener(this);
    }

    //Initializing Dice and on click listener
    private void setDices(){
        Dices = new ArrayList<>();
        for(int i= 1; i <= 6; i++){
            String ID = "dice" + (i);
            int ResID = getResources().getIdentifier(ID, "id", getPackageName());
            Dices.add(findViewById(ResID));
            Dices.get(i-1).setOnClickListener(this);
        }
    }

    //called when a item is selected from the spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selectedChoice = adapterView.getItemAtPosition(i).toString();
        points = gameLogic.calculatePoints(selectedChoice);
        Points = findViewById(R.id.points);
        if(!selectedChoice.equals("Pick a Score")){
            Points.setText(String.format("Points: %s", points));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }

    //On click function that performs a action depending on which button or dice was clicked in the view
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rollDices:
                switch (gameLogic.getNumberOfRolls()) {
                    case "2":
                        gameLogic.setNumberOfRolls(1);
                        numberOfRolls.setText("1");
                        Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT).show();
                        rollDices();
                        spinner.setSelection(0);
                        Points.setText(R.string.points);
                        break;
                    case "1":
                        gameLogic.setNumberOfRolls(0);
                        numberOfRolls.setText("0");
                        rollDices();
                        spinner.setSelection(0);
                        Points.setText(R.string.points);
                        Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT).show();
                        break;
                    case "0":
                        Toast.makeText(this, "Out of re-rolls!", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.collectPoints:
                if(!selectedChoice.equals("Pick a Score")) {
                    if (gameLogic.nRounds == GameLogic.MAX_ROUNDS) {
                        listOfPoints.add(points);
                        selectedChoices.add(selectedChoice);
                        nextActivity();
                    }
                    else {
                        gameLogic.addRound();
                        adapter.remove(selectedChoice);
                        adapter.notifyDataSetChanged();
                        spinner.setSelection(0);
                        listOfPoints.add(points);
                        selectedChoices.add(selectedChoice);
                        Points.setText(R.string.points);
                        gameLogic.setNumberOfRolls(2);
                        numberOfRolls.setText(R.string.numberOfRolls);
                        gameLogic.setFalse();
                        generateDices(null);
                        TextView numberOfRounds = findViewById(R.id.rounds);
                        numberOfRounds.setText(String.format("Rounds: %s", gameLogic.nRounds));
                    }
                }
                else{
                    Toast.makeText(this, "You must pick a score!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dice1:
                changeDice(0);
                break;
            case R.id.dice2:
                changeDice(1);
                break;
            case R.id.dice3:
                changeDice(2);
                break;
            case R.id.dice4:
                changeDice(3);
                break;
            case R.id.dice5:
                changeDice(4);
                break;
            case R.id.dice6:
                changeDice(5);
                break;
            default:
                break;
        }
    }

    //called when dice are rolled for the first time or each round
    private void generateDices(Bundle check) {
        if(check == null) randomValues = gameLogic.generateRandomValues();
        for(int i=0; i < randomValues.size(); i++) {
            String ID;
            if(gameLogic.getIsMarked(i) == Boolean.TRUE) ID = "purple" + randomValues.get(i);
            else ID = "white" + randomValues.get(i);
            int ResID = getResources().getIdentifier(ID, "drawable", getPackageName());
            Dices.get(i).setImageResource(ResID);
        }
    }

    //called when dice are rolled
    private void rollDices() {
        for (int i = 0; i < randomValues.size(); i++){
            if  (gameLogic.getIsMarked(i) == Boolean.TRUE) continue;
            else if (gameLogic.getIsMarked(i) == Boolean.FALSE) {
                String ID = "white" + gameLogic.setRandomValues(i);
                int ResID = getResources().getIdentifier(ID, "drawable", getPackageName());
                Dices.get(i).setImageResource(ResID);
            }
        }
    }

    //called when a die is clicked
    private void changeDice(int index) {
        String ID1 = "purple" + gameLogic.getRandomValues(index);
        int ResID1 = getResources().getIdentifier(ID1, "drawable", getPackageName());
        String ID2 = "white" + gameLogic.getRandomValues(index);
        int ResID2 = getResources().getIdentifier(ID2, "drawable", getPackageName());
        if(gameLogic.getIsMarked(index) == Boolean.FALSE){
            Dices.get(index).setImageResource(ResID1);
            gameLogic.setIsMarked(index, Boolean.TRUE);
        }
        else if(gameLogic.getIsMarked(index) == Boolean.TRUE){
            Dices.get(index).setImageResource(ResID2);
            gameLogic.setIsMarked(index, Boolean.FALSE);
        }
    }

    //After 10 rounds this function is called to start the score Activity
    public void nextActivity() {
        Intent intent = new Intent(this, ScoreActivity.class);
        intent.putIntegerArrayListExtra("pointsList", listOfPoints);
        intent.putStringArrayListExtra("choiceList", selectedChoices);
        startActivity(intent);
        finish();
    }
}