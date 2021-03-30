package com.github.prgrms.orders;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.prgrms.configures.web.Pageable;
import com.github.prgrms.errors.NotFoundException;

import static com.github.prgrms.utils.ApiUtils.*;
import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public OrdersService(OrdersRepository ordersRepository) {this.ordersRepository = ordersRepository;}

    public ApiResult<List<OrdersDto>> findAll(Pageable pageable) {
        return success(ordersRepository.findAll(pageable).stream()
                .map(OrdersDto::new)
                .collect(Collectors.toList()));
    }

    public ApiResult<OrdersDto> findById(Long id) {
        checkNotNull(id, "orderId must be provided");
        return success(ordersRepository.findById(id)
                .map(OrdersDto::new)
                .orElseThrow(() -> new NotFoundException("Could not found order for " + id)));
    }

    public ApiResult<Boolean> accept(Long id) {
        boolean result = false;
        try {
            ApiResult<OrdersDto> order = findById(id);
            if (order.getResponse().getState() == StateType.REQUESTED) {
                ordersRepository.updateState(id, StateType.ACCEPTED);
                result = true;
            }
        } catch (NotFoundException ignored) {
        }
        return success(result);
    }

    public ApiResult<Boolean> reject(Long id, String rejectMsg) {
        boolean result = false;
        try {
            ApiResult<OrdersDto> order = findById(id);
            if (order.getResponse().getState() == StateType.REQUESTED) {
                ordersRepository.updateState(id, StateType.REJECTED, rejectMsg);
                result = true;
            }
        } catch (NotFoundException ignored) {
        }
        return success(result);
    }

    public ApiResult<Boolean> shipping(Long id) {
        boolean result = false;
        try {
            ApiResult<OrdersDto> order = findById(id);
            if (order.getResponse().getState() == StateType.ACCEPTED) {
                ordersRepository.updateState(id, StateType.SHIPPING);
                result = true;
            }
        } catch (NotFoundException ignored) {
        }
        return success(result);
    }

    public ApiResult<Boolean> complete(Long id) {
        boolean result = false;
        try {
            ApiResult<OrdersDto> order = findById(id);
            if (order.getResponse().getState() == StateType.SHIPPING) {
                ordersRepository.updateState(id, StateType.COMPLETED);
                result = true;
            }
        } catch (NotFoundException ignored) {
        }
        return success(result);
    }

    public void updateReviewSeq(Long id, Long reviewSeq) {
        checkNotNull(id, "orderId must be provided");
        checkNotNull(reviewSeq, "reviewId must be provided");
        ordersRepository.updateReviewSeq(id, reviewSeq);
    }

}
