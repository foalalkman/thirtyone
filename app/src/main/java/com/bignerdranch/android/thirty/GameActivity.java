package com.bignerdranch.android.thirty;

/**
 * Author: Annika Svedin
 * email: annika.svedin@gmail.com
 * */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This activity class starts the GameActivity
 * and was created to make it possible to restart the game.
 */
public class GameActivity extends AppCompatActivity {

    private Game game;
    private Game.Player activePlayer;
    private ArrayList<Die> dieList;
    private DieAdapter adapter;
    private boolean menuItemLocked = false;
    private boolean diceSubmitted;
    private String spinnerSelectedItem;
    private ArrayList<String> spinnerItemsList;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private Button rollButton;
    private Button doneButton;
    private int scoreBoardResult = 0;

    private static final String EXTRA_SCORE_BOARD =
            "com.bignerdranch.android.thirty.score_board";

    private final String GAME_KEY = "Game";
    private final String DIE_LIST_KEY = "Dice";
    private final String SELECTED_LIST_ITEM_KEY = "Selected item";
    private final String MENU_ITEM_LOCKED = "Menu item locked";
    private final String SELECTED_SPINNER_ITEMS = "Spinner selected items";
    private final String DICE_SUBMITTED = "Dice submitted";

    public static final int NAVIGATE_UP_CODE = 1;
    public static final int RESTART_CODE = 0;

    /**
     * Saves the data in a bundle if the class is being destroyed for some reason.
     * @param savedInstanceState a bundle for storing state.
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(DIE_LIST_KEY, dieList);
        savedInstanceState.putParcelable(GAME_KEY, game);
        savedInstanceState.putString(SELECTED_LIST_ITEM_KEY, spinnerSelectedItem);
        savedInstanceState.putInt(MENU_ITEM_LOCKED, (menuItemLocked ? 1 : 0));
        savedInstanceState.putStringArrayList(SELECTED_SPINNER_ITEMS, spinnerItemsList);
        savedInstanceState.putInt(DICE_SUBMITTED, (diceSubmitted ? 1 : 0));
    }

    /**
     * Sets up a new instance of Game, adding one player to it.
     */
    private void createGameInstance() {
        game = new Game();
        game.addPlayer("Player 1");
    }

    /**
     * Fetches the array containing the categories and places them in a new ArrayList.
     */
    private void createMenuItemsList() {
        spinnerItemsList = new ArrayList<>();
        String[] menuItems = this.getResources().getStringArray(R.array.menu);

        for (int i = 0; i < menuItems.length; i++) {
            spinnerItemsList.add(menuItems[i]);
        }
    }

    /**
     * Removes a used category from the list of categories to be displayed in the Spinner.
     */
    private void removeSelectedSpinnerItem() {
        if (spinnerItemsList.contains(spinnerSelectedItem)) {
            spinnerItemsList.remove(spinnerSelectedItem);
        }
    }

    /**
     * Assigns a new ArrayAdapter that holds the list of categories to be displayed in the Spinner.
     * Sets the adapter on the Spinner.
     */
    private void setSpinnerAdapter() {
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItemsList.toArray());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    /**
     * Enables the rollButton if player can roll, otherwise disables it.
     */
    private void updateRollButton() {
        rollButton.setEnabled(game.getActivePlayer().playerCanThrow());
    }

    /**
     * Enables/diables the done button depending
     * on whether dice has been submitted or not.
     */
    private void updateDoneButton() {
        doneButton.setEnabled(diceSubmitted);
    }

    /**
     * Gets the stored data from the bundle and assigns it to the instance variables.
     * @param savedInstanceState a bundle with previously stored data.
     */
    private void restoreData(Bundle savedInstanceState) {
        dieList = savedInstanceState.getParcelableArrayList(DIE_LIST_KEY);
        game = savedInstanceState.getParcelable(GAME_KEY);
        spinnerSelectedItem = savedInstanceState.getString(SELECTED_LIST_ITEM_KEY);
        menuItemLocked = savedInstanceState.getInt(MENU_ITEM_LOCKED) != 0;
        spinnerItemsList = savedInstanceState.getStringArrayList(SELECTED_SPINNER_ITEMS);
        diceSubmitted = savedInstanceState.getInt(DICE_SUBMITTED) != 0;
    }

