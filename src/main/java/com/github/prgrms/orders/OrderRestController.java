package com.github.prgrms.orders;

import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.github.prgrms.configures.web.Pageable;
import com.github.prgrms.utils.ApiUtils.ApiResult;

@RestController
@RequestMapping("api/orders")
public class OrderRestController {

    private final OrdersService ordersService;

    public OrderRestController(OrdersService ordersService) {this.ordersService = ordersService;}

    @GetMapping
    public ApiResult<List<OrdersDto>> findAll(Pageable pageable) {
        return ordersService.findAll(pageable);
    }


    @GetMapping("/{id}")
    public ApiResult<OrdersDto> findById(@PathVariable Long id) {
        return ordersService.findById(id);
    }

    @PatchMapping("/{id}/accept")
    public ApiResult<Boolean> accept(@PathVariable Long id) {
        return ordersService.accept(id);
    }

    @PatchMapping("/{id}/reject")
    public ApiResult<Boolean> reject(@PathVariable Long id, @Valid @RequestBody RejectRequestBody request) {
        return ordersService.reject(id, request.getMessage());
    }

    @PatchMapping("/{id}/shipping")
    public ApiResult<Boolean> shipping(@PathVariable Long id) {
        return ordersService.shipping(id);
    }

    @PatchMapping("/{id}/complete")
    public ApiResult<Boolean> complete(@PathVariable Long id) {
        return ordersService.complete(id);
    }
}