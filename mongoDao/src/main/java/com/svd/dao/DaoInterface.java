package com.svd.dao;

import java.util.List;

public interface DaoInterface<T> {

    T getById(String id);

    List<T> getMultipleById(List<String> ids);

    T getBySpotifyUri(String spotifyId);

    void save(T inputObject);

    void saveList(List<T> input);

    void update(String id, T newObject);

    void delete(String id);

}
