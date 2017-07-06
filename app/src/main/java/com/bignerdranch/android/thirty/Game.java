package com.bignerdranch.android.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Game implements Parcelable {
    private ArrayList<Player> players;
    private ArrayList<Die> dice;
    private static int cursor = 0;
    private HashMap<String, Integer> categories;

    public Game() {
        players = new ArrayList<>();
        categories = new HashMap<>();
        dice = new ArrayList<>();
        initializeCategories();
    }

    private Game(Parcel in) {
        cursor = in.readInt();
        in.readTypedList(players, null);
        in.readMap(categories, null);
    }

    private void initializeCategories() {
        categories.put("LOW", 3);

        for (Integer i = 4; i <= 12; i++) {
            categories.put(i.toString(), i);
        }
    }

    public ArrayList<Die> populateDiceList(int numberOfDice) {
        if (dice.isEmpty()) {

            for (int i = 0; i < numberOfDice; i++) {
                dice.add(new Die());
            }
        }
        return dice.isEmpty()? null : dice;
    }

    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    public Player getActivePlayer() {
        return (players.isEmpty()) ? null : players.get(cursor);
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public boolean anyDiceSelected() {
        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                return true;
            }
        }
        return false;
    }

    public void moveCursor() {
        if (cursor < players.size()) {
            cursor++;
        } else {
            cursor = 0;
        }
    }

    public int sumActiveDice() {
        int sum = 0;

        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                sum += die.getValue();
            }
        }

        return sum;
    }

    public boolean isSumLegal(int sum, String choice) {
        Integer value = categories.get(choice);

        if (value == null) {
            return false;
        }

        if (value < 4) {
            return sum < 4;
        } else {
            return value != 0 && sum == value;
        }
    }

    public boolean addPoints(String category, Integer sum) {
        Player activePlayer = getActivePlayer();

        if (activePlayer.submissionStarted()) {
            return activePlayer.score(category, sum);

        } else {

            if (activePlayer.categoryOpen(category)) {
                activePlayer.startSubmission(category);
                return activePlayer.score(category, sum);
            } else {
                return false;
            }
        }
    }

    public boolean endSubmission(String category) {
        Player activePlayer = getActivePlayer();

        if (activePlayer.getCurrentSubmissionCategory().equals(category)) {
            activePlayer.endSubmission();
            return true;

        } else {
            return false;
        }
    }

    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(cursor);
        parcel.writeTypedList(players);
        parcel.writeMap(categories);
    }

    public class Player implements Parcelable {
        private String name;
        private HashMap<String, Integer> combinations;
        private final int MAXIMUM_THROWS = 2;
        private int throwCounter;
        private boolean ongoingSubmission;
        private String currentSubmissionCategory;


        public Player(String name) {
            this.name = name;
            combinations = new HashMap<>();
            initilalizeScoreBoard();
            throwCounter = 0;
            ongoingSubmission = false;
            currentSubmissionCategory = "";
        }

        public boolean playerCanThrow() {
            return throwCounter < MAXIMUM_THROWS;
        }

        public void increaseThrowCounter() {
            throwCounter++;
        }

        public void clearThrowCounter() {
            throwCounter = 0;
        }

        public void initilalizeScoreBoard() {
            combinations.put("LOW", null);
            String choice;

            for (int i = 4; i <= 12; i++) {
                choice = "" + i;
                combinations.put(choice, null);
            }
        }

        public void startSubmission(String category) {
            currentSubmissionCategory = category;
            ongoingSubmission = true;
        }

        public boolean submissionStarted() {
            return ongoingSubmission;
        }

        public String getCurrentSubmissionCategory() {
            return currentSubmissionCategory;
        }
        public void endSubmission() {
            currentSubmissionCategory = "";
            ongoingSubmission = false;
        }

        public HashMap<String, Integer> getScores() {
            return combinations;
        }

        public boolean score(String choice, int value) {
            if (choice.equals(currentSubmissionCategory)) {

                Integer currentValue = combinations.get(choice);

                if (currentValue == null) {
                    combinations.put(choice, value);

                } else {
                    combinations.put(choice, currentValue + value);
                }

                return true;

            } else {
                return false;
            }
        }

        public boolean categoryOpen(String category) {
            return combinations.get(category) == null;
        }

        private Player(Parcel in) {
            in.readMap(combinations, null);
            name = in.readString();
            throwCounter = in.readInt();
            ongoingSubmission = in.readInt() == 1;
            currentSubmissionCategory = in.readString();
        }

        public final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
            @Override
            public Player createFromParcel(Parcel source) {
                return new Player(source);
            }

            @Override
            public Player[] newArray(int size) {
                return new Player[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(throwCounter);
            parcel.writeMap(combinations);
            parcel.writeString(name);
            parcel.writeInt(ongoingSubmission ? 1 : 0);
            parcel.writeString(currentSubmissionCategory);
        }
    }
}
