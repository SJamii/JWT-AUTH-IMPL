package com.example.petProject.service.impl;

import com.example.petProject.dto.BookDTO;
import com.example.petProject.entity.Book;
import com.example.petProject.exception.ResourceNotFoundException;
import com.example.petProject.repository.BookRepository;
import com.example.petProject.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> findAll() {
        final List<Book> books = bookRepository.findAll();
        List<BookDTO> responses;
        responses = books.stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public BookDTO save(BookDTO bookDTO) {
        final Book book = new Book();
        bookRepository.save(bookDTO.toEntity(bookDTO));
        return bookDTO;
    }

    @Override
    @Transactional
    public BookDTO findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        return BookDTO.fromEntity(book);
    }

    @Override
    @Transactional
    public BookDTO update(BookDTO bookDto) {
        Book book = bookRepository.findById(Long.valueOf(bookDto.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookDto.getId()));
        bookRepository.save(book);
        return bookDto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }
}
