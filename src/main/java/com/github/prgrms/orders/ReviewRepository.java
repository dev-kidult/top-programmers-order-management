package com.github.prgrms.orders;

public interface ReviewRepository {
  void save(Review review);
  Long findLastSeq();
}