package com.ipda3.sofraapp.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ipda3.sofraapp.data.room.model.Cart;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    public void addCartItem(Cart cartItem);

    @Delete
    public void deleteCartItem(Cart cartItem);

    @Update
    public void updateCartItem(Cart cartItem);

    @Query("select * from Cart;")
    List<Cart> getAllCartItems();

    @Query("Delete from Cart;")
    void deleteAllCartItems();

}
