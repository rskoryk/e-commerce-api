package projects.projectTwo.e_commerce.order.service.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import projects.projectTwo.e_commerce.cart.entity.Cart;
import projects.projectTwo.e_commerce.cart.entity.CartItem;
import projects.projectTwo.e_commerce.cart.repository.CartRepository;
import projects.projectTwo.e_commerce.common.exceptions.CustomAccessDeniedException;
import projects.projectTwo.e_commerce.common.exceptions.CustomIllegalStateException;
import projects.projectTwo.e_commerce.common.exceptions.InsufficientStockException;
import projects.projectTwo.e_commerce.common.exceptions.ResourceNotFoundException;
import projects.projectTwo.e_commerce.order.dto.OrderResponse;
import projects.projectTwo.e_commerce.order.entity.Order;
import projects.projectTwo.e_commerce.order.entity.OrderItem;
import projects.projectTwo.e_commerce.order.enums.OrderStatus;
import projects.projectTwo.e_commerce.order.mapper.OrderItemMapper;
import projects.projectTwo.e_commerce.order.mapper.OrderMapper;
import projects.projectTwo.e_commerce.order.repository.OrderRepository;
import projects.projectTwo.e_commerce.order.service.OrderService;
import projects.projectTwo.e_commerce.product.entity.Product;
import projects.projectTwo.e_commerce.product.repository.ProductRepository;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .totalAmount(BigDecimal.ZERO)
                                .items(new ArrayList<>())
                                .build()
                ));
    }

    @Transactional
    @Override
    public OrderResponse checkout() {
        User user = getCurrentUser();
        Cart cart = getOrCreateCart(user);
        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) throw new CustomIllegalStateException("Cart is empty");

        for (CartItem cartItem : cartItems){
            if (cartItem.getQuantity() > cartItem.getProduct().getStock()) {
                throw new InsufficientStockException("Insufficient stock");
            }
        }

        Order order = Order.builder()
                .status(OrderStatus.CREATED)
                .totalAmount(cart.getTotalAmount())
                .user(cart.getUser())
                .build();
        order.setOrderItems(cartItems.stream().map(e -> OrderItemMapper.mapCartItemToOrderItem(e, order)).toList());
        for (CartItem cartItem : cartItems){
            Product product = cartItem.getProduct();
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }
        cartRepository.delete(cart);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapOrderToOrderResponse(savedOrder);
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(getCurrentUser().getId())) throw new CustomAccessDeniedException("Access denied");
        if (order.getStatus() != OrderStatus.CREATED) throw new CustomIllegalStateException("Order cannot be cancelled");

        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems){
            Product product = orderItem.getProduct();
            product.setStock(orderItem.getQuantity() + product.getStock());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public List<OrderResponse> getMyOrders() {
        List<Order> orders = orderRepository.findAllByUser(getCurrentUser());
        return orders.stream().map(OrderMapper::mapOrderToOrderResponse).toList();
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (!order.getUser().getId().equals(getCurrentUser().getId())) throw new CustomAccessDeniedException("Access denied");
        return OrderMapper.mapOrderToOrderResponse(order);
    }
}
