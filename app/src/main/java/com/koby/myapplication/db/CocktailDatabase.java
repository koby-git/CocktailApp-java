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

    private static CocktailDatabase instance;
    public abstract CocktailDao getCocktailDao();
    public static synchronized CocktailDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CocktailDatabase.class, "cocktail_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

        }
    };
}
