package com.baizhi.controller;

import com.baizhi.dao.ChapterDao;
import com.baizhi.entity.Chapter;
import com.baizhi.service.ChapterService;
import org.apache.commons.io.FileUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("chapter")
public class ChapterController {
    @Autowired
    private ChapterService chapterService;
    @Autowired
    private ChapterDao chapterDao;

    @RequestMapping("showAllChapters")
    public Map showAllChapters(Integer page, Integer rows, String id) {
        Map map = chapterService.showAllChapters(page, rows, id);
        return map;
    }

    @RequestMapping("downloadChapter")
    public void downloadChapter(String url, HttpSession session, HttpServletResponse response) {
        String realPath = session.getServletContext().getRealPath("/music");
        File file = new File(realPath, url);
        response.setHeader("content-disposition", "attachment;fileName=" + url);
        try {
            FileUtils.copyFile(file, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("editChapter")
    public Map editChapter(Chapter chapter, String oper, String[] id, String albumId) {
        HashMap<String, String> map = new HashMap<>();
        if (oper.equals("add")) {
            String s = chapterService.addChapter(chapter, albumId);
            map.put("id", s);
        } else if (oper.equals("edit")) {
            chapterService.updateChapter(chapter);
        } else if (oper.equals("del")) {
            chapterService.deleteChapters(id);
        }
        return map;
    }

    @RequestMapping("uploadChapter")
    public void uploadChapter(MultipartFile url, HttpSession session, String id) {
        String realPath = session.getServletContext().getRealPath("/music");
        File file = new File(realPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String musicName = new Date().getTime() + "_" + url.getOriginalFilename();
        File music = new File(realPath, musicName);
        try {
            url.transferTo(music);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            AudioFile read = AudioFileIO.read(music);
            AudioHeader audioHeader = read.getAudioHeader();
            int trackLength = audioHeader.getTrackLength();
            String minute = trackLength % 60 + "分";
            String second = trackLength / 60 + "秒";
            Chapter chapter = new Chapter();
            chapter.setId(id);
            chapter.setUrl(musicName);
            String size = url.getSize() / 1024 / 1024 + "MB";
            chapter.setSize(size);
            chapter.setDuration(minute + second);
            chapterDao.updateByPrimaryKeySelective(chapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
