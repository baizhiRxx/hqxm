package com.baizhi.service;

import com.baizhi.dao.ChapterDao;
import com.baizhi.entity.Chapter;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
@Transactional
public class ChapterServiceImpl implements ChapterService {
    @Autowired
    private ChapterDao chapterDao;
    @Override
    public Map showAllChapters(Integer page, Integer rows,String id) {
        //jqGrid 需要page当前页数  total总页数  rows数据  records总条数
        HashMap hashMap = new HashMap();
        Chapter chapter = new Chapter();
        chapter.setAlbum_id(id);
        List<Chapter> chapters = chapterDao.selectByRowBounds(chapter, new RowBounds((page - 1) * rows, rows));
        int record = chapterDao.selectCount(chapter);
        int total = record%rows==0?record/rows:record/rows+1;
        hashMap.put("page",page);
        hashMap.put("total",total);
        hashMap.put("rows",chapters);
        hashMap.put("records",record);
        return hashMap;
    }
    public String addChapter(Chapter chapter,String albumId){
        String id = UUID.randomUUID().toString();
        chapter.setId(id);
        chapter.setAlbum_id(albumId);
        chapterDao.insert(chapter);
        return id;
    }
    public void updateChapter(Chapter chapter){
        chapterDao.updateByPrimaryKeySelective(chapter);
    }
    public void deleteChapters(String[] ids){
        Example example = new Example(Chapter.class);
        example.createCriteria().andIn("id",Arrays.asList(ids));
        chapterDao.deleteByExample(example);
    }
}
