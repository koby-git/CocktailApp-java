package com.koby.myapplication.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.koby.myapplication.model.Cocktail;

@Database(entities = Cocktail.class,version = 4)
public abstract class CocktailDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "cocktail_database";

    private static CocktailDatabase instance;

    public static synchronized CocktailDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    CocktailDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract CocktailDao getCocktailDao();
}
