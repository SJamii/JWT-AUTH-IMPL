package com.example.petProject.controller;

import com.example.petProject.dto.BookDTO;
import com.example.petProject.service.BookService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.petProject.utils.ApiResponse.success;


@RestController
@RequiredArgsConstructor

@RequestMapping("/api/v1/book")

public class BookController {
    private final BookService bookService;


    @GetMapping("/list")
    @PreAuthorize("hasAuthority('BOOK_LIST_VIEW')")
    public ResponseEntity<JSONObject> bookLists() {
        List<BookDTO> responses = bookService.findAll();
        return new ResponseEntity<>(
                success(responses, HttpStatus.OK.value(), "Book List").getJson(),
                HttpStatus.OK);
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('BOOK_ADD')")
    public ResponseEntity<JSONObject> save(@RequestBody BookDTO bookDTO, BindingResult bindingResult
    ) {
        BookDTO response = bookService.save(bookDTO);
        return new ResponseEntity<>(
                success(response, HttpStatus.CREATED.value(), "Book Instance created successfully").getJson(),
                HttpStatus.CREATED);
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('BOOK_VIEW')")
    public ResponseEntity<JSONObject> findById(@PathVariable("id") Long id) {

        BookDTO response = bookService.findById(id);
        return new ResponseEntity<>(
                success(response, HttpStatus.OK.value(), "Book Retrieved Successfully").getJson(),
                HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('BOOK_UPDATE')")
    public ResponseEntity<JSONObject> update(@RequestBody BookDTO bookDTO, BindingResult bindingResult
    ) {

        BookDTO response = bookService.update(bookDTO);
        return new ResponseEntity<>(
                success(response, HttpStatus.OK.value(), "Book Instance updated successfully").getJson(),
                HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('BOOK_DELETE')")
    public ResponseEntity<JSONObject> delete(@PathVariable(value = "id") Long id) {
        bookService.delete(id);
        return new ResponseEntity<>(
                success("Deleted book with id: " + id, HttpStatus.OK.value()).getJson(),
                HttpStatus.OK);
    }
}
