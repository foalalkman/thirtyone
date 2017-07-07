package com.bignerdranch.android.thirty;

/**
 * Author: Annika Svedin
 * email: annika.svedin@gmail.com
 * */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The game class holds the players, manage turn taking, rules and a shared set of Dice.
 */
class Game implements Parcelable {
    private ArrayList<Player> players;
    private ArrayList<Die> dice;
    private static int cursor = 0;
    private HashMap<String, Integer> categories;

    Game() {
        players = new ArrayList<>();
        categories = new HashMap<>();
        dice = new ArrayList<>();
        initializeCategories();
    }

    /**
     * A private constructor to be called from the CREATOR, that assigns the saved data to the
     * instance variables.
     * @param in the parcel holding the saved values.
     */
    private Game(Parcel in) {
        cursor = in.readInt();
        in.readTypedList(players, null);
        in.readMap(categories, null);
    }

    /**
     * Builds a hashmap with the names of the categories and the value they represent.
     * The map is used when verifying weather a submitted value corresponds to the chosen category.
     */
    private void initializeCategories() {
        categories.put("LOW", 3);

        for (Integer i = 4; i <= 12; i++) {
            categories.put(i.toString(), i);
        }
    }

    /**
     * Creates as many dice the game should have and store them in a list.
     * @param numberOfDice the number of dice the game should use.
     * @return the list holding the dice
     */
    ArrayList<Die> populateDiceList(int numberOfDice) {
        if (dice.isEmpty()) {

            for (int i = 0; i < numberOfDice; i++) {
                dice.add(new Die());
            }
        }
        return dice.isEmpty()? null : dice;
    }

    /**
     * Creates a new instance of the Player class. The game could be a multi player game in the future
     * @param name for future multi playing.
     */
    void addPlayer(String name) {
        players.add(new Player(name));
    }

    /**
     *
     * @return the player the cursor points at
     */
    Player getActivePlayer() {
        return (players.isEmpty()) ? null : players.get(cursor);
    }

