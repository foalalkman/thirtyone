package com.bignerdranch.android.thirty;

/**
 * Author: Annika Svedin
 * email: annika.svedin@gmail.com
 * */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashMap;

public class ResultActivity extends AppCompatActivity {

    private static final String EXTRA_SCORE_BOARD =
            "com.bignerdranch.android.thirty.score_board";

    private HashMap<String, Integer> results;
    private TableLayout tableLayout;
    private int total = 0;

    private void fetchResults() {
        Intent intent = getIntent();
        results = (HashMap<String, Integer>)intent.getSerializableExtra(EXTRA_SCORE_BOARD);
    }

    private TextView generateTextView(String text) {
        TextView textView = new TextView(this);
        textView.setPadding(0, 0, 20, 10);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(text);

        return textView;
    }

    private TextView setValueOnView(Integer value) {
        TextView valueView;

        if (value != null) {
            valueView = generateValueView(""+value+"");
            total += value;

        } else {
            valueView = generateValueView("");
        }

        return valueView;
    }

    private TextView generateValueView(String text) {
        TextView valueView = new TextView(this);
        valueView.setPadding(0, 0, 0, 10);
        valueView.setTextSize(20);
        valueView.setText(text);

        return valueView;
    }
    
    private void generateResultTable() {
        TextView valueView, categoryView;
        Integer value;
        TableRow row;

        for (int i = 3; i <= 12; i++) {
            row = new TableRow(this);
            if (i == 3) {
                categoryView = generateTextView("LOW");
                value = results.get("LOW");

            } else {
                categoryView = generateTextView("" + i + ": ");
                value = results.get(""+i);
            }

            valueView = setValueOnView(value);
            addViewToTable(row, categoryView, valueView);
        }
    }

    private void addViewToTable(TableRow row, TextView categoryView, TextView valueView) {
        row.addView(categoryView);
        row.addView(valueView);
        tableLayout.addView(row);
    }

    private TableRow generateTotalRow() {
        TableRow row = new TableRow(this);

        TextView categoryView = new TextView(this);
        categoryView.setPadding(0, 0, 20, 10);
        categoryView.setTextSize(25);
        categoryView.setTypeface(null, Typeface.BOLD);
        categoryView.setText("Total: ");

        TextView valueView = new TextView(this);
        valueView.setTextSize(20);
        valueView.setText(""+ total +"");

        row.addView(categoryView);
        row.addView(valueView);

        return row;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);

        fetchResults();
        tableLayout = (TableLayout) findViewById(R.id.result_view_table);

        generateResultTable();
        tableLayout.addView(generateTotalRow());
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
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}