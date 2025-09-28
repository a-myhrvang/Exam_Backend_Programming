package org.example.CandyCo.order;

import org.example.CandyCo.error.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return orderRepository.findById(id).orElseThrow(() ->
                new OrderNotFoundException(id));
    }


    public Order saveOrder(Order order) {

        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one product");
        }

        if (order.getTotalPrice() <= 0) {
            throw new IllegalArgumentException("Total price must be greater than 0");
        }

        return orderRepository.save(order);
    }



    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException(id);
        }
        orderRepository.deleteById(id);
    }
}