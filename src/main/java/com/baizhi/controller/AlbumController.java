package com.baizhi.controller;

import com.baizhi.dao.AlbumDao;
import com.baizhi.entity.Album;
import com.baizhi.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("album")
public class AlbumController {
    @Autowired
    private AlbumService albumService;
    @Autowired
    private AlbumDao albumDao;
    @RequestMapping("showAllAlbum")
    public Map showAllAlbum(Integer page,Integer rows){
        Map map = albumService.showAllAlbums(page, rows);
        return map;
    }
    @RequestMapping("editAlbum")
    public Map editAlbum(Album album,String[] id,String oper){
        HashMap hashMap = new HashMap();
        if (oper.equals("add")) {
            String s = albumService.addAlbum(album);
            hashMap.put("albumId",s);
            return hashMap;
        } else if (oper.equals("edit")) {
            albumService.updateAlbum(album);
        } else if (oper.equals("del")) {
            albumService.deleteAlbums(id);
        }
        return null;
    }
    @RequestMapping("uploadAlbum")
    public void uploadAlbum(MultipartFile cover, HttpSession session,String id){
        String realPath = session.getServletContext().getRealPath("/img");
        File file = new File(realPath);
        if (!file.exists()){
            file.mkdirs();
        }
        String coverName = new Date().getTime() + "_" + cover.getOriginalFilename();
        System.out.println(coverName);
        try {
            cover.transferTo(new File(realPath,coverName));
            Album album = new Album();
            album.setId(id);
            album.setCover(coverName);
            System.out.println(album);
            albumDao.updateByPrimaryKeySelective(album);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
