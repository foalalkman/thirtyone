package com.bignerdranch.android.thirty;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private static final String EXTRA_SCORE_BOARD =
            "com.bignerdranch.android.thirty.score_board";

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);

        Intent intent = getIntent();
        HashMap<String, Integer> results = (HashMap<String, Integer>)intent.getSerializableExtra(EXTRA_SCORE_BOARD);

        Button returnButton = (Button) findViewById(R.id.button_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TableLayout tableLayout = (TableLayout) findViewById(R.id.result_view_table);

        TextView valueView;
        TextView categoryView;
        Integer value;

        for (int i = 3; i <= 10; i++) {
            TableRow row = new TableRow(this);
            row.setPadding(10, 0, 10, 0); // skillnad?

            categoryView = new TextView(this);
            categoryView.setPadding(0, 0, 20, 10);
            categoryView.setTextSize(20);
            categoryView.setTypeface(null, Typeface.BOLD);

            valueView = new TextView(this);
            valueView.setPadding(0, 0, 0, 10);
            valueView.setTextSize(20);

            if (i == 3) {

                categoryView.setText("LOW:");
                value = results.get("LOW");

            } else {
                categoryView.setText("" + i + ": ");
                value = results.get(""+i);
            }

            if (value != null) {
                valueView.setText(""+value+"");
            }

            row.addView(categoryView);
            row.addView(valueView);

            tableLayout.addView(row);
        }
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