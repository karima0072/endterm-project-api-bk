package org.example.endtermprojectapi.controller;

import org.example.endtermprojectapi.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final TicketService ticketService;

    public CacheController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Manual cache clear
    @DeleteMapping("/tickets")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearTicketsCache() {
        ticketService.clearCache();
    }
}