    /**
     *
     * @return true if at least one of the dice is selected.
     */
    boolean anyDiceSelected() {
        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the cursor to the next player if there are more than one.
     */
    void moveCursor() {
        if (cursor < players.size()) {
            cursor++;
        } else {
            cursor = 0;
        }
    }

    /**
     *
     * @return the total value of active dice in the dice collection.
     */
    int sumActiveDice() {
        int sum = 0;

        for (Die die : dice) {
            if (die.getActiveState() == 1) {
                sum += die.getValue();
            }
        }

        return sum;
    }

    /**
     *
     * @param sum the sum that should correspond to a category.
     * @param choice representation of the category.
     * @return true if the total value of the dice being submitted to the score board
     * corresponds to the category in question
     */
    boolean isSumLegal(int sum, String choice) {
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

    /**
     *  Start to add the points to the category, if the category is free.
     *  The category will be set to 'open' so that the player can keep on scoring,
     *  until the 'Done' button is clocked.
     *
     * @param category the category of choice
     * @param sum the sum to attach to the category
     * @return true if adding points succeeded, otherwise false
     */
    boolean submit(String category, Integer sum) {
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

    /**
     * Cut off the possibility to score on a category
     *
     * @param category String representation of the category to 'close'.
     * @return true if there is an open submission to end, otherwise false
     */
    boolean endSubmission(String category) {
        Player activePlayer = getActivePlayer();

        if (activePlayer.getCurrentSubmissionCategory().equals(category)) {
            activePlayer.endSubmission();

            // Move the cursor here if number of players > 1

            return true;

        } else {
            return false;
        }
    }

    /**
     * For receiving the class loader in the Java Runtime Environment,
     * which can load a class dynamically during runtime. It will use the private constructor
     * that takes a Parcel instance, to set the saved state in the new instance.
     */
    public static final Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        /**
         *
         * @param source the Parcel object holding the old values.
         * @return a new instance of the class that mirrors the old one's state.
         */
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        /**
         * Makes it possible to create an array of Game.
         * @param size the desired size of the array.
         * @return the new Array.
         */
        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    /**
     *
     * @return additional information about the class, if any.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Saves the current state in the Parcel object.
     * @param parcel a Parcel object for storing data.
     * @param flags describes different ways to write to the parcel.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(cursor);
        parcel.writeTypedList(players);
        parcel.writeMap(categories);
    }

    /**
     * The Player class holds the players individual score board, keep track of the number of rolls
     * for each round. It makes it possible to add points to an unused category within the same round.
     */
    class Player implements Parcelable {
        private String name;
        private HashMap<String, Integer> scoreBoard;
        private final int MAXIMUM_THROWS = 2;
        private int rollCounter;
        private boolean ongoingSubmission;
        private String currentSubmissionCategory;

        Player(String name) {
            this.name = name;
            scoreBoard = new HashMap<>();
            initilalizeScoreBoard();
            rollCounter = 0;
            ongoingSubmission = false;
            currentSubmissionCategory = "";
        }

        /**
         * Increasing rollCounter for every time the player roll.
         */
        void increaseRollCounter() {
            rollCounter++;
        }

        /**
         * Stops the player to roll dice more than it's allowed.
         * @return true if the player rolled less times than the allowed amount.
         */
        boolean playerCanThrow() {
            return rollCounter < MAXIMUM_THROWS;
        }

        /**
         * Resets the throw counter when the current round is done.
         */
        void clearRollCounter() {
            rollCounter = 0;
        }

        /**
         * Setting up a score board as a hashmap,
         * with the categories as keys and the points as their values.
         */
        void initilalizeScoreBoard() {
            scoreBoard.put("LOW", null);
            String choice;

            for (int i = 4; i <= 12; i++) {
                choice = "" + i;
                scoreBoard.put(choice, null);
            }
        }

        /**
         * Sets the 'ongoingSubmission' to true and the 'currentSubmissionCategory' to the chosen
         * category. Now it's possible to add points to the score board, for that particular category.
         * @param category String representation of the chosen category.
         */
        void startSubmission(String category) {
            currentSubmissionCategory = category;
            ongoingSubmission = true;
        }

        /**
         *
         * @return true if 'ongoingSubmission' holds true the player can keep adding points.
         */
        boolean submissionStarted() {
            return ongoingSubmission;
        }

        /**
         *
         * @return the category which is 'open' for adding points to
         */
        String getCurrentSubmissionCategory() {
            return currentSubmissionCategory;
        }

        /**
         *      Resets the submission states, indicating that the player is done
         *      with the current category
         * */
        void endSubmission() {
            currentSubmissionCategory = "";
            ongoingSubmission = false;
        }

        /**
         *
         * @return the score board with the collected points
         */
        HashMap<String, Integer> getScores() {
            return scoreBoard;
        }

        /**
         * @param category the category to receive the points.
         * @param value the value representing the points to be added.
         * @return true if able to add the points to the score board,
         * false if the chosen category isn't the same as 'currentSubmissionCategory'
         */
        boolean score(String category, int value) {
            if (category.equals(currentSubmissionCategory)) {

                Integer currentValue = scoreBoard.get(category);

                if (currentValue == null) {
                    scoreBoard.put(category, value);

                } else {
                    scoreBoard.put(category, currentValue + value);
                }

                return true;

            } else {
                return false;
            }
        }

        /**
         *
         * @param category The category that can be open or not.
         * @return true if the category has no value, e.g it's free for scoring
         */
        boolean categoryOpen(String category) {
            return scoreBoard.get(category) == null;
        }

        /**
         * The private constructor used by the Creator, that assigns the saved values to the
         * new instance.
         * @param in the Parcel holding the old state.
         */
        private Player(Parcel in) {
            in.readMap(scoreBoard, null);
            name = in.readString();
            rollCounter = in.readInt();
            ongoingSubmission = in.readInt() == 1;
            currentSubmissionCategory = in.readString();
        }

        /**
         * For receiving the class loader in the Java Runtime Environment,
         * which can load a class dynamically during runtime. It will use the private constructor
         * that takes a Parcel instance.
         */
        public final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
            /**
             * When createFromParcel is called by the system, it takes the Parcel with all the
             * information and use the private constructor to make a new instance of Player that
             * mirrors the old one.
             * @param source the parcel storing the old state.
             * @return a new Player object similar to the old one.
             */
            @Override
            public Player createFromParcel(Parcel source) {
                return new Player(source);
            }

            /**
             * Allows an array of the class to be parcelled.
             * @param size the size of the array to be created.
             * @return An empty array with the specified length.
             */
            @Override
            public Player[] newArray(int size) {
                return new Player[size];
            }
        };

        /**
         * Makes it possible to pass more information about the class, in form of a bit mask.
         * @return a bitmask with information, 0 if none.
         */
        @Override
        public int describeContents() {
            return 0;
        }

        /**
         * This is where the Parcel object is filled with the values that needs to be saved.
         * @param parcel a Parcel object for storing data.
         * @param flags describes different ways to write to the parcel.
         */
        @Override
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeMap(scoreBoard);
            parcel.writeString(name);
            parcel.writeInt(rollCounter);
            parcel.writeInt(ongoingSubmission ? 1 : 0);
            parcel.writeString(currentSubmissionCategory);
        }
    }
}
