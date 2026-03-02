package com.example.contentmanagement.service.impl;

import com.example.contentmanagement.dto.*;
import com.example.contentmanagement.entity.*;
import com.example.contentmanagement.exception.ResourceNotFoundException;
import com.example.contentmanagement.repository.CategoryRepository;
import com.example.contentmanagement.repository.ContentRepository;
import com.example.contentmanagement.repository.UserRepository;
import com.example.contentmanagement.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FilmDTO createFilm(FilmDTO filmDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        Category category = categoryRepository.findById(filmDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + filmDTO.getCategoryId()));

        Film film = new Film();
        mapCommonFieldsToEntity(filmDTO, film, category, user);
        film.setDurationInMinutes(filmDTO.getDurationInMinutes());
        film.setDirector(filmDTO.getDirector());

        Film savedFilm = contentRepository.save(film);
        return mapToFilmDTO(savedFilm);
    }

    @Override
    @Transactional
    public SeriesDTO createSeries(SeriesDTO seriesDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        Category category = categoryRepository.findById(seriesDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + seriesDTO.getCategoryId()));

        Series series = new Series();
        mapCommonFieldsToEntity(seriesDTO, series, category, user);
        series.setNumberOfSeasons(seriesDTO.getNumberOfSeasons());
        series.setNumberOfEpisodes(seriesDTO.getNumberOfEpisodes());
        series.setIsCompleted(seriesDTO.getIsCompleted());

        Series savedSeries = contentRepository.save(series);
        return mapToSeriesDTO(savedSeries);
    }

    @Override
    @Transactional
    public DocumentaryDTO createDocumentary(DocumentaryDTO documentaryDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        Category category = categoryRepository.findById(documentaryDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + documentaryDTO.getCategoryId()));

        Documentary documentary = new Documentary();
        mapCommonFieldsToEntity(documentaryDTO, documentary, category, user);
        documentary.setTopic(documentaryDTO.getTopic());
        documentary.setNarrator(documentaryDTO.getNarrator());

        Documentary savedDoc = contentRepository.save(documentary);
        return mapToDocumentaryDTO(savedDoc);
    }

    @Override
    public ContentDTO getContentById(String id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found: " + id));
        return mapToDTO(content);
    }

    @Override
    public List<ContentDTO> getAllContent() {
        return contentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FilmDTO updateFilm(String id, FilmDTO filmDTO) {
        Film film = (Film) contentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Film not found: " + id));
        Category category = categoryRepository.findById(filmDTO.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + filmDTO.getCategoryId()));

        film.setTitle(filmDTO.getTitle());
        film.setDescription(filmDTO.getDescription());
        film.setReleaseDate(filmDTO.getReleaseDate());
        film.setCategory(category);
        film.setDurationInMinutes(filmDTO.getDurationInMinutes());
        film.setDirector(filmDTO.getDirector());

        return mapToFilmDTO(contentRepository.save(film));
    }

    @Override
    @Transactional
    public SeriesDTO updateSeries(String id, SeriesDTO seriesDTO) {
        Series series = (Series) contentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Series not found: " + id));
        Category category = categoryRepository.findById(seriesDTO.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + seriesDTO.getCategoryId()));

        series.setTitle(seriesDTO.getTitle());
        series.setDescription(seriesDTO.getDescription());
        series.setReleaseDate(seriesDTO.getReleaseDate());
        series.setCategory(category);
        series.setNumberOfSeasons(seriesDTO.getNumberOfSeasons());
        series.setNumberOfEpisodes(seriesDTO.getNumberOfEpisodes());
        series.setIsCompleted(seriesDTO.getIsCompleted());

        return mapToSeriesDTO(contentRepository.save(series));
    }

    @Override
    @Transactional
    public DocumentaryDTO updateDocumentary(String id, DocumentaryDTO documentaryDTO) {
        Documentary doc = (Documentary) contentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Documentary not found: " + id));
        Category category = categoryRepository.findById(documentaryDTO.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + documentaryDTO.getCategoryId()));

        doc.setTitle(documentaryDTO.getTitle());
        doc.setDescription(documentaryDTO.getDescription());
        doc.setReleaseDate(documentaryDTO.getReleaseDate());
        doc.setCategory(category);
        doc.setTopic(documentaryDTO.getTopic());
        doc.setNarrator(documentaryDTO.getNarrator());

        return mapToDocumentaryDTO(contentRepository.save(doc));
    }

    @Override
    @Transactional
    public void deleteContent(String id) {
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Content not found: " + id));
        contentRepository.delete(content);
    }

    private void mapCommonFieldsToEntity(ContentDTO dto, Content entity, Category category, User user) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setReleaseDate(dto.getReleaseDate());
        entity.setCategory(category);
        entity.setAddedBy(user);
    }

    private ContentDTO mapToDTO(Content content) {
        if (content instanceof Film) return mapToFilmDTO((Film) content);
        if (content instanceof Series) return mapToSeriesDTO((Series) content);
        if (content instanceof Documentary) return mapToDocumentaryDTO((Documentary) content);
        return null;
    }

    private FilmDTO mapToFilmDTO(Film film) {
        FilmDTO dto = new FilmDTO();
        mapCommonFieldsToDTO(film, dto);
        dto.setDurationInMinutes(film.getDurationInMinutes());
        dto.setDirector(film.getDirector());
        dto.setContentType("FILM");
        return dto;
    }

    private SeriesDTO mapToSeriesDTO(Series series) {
        SeriesDTO dto = new SeriesDTO();
        mapCommonFieldsToDTO(series, dto);
        dto.setNumberOfSeasons(series.getNumberOfSeasons());
        dto.setNumberOfEpisodes(series.getNumberOfEpisodes());
        dto.setIsCompleted(series.getIsCompleted());
        dto.setContentType("SERIES");
        return dto;
    }

    private DocumentaryDTO mapToDocumentaryDTO(Documentary doc) {
        DocumentaryDTO dto = new DocumentaryDTO();
        mapCommonFieldsToDTO(doc, dto);
        dto.setTopic(doc.getTopic());
        dto.setNarrator(doc.getNarrator());
        dto.setContentType("DOCUMENTARY");
        return dto;
    }

    private void mapCommonFieldsToDTO(Content entity, ContentDTO dto) {
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setReleaseDate(entity.getReleaseDate());
        dto.setCategoryId(entity.getCategory().getId());
        dto.setCategoryName(entity.getCategory().getName());
        dto.setAddedById(entity.getAddedBy().getId());
        dto.setAddedByUsername(entity.getAddedBy().getUsername());
    }
}
