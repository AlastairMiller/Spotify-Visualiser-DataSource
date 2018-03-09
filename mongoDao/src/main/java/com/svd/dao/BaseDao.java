package com.svd.dao;

import com.svd.ClientHandler;

abstract class BaseDao {

    final ClientHandler clientHandler;
    BaseDao(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }
}
