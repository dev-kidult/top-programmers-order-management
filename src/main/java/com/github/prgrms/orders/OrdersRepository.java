package com.github.prgrms.orders;

import java.util.List;
import java.util.Optional;

import com.github.prgrms.configures.web.Pageable;

public interface OrdersRepository {
    List<Orders> findAll(Pageable pageable);

    Optional<Orders> findById(long id);

    void updateState(long id, StateType state);

    void updateState(long id, StateType state, String rejectMsg);

    void updateReviewSeq(long id, long reviewSeq);
}