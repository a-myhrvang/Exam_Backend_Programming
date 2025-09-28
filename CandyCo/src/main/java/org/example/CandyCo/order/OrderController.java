package org.example.CandyCo.order;

import org.example.CandyCo.product.Product;
import org.example.CandyCo.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    // Endpoint for å vise alle ordre i Thymeleaf
    @GetMapping
    public String getOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "orders";
    }

    // Endpoint for å hente alle ordre som JSON
    @GetMapping("/api")
    @ResponseBody
    public List<Order> getAllOrdersAsJson() {
        return orderService.getAllOrders();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping("/api")
    @ResponseBody
    public Order createOrder(@RequestBody Order order) {
        return orderService.saveOrder(order);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public Product updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productService.updateProduct(id, updatedProduct);
    }
}
