package com.bignerdranch.android.thirty;

import android.content.Intent;
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
import java.util.Map;

public class ResultActivity extends AppCompatActivity {

    private static final String EXTRA_SCORE_BOARD =
            "com.bignerdranch.android.thirty.score_board";

    private TableLayout tableLayout;
    private Button returnButton;


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //
    }

    @Override // fixa rotation!
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);

        Intent intent = getIntent();
        HashMap<String, Integer> results = (HashMap<String, Integer>)intent.getSerializableExtra(EXTRA_SCORE_BOARD);

        tableLayout = (TableLayout) findViewById(R.id.result_view_table);

        returnButton = (Button) findViewById(R.id.button_return);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        TextView valueView;
        TextView categoryView;
        Integer value;

        for (Map.Entry<String, Integer> entry : results.entrySet()) {

            TableRow row = new TableRow(this);
            row.setPadding(10, 0, 10, 0); // skillnad?

            categoryView = new TextView(this);
            categoryView.setPadding(0, 0, 20, 10);
            valueView = new TextView(this);
            valueView.setPadding(0, 0, 0, 10);
            categoryView.setText(entry.getKey() + ": ");
            value = entry.getValue();
            if (value != null) {
                valueView.setText("" + entry.getValue());
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