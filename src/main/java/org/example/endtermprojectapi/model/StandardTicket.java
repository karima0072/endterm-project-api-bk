package org.example.endtermprojectapi.model;

public class StandardTicket extends TicketBase {

    // для создания (id нет)
    public StandardTicket(Long customerId, Long movieId, double basePrice) {
        this(null, customerId, movieId, basePrice, basePrice);
    }

    // для чтения из БД (id и finalPrice уже есть)
    public StandardTicket(Long id, Long customerId, Long movieId, double basePrice, double finalPrice) {
        super(id, customerId, movieId, "STANDARD", basePrice, finalPrice);
    }
}