package org.example.endtermprojectapi.service;

import org.example.endtermprojectapi.dto.TicketRequest;
import org.example.endtermprojectapi.dto.TicketResponse;
import org.example.endtermprojectapi.exception.DuplicateResourceException;
import org.example.endtermprojectapi.exception.InvalidInputException;
import org.example.endtermprojectapi.exception.NotFoundException;
import org.example.endtermprojectapi.model.TicketBase;
import org.example.endtermprojectapi.patterns.builder.TicketBuilder;
import org.example.endtermprojectapi.repository.TicketRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository repository;

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public List<TicketResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TicketResponse getById(Long id) {
        try {
            TicketBase t = repository.findById(id);
            return toResponse(t);
        } catch (EmptyResultDataAccessException ex) {
            throw new NotFoundException("Ticket not found: id=" + id);
        }
    }

    public TicketResponse create(TicketRequest request) {
        validate(request);

        TicketBase model = new TicketBuilder()
                .customerId(request.getCustomerId())
                .movieId(request.getMovieId())
                .type(request.getType())
                .basePrice(request.getBasePrice())
                .build();

        preventDuplicate(model, null);

        TicketBase saved = repository.save(model);
        return toResponse(saved);
    }

    public TicketResponse update(Long id, TicketRequest request) {
        validate(request);

        getById(id);

        TicketBase model = new TicketBuilder()
                .customerId(request.getCustomerId())
                .movieId(request.getMovieId())
                .type(request.getType())
                .basePrice(request.getBasePrice())
                .build();

        preventDuplicate(model, id);

        TicketBase updated = repository.update(id, model);
        return toResponse(updated);
    }

    public void delete(Long id) {
        getById(id);
        repository.deleteById(id);
    }


    private void validate(TicketRequest req) {
        if (req.getCustomerId() == null || req.getCustomerId() <= 0) {
            throw new InvalidInputException("customerId must be > 0");
        }
        if (req.getMovieId() == null || req.getMovieId() <= 0) {
            throw new InvalidInputException("movieId must be > 0");
        }
        if (req.getType() == null || req.getType().isBlank()) {
            throw new InvalidInputException("type is required (VIP or STANDARD)");
        }
        if (req.getBasePrice() <= 0) {
            throw new InvalidInputException("basePrice must be > 0");
        }
    }

    private void preventDuplicate(TicketBase model, Long excludeId) {
        boolean exists = repository.findAll().stream().anyMatch(t -> {
            boolean sameKey =
                    t.getCustomerId().equals(model.getCustomerId()) &&
                            t.getMovieId().equals(model.getMovieId()) &&
                            t.getType() != null &&
                            t.getType().equalsIgnoreCase(model.getType());

            if (!sameKey) return false;

            if (excludeId == null) return true;
            return t.getId() != null && !t.getId().equals(excludeId);
        });

        if (exists) {
            throw new DuplicateResourceException("Duplicate ticket for same customer/movie/type");
        }
    }

    private TicketResponse toResponse(TicketBase t) {
        return new TicketResponse(
                t.getId(),
                t.getCustomerId(),
                t.getMovieId(),
                t.getType(),
                t.getBasePrice(),
                t.getFinalPrice()
        );
    }
}