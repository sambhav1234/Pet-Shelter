package com.example.android.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

public  final class PetContract {

    public static  final String  CONTENT_AUTHORITY="com.example.android.pets";
    public static Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);
    public  static final String PATH_PETS="pets";



    public static final class PetEntry implements BaseColumns
    {
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.withAppendedPath(BASE_CONTENT_URI,PATH_PETS);
        public static final String CONTENT_LIST_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PETS;
        public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_PETS;
        public final static String COLUMN_ID=BaseColumns._ID;
        public final static String TABLE_NAME="Pets";
        public final static String COLUMN_PET_NAME="name";
        public final static String COLUMN_PET_BREED="breed";
        public final static String COLUMN_PET_GENDER="gender";
        public final static String COLUMN_PET_WEIGHT="weight";


        public final static int GENDER_MALE=1;
        public final static int GENDER_FEMALE=0;
        public static final int GENDER_UNKNOWN=2;
    public static boolean isValidGender(int gender)
    {
        if(gender==GENDER_FEMALE||gender==GENDER_MALE||gender==GENDER_UNKNOWN)
            return true;
        return false;
    }


    }

}
