package com.example.petProject.service;

import com.example.petProject.dto.BookDTO;

import java.util.List;

public interface BookService {
    List<BookDTO> findAll();
    BookDTO save(BookDTO demoDto);
    BookDTO findById(Long id);
    BookDTO update(BookDTO demoDto);
    void delete(Long id);
}
