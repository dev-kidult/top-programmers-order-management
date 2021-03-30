package com.github.prgrms.orders;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Orders {
    private final Long seq;
    private Long userSeq;
    private Long productSeq;
    private Review review;
    private StateType state;
    private String requestMsg;
    private String rejectMsg;
    private LocalDateTime completedAt;
    private LocalDateTime rejectedAt;
    private final LocalDateTime createAt;

    public Orders(Long userSeq, Long productSeq, String requestMsg) {
        this(null, userSeq, productSeq, null, StateType.REQUESTED, requestMsg, null, null, null, null);
    }

    public Orders(Long seq, Long userSeq, Long productSeq, Review review, StateType state, String requestMsg, String rejectMsg,
            LocalDateTime completedAt, LocalDateTime rejectedAt, LocalDateTime createAt) {
        this.seq = seq;
        this.userSeq = userSeq;
        this.productSeq = productSeq;
        this.review = review;
        this.state = state;
        this.requestMsg = requestMsg;
        this.rejectMsg = rejectMsg;
        this.completedAt = completedAt;
        this.rejectedAt = rejectedAt;
        this.createAt = defaultIfNull(createAt, now());
    }

    public Long getSeq() {
        return seq;
    }

    public Long getUserSeq() {
        return userSeq;
    }

    public void setUserSeq(Long userSeq) {
        checkArgument(userSeq != null,
                "userSeq must be provided");
        this.userSeq = userSeq;
    }

    public Long getProductSeq() {
        return productSeq;
    }

    public void setProductSeq(Long productSeq) {
        checkArgument(productSeq != null,
                "productSeq must be provided");
        this.productSeq = productSeq;
    }

    public Optional<Review> getReview() {
        return ofNullable(review);
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        checkArgument(state != null,
                "state must be provided");
        this.state = state;
    }

    public Optional<String> getRequestMsg() {
        return ofNullable(requestMsg);
    }

    public void setRequestMsg(String requestMsg) {
        checkArgument(
                isEmpty(requestMsg) || requestMsg.length() <= 1000,
                "requestMsg length must be less than 1000 characters"
        );
        this.requestMsg = requestMsg;
    }

    public Optional<String> getRejectMsg() {
        return ofNullable(rejectMsg);
    }

    public void setRejectMsg(String rejectMsg) {
        checkArgument(
                isEmpty(rejectMsg) || rejectMsg.length() <= 1000,
                "rejectMsg length must be less than 1000 characters"
        );
        this.rejectMsg = rejectMsg;
    }

    public Optional<LocalDateTime> getCompletedAt() {
        return ofNullable(completedAt);
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Optional<LocalDateTime> getRejectedAt() {
        return ofNullable(rejectedAt);
    }

    public void setRejectedAt(LocalDateTime rejectedAt) {
        this.rejectedAt = rejectedAt;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Orders orders = (Orders) o;
        return Objects.equals(seq, orders.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("seq", seq)
                .append("userSeq", userSeq)
                .append("productSeq", productSeq)
                .append("review", review)
                .append("state", state)
                .append("requestMsg", requestMsg)
                .append("rejectMsg", rejectMsg)
                .append("completedAt", completedAt)
                .append("rejectedAt", rejectedAt)
                .append("createAt", createAt)
                .toString();
    }

    static public class Builder {
        private Long seq;
        private Long userSeq;
        private Long productSeq;
        private Review review;
        private StateType state;
        private String requestMsg;
        private String rejectMsg;
        private LocalDateTime completedAt;
        private LocalDateTime rejectedAt;
        private LocalDateTime createAt;

        public Builder() {/*empty*/}

        public Builder(Orders orders) {
            this.seq = orders.seq;
            this.userSeq = orders.userSeq;
            this.productSeq = orders.productSeq;
            this.review = orders.review;
            this.state = orders.state;
            this.requestMsg = orders.requestMsg;
            this.rejectMsg = orders.rejectMsg;
            this.completedAt = orders.completedAt;
            this.rejectedAt = orders.rejectedAt;
            this.createAt = orders.createAt;
        }

        public Builder seq(Long seq) {
            this.seq = seq;
            return this;
        }

        public Builder userSeq(Long userSeq) {
            this.userSeq = userSeq;
            return this;
        }

        public Builder productSeq(Long productSeq) {
            this.productSeq = productSeq;
            return this;
        }

        public Builder review(Review review) {
            this.review = review;
            return this;
        }

        public Builder state(StateType state) {
            this.state = state;
            return this;
        }

        public Builder requestMsg(String requestMsg) {
            this.requestMsg = requestMsg;
            return this;
        }

        public Builder rejectMsg(String rejectMsg) {
            this.rejectMsg = rejectMsg;
            return this;
        }

        public Builder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public Builder rejectedAt(LocalDateTime rejectedAt) {
            this.rejectedAt = rejectedAt;
            return this;
        }

        public Builder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public Orders build() {
            return new Orders(
                    seq,
                    userSeq,
                    productSeq,
                    review,
                    state,
                    requestMsg,
                    rejectMsg,
                    completedAt,
                    rejectedAt,
                    createAt
            );
        }
    }
}
