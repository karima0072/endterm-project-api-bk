package org.example.endtermprojectapi.service;

import org.example.endtermprojectapi.cache.TicketCache;
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

    private final TicketCache cache = TicketCache.getInstance();

    public TicketService(TicketRepository repository) {
        this.repository = repository;
    }

    public List<TicketResponse> getAll() {
        List<TicketResponse> cached = cache.getAllTickets();
        if (cached != null) {
            return cached;
        }

        List<TicketResponse> fresh = repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        cache.putAllTickets(fresh);
        return fresh;
    }

    public TicketResponse getById(Long id) {
        TicketResponse cached = cache.getById(id);
        if (cached != null) return cached;

        try {
            TicketBase t = repository.findById(id);
            TicketResponse resp = toResponse(t);
            cache.putById(id, resp);
            return resp;
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
        TicketResponse resp = toResponse(saved);

        cache.invalidateAllTickets();
        cache.putById(resp.getId(), resp);

        return resp;
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
        TicketResponse resp = toResponse(updated);

        cache.invalidateAllTickets();
        cache.invalidateById(id);
        cache.putById(id, resp);

        return resp;
    }

    public void delete(Long id) {
        getById(id);
        repository.deleteById(id);

        cache.invalidateAllTickets();
        cache.invalidateById(id);
    }

    public void clearCache() {
        cache.clear();
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