package com.baizhi.service;

import com.baizhi.dao.AlbumDao;
import com.baizhi.entity.Album;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Transactional
public class AlbumServiceImpl implements AlbumService{
    @Autowired
    private AlbumDao albumDao;
    @Override
    public Map showAllAlbums(Integer page, Integer row) {
        //jqGrid 需要page当前页数  total总页数  rows数据  records总条数
        HashMap hashMap = new HashMap();
        List<Album> albums = albumDao.selectByRowBounds(new Album(), new RowBounds((page - 1) * row, row));
        int records = albumDao.selectCount(new Album());
        int total = records%row==0?records/row:records/row+1;
        hashMap.put("page",page);
        hashMap.put("total",total);
        hashMap.put("rows",albums);
        hashMap.put("records",records);
        return hashMap;
    }
    public String addAlbum(Album album){
        String id = UUID.randomUUID().toString();
        album.setId(id);
        album.setCreate_date(new Date());
        album.setPublish_date(new Date());
        albumDao.insert(album);
        return id;
    }
    public void updateAlbum(Album album){
        albumDao.updateByPrimaryKeySelective(album);
    }
    public void deleteAlbums(String[] ids){
        Example example = new Example(Album.class);
        example.createCriteria().andIn("id",Arrays.asList(ids));
        albumDao.deleteByExample(example);
    }
}
