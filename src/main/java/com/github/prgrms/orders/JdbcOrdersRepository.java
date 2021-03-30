package com.github.prgrms.orders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.github.prgrms.configures.web.Pageable;

import static com.github.prgrms.utils.DateTimeUtils.dateTimeOf;
import static java.util.Optional.ofNullable;

@Repository
public class JdbcOrdersRepository implements OrdersRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrdersRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Orders> findAll(Pageable pageable) {
        return jdbcTemplate.query(
                "select o.*, r.user_seq as review_user_seq, r.product_seq as review_product_seq, r.content as review_content, r.create_at as review_create_at from orders o left join reviews r on o.review_seq = r.seq order by o.seq desc limit ?, ?",
                mapper,
                pageable.getOffset(),
                pageable.getSize()
        );
    }

    @Override
    public Optional<Orders> findById(long id) {
        List<Orders> results = jdbcTemplate.query(
                "select o.*, r.user_seq as review_user_seq, r.product_seq as review_product_seq, r.content as review_content, r.create_at as review_create_at from orders o left join reviews r on o.review_seq = r.seq where o.seq=?",
                mapper,
                id
        );
        return ofNullable(results.isEmpty() ? null : results.get(0));
    }

    @Override
    public void updateState(long id, StateType state) {
        this.updateState(id, state, null);
    }

    @Override
    public void updateState(long id, StateType state, String rejectMsg) {
        LocalDateTime rejectedAt = null;
        LocalDateTime completedAt = null;
        if (state == StateType.REJECTED) {
            rejectedAt = LocalDateTime.now();
        }
        if (state == StateType.COMPLETED) {
            completedAt = LocalDateTime.now();
        }
        jdbcTemplate.update("update orders set `state` = ?, reject_msg=?, rejected_at=?, completed_at=? where seq=?",
                state.toString(),
                rejectMsg,
                rejectedAt,
                completedAt,
                id);
    }

    @Override
    public void updateReviewSeq(long id, long reviewSeq) {
        jdbcTemplate.update("update orders set review_seq =? where seq=?", reviewSeq, id);
    }

    static RowMapper<Orders> mapper = ((rs, rowNum) -> {
        Review review;
        Long review_seq = rs.getLong("review_seq");
        if (rs.wasNull()) {
            review = null;
        } else {
            review = new Review.Builder()
                    .seq(review_seq)
                    .userSeq(rs.getLong("review_user_seq"))
                    .productSeq(rs.getLong("review_product_seq"))
                    .content(rs.getString("review_content"))
                    .createAt(dateTimeOf(rs.getTimestamp("review_create_at")))
                    .build();
        }
        return new Orders.Builder()
                .seq(rs.getLong("seq"))
                .userSeq(rs.getLong("user_seq"))
                .productSeq(rs.getLong("product_seq"))
                .review(review)
                .state(StateType.valueOf(rs.getString("state")))
                .requestMsg(rs.getString("request_msg"))
                .rejectMsg(rs.getString("reject_msg"))
                .completedAt(dateTimeOf(rs.getTimestamp("completed_at")))
                .rejectedAt(dateTimeOf(rs.getTimestamp("rejected_at")))
                .createAt(dateTimeOf(rs.getTimestamp("create_at")))
                .build();
    });
}