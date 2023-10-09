package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("INSERT INTO NOTES (noteid, notetitle, notedescription, userid) VALUES (#{noteId}, #{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    void updateNote(Note note);

    @Select("SELECT * FROM NOTES WHERE userid= #{userId} ")
    List<Note> getAllNotes(Integer userId);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND userid=#{userId}")
    void deleteNote(Integer noteId, Integer userId);
}
