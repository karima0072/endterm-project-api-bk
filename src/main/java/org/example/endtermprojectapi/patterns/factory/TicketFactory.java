package org.example.endtermprojectapi.patterns.factory;

import org.example.endtermprojectapi.model.StandardTicket;
import org.example.endtermprojectapi.model.TicketBase;
import org.example.endtermprojectapi.model.VipTicket;
import org.springframework.stereotype.Component;

@Component
public class TicketFactory {

    public TicketBase create(String type, Long customerId, Long movieId, double basePrice) {
        if ("VIP".equalsIgnoreCase(type)) {
            return new VipTicket(customerId, movieId, basePrice);
        }
        if ("STANDARD".equalsIgnoreCase(type)) {
            return new StandardTicket(customerId, movieId, basePrice);
        }
        throw new IllegalArgumentException("Unknown ticket type: " + type);
    }

    public TicketBase createFromDb(
            Long id,
            Long customerId,
            Long movieId,
            String type,
            double basePrice,
            double finalPrice
    ) {
        TicketBase t;

        if ("VIP".equalsIgnoreCase(type)) {
            t = new VipTicket(customerId, movieId, basePrice);
        } else {
            t = new StandardTicket(customerId, movieId, basePrice);
        }

        t.setId(id);
        t.setFinalPrice(finalPrice);

        return t;
    }
}