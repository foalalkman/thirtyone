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

public class GameActivity extends AppCompatActivity {

    private Game game;
    private Game.Player activePlayer;
    private ArrayList<Die> dieList;
    private DieAdapter adapter;
    private boolean menuItemLocked;
    private boolean diceSubmitted;
    private String spinnerSelectedItem;
    private ArrayList<String> spinnerItemsList;
    private Spinner spinner;
    private ArrayAdapter<String> spinnerAdapter;
    private Button rollButton;

    private static final String EXTRA_SCORE_BOARD =
            "com.bignerdranch.android.thirty.score_board";

    private final String GAME_KEY = "Game";
    private final String DIE_LIST_KEY = "Dice";
    private final String SELECTED_LIST_ITEM_KEY = "Selected item";
    private final String MENU_ITEM_LOCKED = "Menu item locked";
    private final String SELECTED_SPINNER_ITEMS = "Spinner selected items";
    private final String DICE_SUBMITTED = "Dice submitted";

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

    private void createGameInstance() {
        game = new Game();
        game.addPlayer("Player 1");

        dieList = game.populateDiceList(6);
        menuItemLocked = false;
    }

    private void createMenuItemsList() {
        spinnerItemsList = new ArrayList<>();
        String[] menuItems = this.getResources().getStringArray(R.array.menu);

        for (int i = 0; i < menuItems.length; i++) {
            spinnerItemsList.add(menuItems[i]);
        }
    }

    private void removeSelectedSpinnerItem() {
        if (spinnerItemsList.contains(spinnerSelectedItem)) {
            spinnerItemsList.remove(spinnerSelectedItem);
        }
    }

    private void setSpinnerAdapter() {
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItemsList.toArray());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    private void updateRollButton() {
        if (game.getActivePlayer().playerCanThrow()) {
            rollButton.setEnabled(true);
        } else {
            rollButton.setEnabled(false);
        }
    }

    private void restoreData(Bundle savedInstanceState) {
        dieList = savedInstanceState.getParcelableArrayList(DIE_LIST_KEY);
        game = savedInstanceState.getParcelable(GAME_KEY);
        spinnerSelectedItem = savedInstanceState.getString(SELECTED_LIST_ITEM_KEY);
        menuItemLocked = savedInstanceState.getInt(MENU_ITEM_LOCKED) != 0;
        spinnerItemsList = savedInstanceState.getStringArrayList(SELECTED_SPINNER_ITEMS);
        diceSubmitted = savedInstanceState.getInt(DICE_SUBMITTED) != 0;
    }

    private void createDoneButton() {
        Button doneButton = (Button) findViewById(R.id.button_done);
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

                activePlayer = game.getActivePlayer();
                game.endSubmission(spinnerSelectedItem);
                activePlayer.clearRollCounter();

                removeSelectedSpinnerItem();
                setSpinnerAdapter();

                spinnerSelectedItem = null;
                menuItemLocked = false;

                adapter.updateDiceView();
                spinner.setSelection(0);

                updateRollButton();
                diceSubmitted = false;

                if (spinnerItemsList.isEmpty()) {
                    launchResultActivity();
                }

                /** switch to next player if multi player
                 activePlayer = game.getActivePlayer();  - get the new active player */

            } else {
                Toast.makeText(GameActivity.this, "Choose category and add points", Toast.LENGTH_SHORT).show();
            }
            }
        });

    }

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

            if ((activePlayer != null)) {

                if (activePlayer.playerCanThrow()) {

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
            } else {
                Toast.makeText(GameActivity.this, "Player missing", Toast.LENGTH_SHORT).show();
            }
            }
        });
    }

    private void createDiceBoard() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.die_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new DieAdapter(dieList, this);
        recyclerView.setAdapter(adapter);
    }

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
                            adapter.disableActiveDice();
                            menuItemLocked = true;
                            updateRollButton();
                            diceSubmitted = true;

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

                if (!item.equals("Select category:")) {

                    if (menuItemLocked) {
                        if (spinnerSelectedItem != null && !spinnerSelectedItem.equals(item)) {
                            Toast.makeText(GameActivity.this, "You already started with category " + spinnerSelectedItem, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        spinnerSelectedItem = item;

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void createResultButton() {
        /**
         * Navigates to the result view.
         */
        Button resultButton = (Button) findViewById(R.id.button_result_view);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchResultActivity();
            }
        });
    }

    private void launchResultActivity() {
        Intent resultIntent = new Intent(GameActivity.this, ResultActivity.class);

        HashMap<String, Integer> scores = game.getActivePlayer().getScores();
        resultIntent.putExtra(EXTRA_SCORE_BOARD, scores);
        startActivity(resultIntent);
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.restart_game) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
