package com.bignerdranch.android.thirty;

/**
 * Author: Annika Svedin
 * email: annika.svedin@gmail.com
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * This activity class is a part of the controller layer,
 * which controls and assembles the representation of the actvity_game.xml,
 * die_view.xml and dice_view.xml.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The first thing that happens when the activity is launched. Initializes the widgets and the
     * state of the activity.
     * @param savedInstanceState a bundle storing the state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game);
        startGameActivity();
    }

    /**
     * To make Restart work:
     * When the GameActivity is being shut down a new one will launch.
     */
    @Override
    protected void onResume() {
        super.onResume();
        startGameActivity();
    }

    /**
     * Starts a new GameActivity.
     */
    private void startGameActivity() {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }
}
