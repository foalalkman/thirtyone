package com.bignerdranch.android.thirty;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game implements Parcelable {
    private ArrayList<Player> players;
    private ArrayList<Die> dice;
    private static int cursor = 0;
    private final int MAXIMUM_THROWS = 2;
    private HashMap<String, Integer> categories;
    private int partialSum = 0;

    public Game() {
        players = new ArrayList<>();
        categories = new HashMap<>();
        dice = new ArrayList<>();
        initializeCategories();
    }

    private Game (Parcel in) {
        cursor = in.readInt();
        in.readTypedList(players, null);
        in.readMap(categories, null);
    }

    private void initializeCategories() {
        categories.put("Low", 3);

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
            if (die.getActiveState() == 1) {       //
                sum += die.getValue();
            }
        }

        return sum;
    }

    public boolean isSumLegal(int sum, String choice) {
        int value = categories.get(choice);

        if (value < 4) {
            return sum < 4;
        } else {
            return value != 0 && sum == value;
        }
    }

    public void addPartialSum(int sum) {
        partialSum += sum;
    }

    public void clearPartialSum() {
        partialSum = 0;
    }

    public boolean addPointsToPlayer(String category) {
        if (partialSum == 0) {
            return false;
        } else {
            return getActivePlayer().addValue(category, partialSum);
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
        private int throwCounter;

        public Player(String name) {
            this.name = name;
            combinations = new HashMap<>();
            initilalizeScoreBoard();
            throwCounter = 0;
        }

        public boolean playerCanThrow() {
            return throwCounter < MAXIMUM_THROWS;
        }

        public void increaseThrowCounter() {
            throwCounter++;
        }

        public void clearThrowCounter() {
            throwCounter = 0;
        }                   // funkar detta?

        public void initilalizeScoreBoard() { // initializeScoreBoard
            combinations.put("Low", null);
            String choice;

            for (int i = 4; i <= 12; i++) {
                choice = "" + i;
                combinations.put(choice, null);
            }
        }

        public HashMap<String, Integer> getScores() {
            return combinations;
        }


        public boolean addValue(String choice, int value) { // annat namn
            if (combinations.get(choice) == null) {
                combinations.put(choice, value);
                return true;
            } else {
                return false;
            }
        }

        private Player(Parcel in) {
            in.readMap(combinations, null);
            name = in.readString();
            throwCounter = in.readInt();
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
        }
    }
}
