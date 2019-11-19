package com.baizhi.service;

import com.baizhi.entity.Chapter;

import java.util.Map;

public interface ChapterService {
    Map showAllChapters(Integer page,Integer rows,String id);
    String addChapter(Chapter chapter, String albumId);
    void updateChapter(Chapter chapter);
    void deleteChapters(String[] ids);
}
