package com.example.petProject.controller;


import com.example.petProject.customAnnotation.ModuleName;
import com.example.petProject.customAnnotation.ModuleAndDependentModule;
import com.example.petProject.dto.BookDTO;
import com.example.petProject.enums.FeatureEnum;
import com.example.petProject.enums.ModuleEnum;
import com.example.petProject.service.BookService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.petProject.utils.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@ModuleName(name = ModuleEnum.AUTHOR)
@RequestMapping("/api/v1/author")
public class AuthorController {
    private final BookService bookService;

    @GetMapping("/list")
//    @ModuleAndDependentModule(moduleName = ModuleName.AUTHOR, dependentModules = {ModuleName.BOOK})
    @ModuleAndDependentModule(moduleName = ModuleEnum.PUBLISHER, dependentFeatures = {FeatureEnum.BOOK_ADD, FeatureEnum.BOOK_LIST}, isInternal = true)
    @PreAuthorize("hasAuthority('AUTHOR_VIEW')")
    public ResponseEntity<JSONObject> bookLists() {
        List<BookDTO> responses = bookService.findAll();
        return new ResponseEntity<>(
                success(responses, HttpStatus.OK.value(), "Author List").getJson(),
                HttpStatus.OK);
    }


}
