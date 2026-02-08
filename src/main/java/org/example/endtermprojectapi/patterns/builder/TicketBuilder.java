package org.example.endtermprojectapi.patterns.builder;

import org.example.endtermprojectapi.exception.InvalidInputException;
import org.example.endtermprojectapi.model.StandardTicket;
import org.example.endtermprojectapi.model.TicketBase;
import org.example.endtermprojectapi.model.VipTicket;

public class TicketBuilder {

    private Long customerId;
    private Long movieId;
    private String type;
    private Double basePrice;

    public TicketBuilder customerId(Long customerId) {
        this.customerId = customerId;
        return this;
    }

    public TicketBuilder movieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }

    public TicketBuilder type(String type) {
        this.type = type;
        return this;
    }

    public TicketBuilder basePrice(Double basePrice) {
        this.basePrice = basePrice;
        return this;
    }

    public TicketBase build() {
        if (customerId == null || customerId <= 0) throw new InvalidInputException("customerId must be > 0");
        if (movieId == null || movieId <= 0) throw new InvalidInputException("movieId must be > 0");
        if (type == null || type.isBlank()) throw new InvalidInputException("type is required");
        if (basePrice == null || basePrice <= 0) throw new InvalidInputException("basePrice must be > 0");

        String t = type.trim().toUpperCase();

        return switch (t) {
            case "STANDARD" -> new StandardTicket(customerId, movieId, basePrice);
            case "VIP" -> new VipTicket(customerId, movieId, basePrice);
            default -> throw new InvalidInputException("Unknown ticket type: " + type);
        };
    }
}