    /**
     * Creates a button and sets an OnClickListener to it.
     * When clicked the submission ends and a new round starts.
     */
    private void createDoneButton() {
        doneButton = (Button) findViewById(R.id.button_done);
        updateDoneButton();

        doneButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Tells the game that the submission is over and can be closed.
             * Clear the activePlayers rollCounter, store the used category in a collection for
             * used categories. Unlocks the spinner, let the adapter roll and reset the dice.
             * @param view the view being clicked on.
             */
            @Override
            public void onClick(View view) {
                if (spinnerSelectedItem != null && diceSubmitted) {
                    game.endSubmission(spinnerSelectedItem);
                    startNextRound();
                } else {
                    Toast.makeText(GameActivity.this, "Choose category and add points", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Makes the game board ready for the next player by resetting the data.
     */
    private void startNextRound() {
        activePlayer = game.getActivePlayer();
        activePlayer.clearRollCounter();
        removeSelectedSpinnerItem();
        setSpinnerAdapter();
        spinnerSelectedItem = null;
        menuItemLocked = false;
        adapter.updateDiceView();
        spinner.setSelection(0);
        updateRollButton();
        diceSubmitted = false;
        updateDoneButton();

        if (spinnerItemsList.isEmpty()) {
            launchResultActivity();
        }
    }

    /**
     * Creates a button and sets an OnClickListener to it.
     * If at least one Die is selected and the player is allowed to
     * roll, the selected Dice will roll.
     */
    private void createRollButton() {
        rollButton = (Button) findViewById(R.id.roll_button);
        rollButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Rolls the selected dice if the player has rolls left in the current round.
             * @param view the view being clicked on.
             */
            @Override
            public void onClick(View view) {
                activePlayer = game.getActivePlayer();

                if ((activePlayer != null) && activePlayer.playerCanThrow()) {
                    if (game.anyDiceSelected()) {
                        adapter.rollDice();
                        activePlayer.increaseRollCounter();
                        updateRollButton();

                    } else {
                        Toast.makeText(GameActivity.this, "Choose dice to throw", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(GameActivity.this, "No rolls left", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Sets up a RecyclerView, with an adapter holding a list of Die objects,
     * for the visual representation of the dice.
     */
    private void createDiceBoard() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.die_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new DieAdapter(dieList, this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Creates a submit button and sets a listener to it.
     * When clicked, if the sum of the dice match the chosen category,
     * they will be submitted to the players score board.
     */
    private void createSubmitButton() {
        Button submitButton = (Button) findViewById(R.id.button_submit_dice);
        submitButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Checks if the player is submitting a correct value of dice according to the category,
             * and submits points to the players score board.
             * Asks the adapter to set the state of the used dice to Disabled, indicating the dice
             * can't be used again.
             *
             * Locks the category so that the player is unable to change category within the same round.
             *
             * @param view the view being clicked on.
             */
            @Override
            public void onClick(View view) {
                if (spinnerSelectedItem != null) {

                    if (adapter.getTotalValue() > 0) {
                        int sum = game.sumActiveDice();

                        if (game.isSumLegal(sum, spinnerSelectedItem)) {
                            game.submit(spinnerSelectedItem, sum);
                            postSubmissionCleanUp();

                        } else {
                            Toast.makeText(GameActivity.this, "Sum doesn't match the category", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(GameActivity.this, "Select dice", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GameActivity.this, "Select a category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Sets the selected dice to disabled, indicating they've been used.
     * Locks the category, updates the roll button.
     * Sets the boolean diceSubmitted to true, which enables the Done button.
     */
    private void postSubmissionCleanUp() {
        adapter.disableActiveDice();
        menuItemLocked = true;
        updateRollButton();
        diceSubmitted = true;
        updateDoneButton();
    }

    /**
     * Creates the Spinner holding the available categories.
     */
    private void createSpinner() {
        spinner = (Spinner) findViewById(R.id.spinner_menu);
        setSpinnerAdapter();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Invoked when an item is selected in the spinner. Places the item in 'spinnerSelectedItem'
             * if the item is not used already, or if the player hasn't started on another category.
             * @param parentView the spinner.
             * @param selectedItemView the view representing the item.
             * @param position the position of the chosen item.
             * @param id the row id of the item.
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {

                String item = spinner.getSelectedItem().toString();
                setSpinnerSelectedItem(item);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    /**
     * Checks whether its allowed to pick a new category,
     * and sets the new category as the current one.
     * @param item A String representing the selected item.
     */
    private void setSpinnerSelectedItem(String item) {
        if (menuItemLocked) {
            if (spinnerSelectedItem != null && !spinnerSelectedItem.equals(item)) {
                Toast.makeText(GameActivity.this, "You already started with category " + spinnerSelectedItem, Toast.LENGTH_SHORT).show();
            }
        } else {
            spinnerSelectedItem = item;
        }
    }

    /**
     * The resultButton navigates us to the result view.
     */
    private void createResultButton() {
        Button resultButton = (Button) findViewById(R.id.button_result_view);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchResultActivity();
            }
        });
    }

    /**
     * Starts a new activity showing the current score board.
     */
    private void launchResultActivity() {
        Intent resultIntent = new Intent(GameActivity.this, ResultActivity.class);
        HashMap<String, Integer> scores = game.getActivePlayer().getScores();
        resultIntent.putExtra(EXTRA_SCORE_BOARD, scores);
        startActivityForResult(resultIntent, scoreBoardResult);
    }

    /**
     * If the result coming back with the Intent is equal to RESTART_CODE,
     * call finish() on this activity.
     * @param requestCode a key for specifying the requested result.
     * @param resultCode the result attached to the key.
     * @param data the Intent that was sent with startActivityForResult();
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == scoreBoardResult) {
            if (resultCode == RESTART_CODE) {
                finish();
            }
        }
    }

    /**
     * Calling methods for creating widgets.
     */
    private void createWidgets() {
        createDiceBoard();
        createRollButton();
        createSubmitButton();
        createDoneButton();
        createSpinner();
        createResultButton();
    }

    /**
     * The first thing that happens when the activity is launched. Initializes the widgets and the
     * state of the activity.
     * @param savedInstanceState a bundle storing the state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (savedInstanceState != null) {
            restoreData(savedInstanceState);

        } else {
            createGameInstance();
            dieList = game.populateDiceList(6);
            createMenuItemsList();
        }

        createWidgets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    /**
     * Called when the Action Bar was clicked.
     * @param item The menu item being clicked on.
     * @return true if the item id corresponded to a given id and fired
     * an action.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.restart_game) {
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
