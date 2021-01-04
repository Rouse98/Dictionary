package com.dict.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView helloTextView = (TextView) findViewById(R.id.text_view_id);
        final SearchView sv = (SearchView) findViewById(R.id.search_bar);
        final ListView result_list = (ListView) findViewById(R.id.result_list_id);
        sv.setIconifiedByDefault(false);


        final DataAdapter mDbHelper = new DataAdapter(this);
        mDbHelper.createDatabase();
        mDbHelper.open();

        //Default or welcome message
        helloTextView.setText("ようこそ");
        //Search is queried
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Cursor cur = mDbHelper.searchEnglish(query);
                String Japanese ="";
                String English = "";
                String HiraKata = "";
                String output_eng_in = "";
                List<String> resList = new ArrayList<String>();
                //Checks if query yielded results from the database
                if( cur != null && cur.moveToFirst() ){
                //Gets the data from each row and combines them then adds it too the array list
                    while (cur.moveToNext()) {
                        Japanese = cur.getString(cur.getColumnIndex("kanji"));
                        HiraKata = cur.getString(cur.getColumnIndex("reading"));
                        English = cur.getString(cur.getColumnIndex("glosses"));

                        // Formatting for the english outputs: dropping the semicolons for commas
                        English=English.replaceAll(";", ", ");
                        int length = English.length()-2;
                        English = English.substring(0,length) ;

                        //If the kanji is null or empty then don't output anything for it.
                        if(Japanese == null || Japanese.equals("null")){
                            output_eng_in = "\n"+"["+HiraKata+"]"+"\n"+"English: "+English + "\n";
                        }else {
                            output_eng_in = "\n" +"["+HiraKata+"]"+"\n"+"English: "+ English + "\n" + "Kanji: " + Japanese + "\n";
                        }
                        resList.add(output_eng_in);
                    }

                    cur.close();

                }else{
                    //Not in the database
                    resList.add("No Results");
                }

                helloTextView.setText("Results for: "+ query );
                //Puts the items in the array list in the list view that is displayed
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, resList);
                result_list.setAdapter(arrayAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
            
            return false;
        }
    });


    }
}