package com.bignerdranch.android.thirty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity {

    private Game game;
    private Game.Player activePlayer;
    private Spinner spinner;
    private ArrayList<Die> dieList;
    private DieAdapter adapter;
    private boolean menuItemLocked;
    private String spinnerSelectedItem;
    private ArrayList<String> spinnerSelectedItemsList;

    private static final String EXTRA_SCORE_BOARD =
            "com.bignerdranch.android.thirty.score_board";

    private final String GAME_KEY = "Game";
    private final String DIE_LIST_KEY = "Dice";
    private final String PLAYER_KEY = "Player"; // ?
    private final String SELECTED_LIST_ITEM_KEY = "Selected item";
    private final String MENU_ITEM_LOCKED = "Menu item locked";
    private final String SELECTED_SPINNER_ITEMS = "Spinner selected items";



    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList(DIE_LIST_KEY, dieList);
        savedInstanceState.putParcelable(GAME_KEY, game);
        savedInstanceState.putString(SELECTED_LIST_ITEM_KEY, spinnerSelectedItem);
        savedInstanceState.putInt(MENU_ITEM_LOCKED, (menuItemLocked ? 1 : 0));
        savedInstanceState.putStringArrayList(SELECTED_SPINNER_ITEMS, spinnerSelectedItemsList);
        savedInstanceState.putParcelable(PLAYER_KEY, activePlayer); //?
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        if (savedInstanceState != null) {
            dieList = savedInstanceState.getParcelableArrayList(DIE_LIST_KEY);
            game = savedInstanceState.getParcelable(GAME_KEY);
            spinnerSelectedItem = savedInstanceState.getString(SELECTED_LIST_ITEM_KEY);
            activePlayer = savedInstanceState.getParcelable(PLAYER_KEY); // ?
            menuItemLocked = savedInstanceState.getInt(MENU_ITEM_LOCKED) != 0; // funkis?
            spinnerSelectedItemsList = savedInstanceState.getStringArrayList(SELECTED_SPINNER_ITEMS);
        } else {

            game = new Game();
            game.addPlayer("Player 1");

            dieList = game.populateDiceList(6);
            menuItemLocked = false;
            spinnerSelectedItemsList = new ArrayList<>();
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.die_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new DieAdapter(dieList, this);
        recyclerView.setAdapter(adapter);

        Button rollButton = (Button) findViewById(R.id.roll_button);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activePlayer = game.getActivePlayer();

                if (activePlayer.playerCanThrow()) {
                    if (game.anyDiceSelected()) {
                        adapter.rollDice();
                        activePlayer.increaseThrowCounter();
                    } else {
                        Toast.makeText(GameActivity.this, "Choose dice to throw", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(GameActivity.this, "Pick dice and commit", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button submitButton = (Button) findViewById(R.id.button_submit_dice);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spinnerSelectedItem != null) {

                    if (adapter.getTotalValue() > 0) {
                        int sum = game.sumActiveDice();

                        if (game.isSumLegal(sum, spinnerSelectedItem)) {
                            game.addPartialSum(sum);
                            adapter.disableActiveDice();
                            menuItemLocked = true;


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

        Button doneButton = (Button) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GameActivity.this, spinnerSelectedItem, Toast.LENGTH_SHORT).show();
                if (spinnerSelectedItem != null) {
                    if (game.addPointsToPlayer(spinnerSelectedItem)) {
                        game.clearPartialSum();
                        spinnerSelectedItemsList.add(spinnerSelectedItem);
                        spinnerSelectedItem = null;
                        menuItemLocked = false;
                        adapter.updateDiceView();


                        /** switch to next player if multi player
                            activePlayer = game.getActivePlayer();  - get the new active player */
                    }
                } else {
                    Toast.makeText(GameActivity.this, "Choose category and add points", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner_menu);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedItemView, int position, long id) {

                String item = spinner.getSelectedItem().toString();

                if (menuItemLocked) {
                    Toast.makeText(GameActivity.this, "You already started on another category", Toast.LENGTH_SHORT).show();
                }

                if (verifyCategory(item)) {
                    Toast.makeText(GameActivity.this, "You are done with this category", Toast.LENGTH_SHORT).show();

                } else {
                    spinnerSelectedItem = item;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        Button resultButton = (Button) findViewById(R.id.button_result_view);
        resultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent(GameActivity.this, ResultActivity.class);

                HashMap<String, Integer> scores = game.getActivePlayer().getScores();
                resultIntent.putExtra(EXTRA_SCORE_BOARD, scores);
                startActivity(resultIntent);
            }
        });
    }

    private boolean verifyCategory(String what) {
        if (spinnerSelectedItemsList != null && !spinnerSelectedItemsList.isEmpty()) {
            for (String s : spinnerSelectedItemsList) {
                if (s.equals(what)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
