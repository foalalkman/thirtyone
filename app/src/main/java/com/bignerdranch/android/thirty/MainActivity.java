package com.bignerdranch.android.thirty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Author: Annika Svedin
 * email: annika.svedin@gmail.com
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

    @Override
    protected void onResume() {
        super.onResume();
        startGameActivity();
    }

    private void startGameActivity() {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

}
