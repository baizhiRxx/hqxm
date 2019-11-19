package com.baizhi.service;

import com.baizhi.entity.Album;

import java.util.List;
import java.util.Map;

public interface AlbumService  {
    Map showAllAlbums(Integer page, Integer row);
    String addAlbum(Album album);
    void updateAlbum(Album album);
    void deleteAlbums(String[] ids);
}
