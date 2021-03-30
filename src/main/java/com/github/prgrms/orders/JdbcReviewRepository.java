package com.github.prgrms.orders;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcReviewRepository implements ReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Review review) {
        jdbcTemplate.update("insert into reviews(user_seq, product_seq, content, create_at) values(?,?,?,?)",
                review.getUserSeq(),
                review.getProductSeq(),
                review.getContent(),
                Timestamp.valueOf(review.getCreateAt())
        );
    }

    @Override
    public Long findLastSeq() {
        List<Long> query = jdbcTemplate.query("select seq from reviews order by seq desc", (rs, rowNum) -> rs.getLong(1));
        return query.isEmpty() ? null : query.get(0);
    }
}