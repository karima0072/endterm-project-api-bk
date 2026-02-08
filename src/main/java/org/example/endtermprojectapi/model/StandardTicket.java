package org.example.endtermprojectapi.model;

public class StandardTicket extends TicketBase {


    public StandardTicket(Long customerId, Long movieId, double basePrice) {
        this(null, customerId, movieId, basePrice, basePrice);
    }


    public StandardTicket(Long id, Long customerId, Long movieId, double basePrice, double finalPrice) {
        super(id, customerId, movieId, "STANDARD", basePrice, finalPrice);
    }
}