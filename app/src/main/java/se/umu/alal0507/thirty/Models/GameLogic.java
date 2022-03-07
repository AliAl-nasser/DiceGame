package se.umu.alal0507.thirty.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class GameLogic implements Parcelable {

    private static final int LOW = 3;
    private static final int N_DICES = 6;
    public static final int MAX_ROUNDS = 10;
    public int nRounds = 1;
    public int nRolls = 2;
    private ArrayList<Boolean> isMarked = new ArrayList<>(Arrays.asList(new Boolean[N_DICES]));
    private ArrayList<Integer> frequency;
    private int points;
    private ArrayList<Integer> randomValues;
    private int randomValue;

    public GameLogic(){
        Collections.fill(isMarked, Boolean.FALSE);
    }

    protected GameLogic(Parcel in) {
        nRounds = in.readInt();
        nRolls = in.readInt();
        points = in.readInt();
        randomValue = in.readInt();
        isMarked = (ArrayList<Boolean>) in.readSerializable();
        randomValues = (ArrayList<Integer>) in.readSerializable();

    }

    public static final Creator<GameLogic> CREATOR = new Creator<GameLogic>() {
        @Override
        public GameLogic createFromParcel(Parcel in) {
            return new GameLogic(in);
        }

        @Override
        public GameLogic[] newArray(int size) {
            return new GameLogic[size];
        }
    };

    public String getNumberOfRolls(){
        return String.valueOf(nRolls);
    }

    public void setNumberOfRolls(int value){
        nRolls = value;
    }


    public void setFalse(){
        Collections.fill(isMarked, Boolean.FALSE);
    }

    public boolean getIsMarked(int i){
        return isMarked.get(i);
    }

    public void addRound(){
        nRounds++;
    }

    public void setIsMarked(int i, boolean selected){
        isMarked.set(i, selected);
    }

    public ArrayList<Integer> generateRandomValues(){
        randomValues = new ArrayList<>();
        for(int i=0; i < N_DICES; i++) {
            randomValue = new Random().nextInt((N_DICES - 1) + 1) + 1;
            randomValues.add(randomValue);
        }
        return randomValues;
    }

    public int setRandomValues(int i){
        randomValue = new Random().nextInt((N_DICES - 1) + 1) + 1;
        randomValues.set(i, randomValue);
        return randomValue;
    }

    public int getRandomValues(int index){
        return randomValues.get(index);
    }

    //calculates the max points for choice
    public int calculatePoints(String selectedChoice) {
        points = 0;
        if(!selectedChoice.equals("Pick a Score")){
            int choice;
            if(selectedChoice.equals("Low")){
                choice = LOW;
                for (int i = 0; i < N_DICES; i++) {
                    if (randomValues.get(i) <= choice) {
                        points += randomValues.get(i);
                    }
                }
            }
            else{
                choice = Integer.parseInt(selectedChoice);
                Vector<Integer> A = new Vector<>(randomValues);
                frequency = new ArrayList<>();
                for (int i = 1; i <= N_DICES;  i++){
                    frequency.add(Collections.frequency(randomValues, i));
                }
                Combination(A, choice);
            }
        }
        return points;
    }

    //sorts dice values in descending order, creates new local vector to store combinations and calls combinationUtil
    private void Combination(Vector<Integer> A, int K) {
        Collections.sort(A, Collections.reverseOrder());
        Vector<Integer> local = new Vector<>();
        combinationUtil(0, 0, K, local, A);
    }

    //generates the combinations and checks if sum is equal to choice, then add points to the total score
    private void combinationUtil(int l, int sum, int K, Vector<Integer> local, Vector<Integer> A) {
        // If combination is found
        if (sum == K) {
            for (int i = 0; i < local.size(); i++) {
                points += local.get(i);
                int value = frequency.get(local.get(i)-1);
                value -= 1;
                frequency.set(local.get(i)-1, value);
            }
            return;
        }
        // For all other combinations
        for (int i = l; i < A.size(); i++) {
            // Check if the sum exceeds K
            if (sum + A.get(i) > K) continue;
            // Take the element into the combination
            local.add(A.get(i));
            // Check if value already used
            Boolean check = checkIfUsed(local);
            if(check){
                local.remove(A.get(i));
                continue;
            }
            // Recursive call
            combinationUtil(i+1, sum + A.get(i), K,
                    local, A);
            // Remove element from the combination
            local.remove(local.size() - 1);
        }
    }

    /*Function that checks if combination is already used (frequency==0) or has frequency
    that exceed the frequency for the dice values*/
    private Boolean checkIfUsed(Vector<Integer> local){
        boolean check = false;
        for(int i=1; i <= N_DICES; i++){
            if(Collections.frequency(local, i) > frequency.get(i-1)){
                check = true;
            }
        }
        for(int j = 0; j < local.size(); j++){
            if (frequency.get(local.get(j) - 1) == 0){
                check = true;
            }
        }
        return check;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(nRounds);
        parcel.writeInt(nRolls);
        parcel.writeInt(points);
        parcel.writeInt(randomValue);
        parcel.writeSerializable(isMarked);
        parcel.writeSerializable(randomValues);
    }
}
