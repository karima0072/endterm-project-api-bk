package org.example.endtermprojectapi.dto;

public class TicketResponse {

    private Long id;
    private Long customerId;
    private Long movieId;
    private String type;
    private double basePrice;
    private double finalPrice;

    public TicketResponse(Long id, Long customerId, Long movieId,
                          String type, double basePrice, double finalPrice) {
        this.id = id;
        this.customerId = customerId;
        this.movieId = movieId;
        this.type = type;
        this.basePrice = basePrice;
        this.finalPrice = finalPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public String getType() {
        return type;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }
}