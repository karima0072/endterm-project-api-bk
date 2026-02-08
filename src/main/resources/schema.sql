CREATE TABLE IF NOT EXISTS tickets (
                                       id BIGSERIAL PRIMARY KEY,
                                       customer_id BIGINT NOT NULL,
                                       movie_id BIGINT NOT NULL,
                                       type VARCHAR(50) NOT NULL,
    base_price DOUBLE PRECISION NOT NULL,
    final_price DOUBLE PRECISION NOT NULL
    );