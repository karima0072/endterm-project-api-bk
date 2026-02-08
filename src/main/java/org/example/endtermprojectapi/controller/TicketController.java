package org.example.endtermprojectapi.controller;

import org.example.endtermprojectapi.dto.TicketRequest;
import org.example.endtermprojectapi.dto.TicketResponse;
import org.example.endtermprojectapi.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService service;

    public TicketController(TicketService service) {
        this.service = service;
    }

    @GetMapping
    public List<TicketResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public TicketResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse create(@RequestBody TicketRequest request) {
        return service.create(request);
    }

    // ✅ UPDATE (CRUD)
    @PutMapping("/{id}")
    public TicketResponse update(@PathVariable Long id, @RequestBody TicketRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}