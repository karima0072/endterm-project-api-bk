package org.example.endtermprojectapi.repository;

import org.example.endtermprojectapi.factory.TicketFactory;
import org.example.endtermprojectapi.model.TicketBase;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TicketFactory factory;

    public TicketRepository(JdbcTemplate jdbcTemplate, TicketFactory factory) {
        this.jdbcTemplate = jdbcTemplate;
        this.factory = factory;
    }

    public List<TicketBase> findAll() {
        String sql = "SELECT id, customer_id, movie_id, type, base_price, final_price FROM tickets ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                factory.createFromDb(
                        rs.getLong("id"),
                        rs.getLong("customer_id"),
                        rs.getLong("movie_id"),
                        rs.getString("type"),
                        rs.getDouble("base_price"),
                        rs.getDouble("final_price")
                )
        );
    }

    public TicketBase findById(Long id) throws EmptyResultDataAccessException {
        String sql = "SELECT id, customer_id, movie_id, type, base_price, final_price FROM tickets WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) ->
                factory.createFromDb(
                        rs.getLong("id"),
                        rs.getLong("customer_id"),
                        rs.getLong("movie_id"),
                        rs.getString("type"),
                        rs.getDouble("base_price"),
                        rs.getDouble("final_price")
                ), id
        );
    }

    public TicketBase save(TicketBase ticket) {
        String sql = """
                INSERT INTO tickets(customer_id, movie_id, type, base_price, final_price)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                ticket.getCustomerId(),
                ticket.getMovieId(),
                ticket.getType(),
                ticket.getBasePrice(),
                ticket.getFinalPrice()
        );

        if (id == null) {
            throw new IllegalStateException("DB did not return id");
        }

        return factory.createFromDb(
                id,
                ticket.getCustomerId(),
                ticket.getMovieId(),
                ticket.getType(),
                ticket.getBasePrice(),
                ticket.getFinalPrice()
        );
    }

    // ✅ UPDATE
    public TicketBase update(Long id, TicketBase ticket) {
        String sql = """
                UPDATE tickets
                SET customer_id = ?, movie_id = ?, type = ?, base_price = ?, final_price = ?
                WHERE id = ?
                """;

        int updated = jdbcTemplate.update(
                sql,
                ticket.getCustomerId(),
                ticket.getMovieId(),
                ticket.getType(),
                ticket.getBasePrice(),
                ticket.getFinalPrice(),
                id
        );

        if (updated == 0) {
            throw new EmptyResultDataAccessException(1);
        }

        // вернуть обновлённую запись как объект
        return findById(id);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM tickets WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}