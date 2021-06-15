/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetDbHelper;
import com.example.android.pets.data.PetContract.PetEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PET_LOADER=0;
    PetCursorAdapter mPetCursorAdapter;
private  PetDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

         //Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView petListView=(ListView)findViewById(R.id.list);
        View emptyView=findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);
         mPetCursorAdapter=new PetCursorAdapter(this,null);
         petListView.setAdapter(mPetCursorAdapter);


         //setup item click listener
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   // create intent to go to editoacitity class
                Intent intent=new Intent(CatalogActivity.this,EditorActivity.class);

                //form the content uri that represent specific uri  that was clicked on
                //by appending id of the Pet onto content uri
                Uri uri= ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);

                //set data of uri data into intent
                intent.setData(uri);

                //start intent
                startActivity(intent);

            }
        });



        getLoaderManager().initLoader(PET_LOADER,null,this);

       //mDbHelper=new PetDbHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //dispplayDataBaseInfo();
    }

//    public  void dispplayDataBaseInfo()
//    {
//
//
//  SQLiteDatabase db=mDbHelper.getReadableDatabase();
//        // Define a projection that specifies which columns from the database
//        // you will actually use after this query.
//        String[] projection = {
//                PetEntry._ID,
//                PetEntry.COLUMN_PET_NAME,
//                PetEntry.COLUMN_PET_BREED,
//                PetEntry.COLUMN_PET_GENDER,
//                PetEntry.COLUMN_PET_WEIGHT };
//
//        // Perform a query on the pets table
////        Cursor cursor = db.query(
////                PetEntry.TABLE_NAME,   // The table to query
////                projection,            // The columns to return
////                null,                  // The columns for the WHERE clause
////                null,                  // The values for the WHERE clause
////                null,                  // Don't group the rows
////                null,                  // Don't filter by row groups
////                null);                   // The sort order
//
//
//        Cursor cursor=getContentResolver().query(PetEntry.CONTENT_URI,projection,null,null,null);
//        ListView petListView=(ListView)findViewById(R.id.list);
//        PetCursorAdapter adapter=new PetCursorAdapter(this,cursor);
//        petListView.setAdapter(adapter);
//
//
//    }

    private void insertPet()
    {    // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link PetEntry#CONTENT_URI} to indicate that we want to insert
        // into the pets database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
        Toast.makeText(this,"sample Pet added"+newUri,Toast.LENGTH_LONG).show();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
             //  dispplayDataBaseInfo();
                // Do nothing for now
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void deleteAllPets()
    {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String projection[]={PetEntry._ID,PetEntry.COLUMN_PET_NAME,PetEntry.COLUMN_PET_BREED};
        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,

                null,
                null,
                null

                );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
   // update with new cursor that containing updated data
        mPetCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //metod called when data need to be deleted
        mPetCursorAdapter.swapCursor(null);

    }
}
