package com.example.demo.controller;


import com.example.demo.model.ErrorResponse;
import com.example.demo.model.Note;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller to demonstrate Exception handling.
 */
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private List<Note> notes = new ArrayList<>();
    private Long nextId = 1L;

    // GET all books
    @GetMapping
    public List<Note> getAllNotes() {
        return notes;
    }

    // GET book by ID
    @GetMapping("/{id}")
    public Note getNote(@PathVariable Long id) throws NoDataFoundException {
        Note n = notes.stream()
                    .filter(b -> b.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        if (n == null) throw new NoDataFoundException();
        return n;
    }

    // POST - Create book
    @PostMapping
    public Note createNote(@RequestBody Note n) {
        n.setId(nextId++);
        notes.add(n);
        return n;
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoDataFound(Exception ex) {

        ErrorResponse error = new ErrorResponse(
                420,
                "Invalid request",
                System.currentTimeMillis()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

}
