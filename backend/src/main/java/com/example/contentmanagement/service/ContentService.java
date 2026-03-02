package com.example.contentmanagement.service;

import com.example.contentmanagement.dto.ContentDTO;
import com.example.contentmanagement.dto.FilmDTO;
import com.example.contentmanagement.dto.SeriesDTO;
import com.example.contentmanagement.dto.DocumentaryDTO;
import java.util.List;

public interface ContentService {
    FilmDTO createFilm(FilmDTO filmDTO, String username);
    SeriesDTO createSeries(SeriesDTO seriesDTO, String username);
    DocumentaryDTO createDocumentary(DocumentaryDTO documentaryDTO, String username);
    
    ContentDTO getContentById(String id);
    List<ContentDTO> getAllContent();
    
    FilmDTO updateFilm(String id, FilmDTO filmDTO);
    SeriesDTO updateSeries(String id, SeriesDTO seriesDTO);
    DocumentaryDTO updateDocumentary(String id, DocumentaryDTO documentaryDTO);
    
    void deleteContent(String id);
}
