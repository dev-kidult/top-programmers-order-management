package com.github.prgrms.orders;

import org.springframework.stereotype.Service;

import com.github.prgrms.products.ProductService;
import com.github.prgrms.utils.ApiUtils.ApiResult;

import static com.github.prgrms.utils.ApiUtils.success;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrdersService ordersService;
    private final ProductService productService;

    public ReviewService(ReviewRepository reviewRepository, OrdersService ordersService, ProductService productService) {
        this.reviewRepository = reviewRepository;
        this.ordersService = ordersService;
        this.productService = productService;
    }

    public ApiResult<ReviewDto> review(Long userId, Long orderId, String content) {
        ApiResult<OrdersDto> order = ordersService.findById(orderId);
        if (order.getResponse().getReview() != null) {
            throw new IllegalStateException("Could not write review for order " + orderId + " because have already written");
        }
        if (order.getResponse().getState() != StateType.COMPLETED) {
            throw new IllegalStateException("Could not write review for order " + orderId + " because state(REQUESTED) is not allowed");
        }
        Review review = new Review(userId, order.getResponse().getProductId(), content);
        reviewRepository.save(review);
        Long seq = reviewRepository.findLastSeq();

        ordersService.updateReviewSeq(orderId, seq);
        productService.plusReviewCount(order.getResponse().getProductId());

        ReviewDto reviewDto = new ReviewDto(seq, review.getProductSeq(), review.getContent(), review.getCreateAt());
        return success(reviewDto);
    }
}
