package org.example.endtermprojectapi.cache;

import org.example.endtermprojectapi.dto.TicketResponse;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class TicketCache {

    private TicketCache() {}

    private static class Holder {
        private static final TicketCache INSTANCE = new TicketCache();
    }

    public static TicketCache getInstance() {
        return Holder.INSTANCE;
    }

    private volatile List<TicketResponse> allTicketsCache; // cache for getAll()
    private final Map<Long, TicketResponse> byIdCache = new ConcurrentHashMap<>();

    public List<TicketResponse> getAllTickets() {
        return allTicketsCache;
    }

    public void putAllTickets(List<TicketResponse> tickets) {
        this.allTicketsCache = List.copyOf(tickets);
    }

    public void invalidateAllTickets() {
        this.allTicketsCache = null;
    }

    public TicketResponse getById(Long id) {
        return byIdCache.get(id);
    }

    public void putById(Long id, TicketResponse ticket) {
        if (id != null && ticket != null) {
            byIdCache.put(id, ticket);
        }
    }

    public void invalidateById(Long id) {
        if (id != null) {
            byIdCache.remove(id);
        }
    }

    public void clear() {
        allTicketsCache = null;
        byIdCache.clear();
    }
}