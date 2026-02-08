package org.example.endtermprojectapi.model;

public abstract class TicketBase {

    protected Long id;
    protected Long customerId;
    protected Long movieId;
    protected String type;
    protected double basePrice;
    protected double finalPrice;

    protected TicketBase(
            Long id,
            Long customerId,
            Long movieId,
            String type,
            double basePrice,
            double finalPrice
    ) {
        this.id = id;
        this.customerId = customerId;
        this.movieId = movieId;
        this.type = type;
        this.basePrice = basePrice;
        this.finalPrice = finalPrice;
    }

    // ✅ ДОБАВЬ ЭТО
    public void setId(Long id) {
        this.id = id;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    // getters
    public Long getId() { return id; }
    public Long getCustomerId() { return customerId; }
    public Long getMovieId() { return movieId; }
    public String getType() { return type; }
    public double getBasePrice() { return basePrice; }
    public double getFinalPrice() { return finalPrice; }
}