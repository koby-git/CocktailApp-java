package com.koby.myapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.koby.myapplication.model.Cocktail;

import java.util.List;


@Dao
public interface CocktailDao {

    @Insert()
    void insert(Cocktail cocktail);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Cocktail> cocktails);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insert(Cocktail... cocktails);

    @Update()
    int update(Cocktail cocktail);

    //TODO:add delete function
    @Delete
    void delete(Cocktail cocktail);

    @Query("UPDATE cocktail_table SET id = :cocktail_id," +
            " name = :name," +
            " imageUri = :imageUri," +
            " instruction = :instruction," +
            " isPopular = 1 " +
            "WHERE id = :cocktail_id")
    void update(String cocktail_id, String name, String imageUri,String instruction);

    @Query("SELECT * FROM cocktail_table WHERE isFavorite = 1")
    LiveData<List<Cocktail>> getFavoriteCocktails();

    @Query("SELECT * FROM cocktail_table WHERE isPopular = 1")
    LiveData<List<Cocktail>> getPopularCocktails();

    @Query("SELECT * FROM cocktail_table WHERE name LIKE '%' || :query || '%' ")
    LiveData<List<Cocktail>> getSearchedCocktails(String query);
}
