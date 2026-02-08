package org.example.endtermprojectapi.model;

public class VipTicket extends TicketBase {

    public VipTicket(Long customerId, Long movieId, double basePrice) {
        this(null, customerId, movieId, basePrice, basePrice * 1.5);
    }

    public VipTicket(Long id, Long customerId, Long movieId, double basePrice, double finalPrice) {
        super(id, customerId, movieId, "VIP", basePrice, finalPrice);
    }
}