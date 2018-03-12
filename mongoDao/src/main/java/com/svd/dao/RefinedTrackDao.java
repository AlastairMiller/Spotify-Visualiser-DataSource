package com.svd.dao;

import com.svd.ClientHandler;
import refinedDataModels.RefinedTrack;

public class RefinedTrackDao extends BaseDao<RefinedTrack> {
 public RefinedTrackDao(ClientHandler clientHandler) {
   super(clientHandler);
 }


  public RefinedTrack retrieveEntryById(String itemId) {
        return (RefinedTrack) super.retrieveEntryById(itemId, "RefinedTracks");
    }
}
