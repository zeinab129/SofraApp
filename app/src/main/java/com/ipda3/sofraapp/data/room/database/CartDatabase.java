package com.ipda3.sofraapp.data.room.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ipda3.sofraapp.data.room.dao.CartDao;
import com.ipda3.sofraapp.data.room.model.Cart;

@Database(entities = {Cart.class}, version = 1, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {

    public abstract CartDao cartDao();

    private static CartDatabase cartDatabase;

    public static CartDatabase getInstance(Context context){
        if(cartDatabase == null){
            cartDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    CartDatabase.class, "cart_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return cartDatabase;
    }

}
