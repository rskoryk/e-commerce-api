package projects.projectTwo.e_commerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import projects.projectTwo.e_commerce.cart.entity.Cart;
import projects.projectTwo.e_commerce.cart.entity.CartItem;
import projects.projectTwo.e_commerce.cart.repository.CartRepository;
import projects.projectTwo.e_commerce.common.exceptions.CustomIllegalStateException;
import projects.projectTwo.e_commerce.common.exceptions.InsufficientStockException;
import projects.projectTwo.e_commerce.order.entity.Order;
import projects.projectTwo.e_commerce.order.entity.OrderItem;
import projects.projectTwo.e_commerce.order.enums.OrderStatus;
import projects.projectTwo.e_commerce.order.repository.OrderRepository;
import projects.projectTwo.e_commerce.order.service.impl.OrderServiceImpl;
import projects.projectTwo.e_commerce.product.entity.Category;
import projects.projectTwo.e_commerce.product.entity.Product;
import projects.projectTwo.e_commerce.product.repository.ProductRepository;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.enums.Role;
import projects.projectTwo.e_commerce.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderServiceImpl orderService;
    private Authentication authentication;
    private SecurityContext securityContext;
    private User user;

    @BeforeEach
    void setUp(){
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        userRepository = mock(UserRepository.class);
        cartRepository = mock(CartRepository.class);
        orderRepository = mock(OrderRepository.class);
        productRepository = mock(ProductRepository.class);
        orderService = new OrderServiceImpl(userRepository, cartRepository, orderRepository, productRepository);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser123");

        SecurityContextHolder.setContext(securityContext);

        user = User.builder()
                .id(1L)
                .username("testuser123")
                .email("testuser123@gmail.com")
                .password("test")
                .orders(new ArrayList<>())
                .blocked(false)
                .emailVerified(true)
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("testuser123")).thenReturn(Optional.of(user));
    }

    private Cart createEmptyCart(){
        return Cart.builder()
                .user(user)
                .items(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .build();
    }

    private Product createProduct(int stock, BigDecimal price){
        return Product.builder()
                .id(1L)
                .name("test")
                .description("test")
                .price(price)
                .stock(stock)
                .category(Category.builder()
                        .name("test")
                        .build())
                .build();
    }

    private void createCartItem(Cart cart, int quantity, int stock, BigDecimal price){
        Product product = createProduct(stock, price);
        CartItem cartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .cart(cart)
                .total(price.multiply(BigDecimal.valueOf(quantity)))
                .quantity(quantity)
                .price(price)
                .build();
        cart.getItems().add(cartItem);
        cart.setTotalAmount(cartItem.getTotal());
    }


    @Test
    public void testCheckout_successfullyCreatesOrder(){
        Cart cart = createEmptyCart();
        createCartItem(cart, 2, 3, BigDecimal.valueOf(100));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        orderService.checkout();

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

        verify(orderRepository, times(1)).save(captor.capture());

        Order order = captor.getValue();

        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(user, order.getUser());
        assertEquals(BigDecimal.valueOf(200), order.getTotalAmount());
    }

    @Test
    public void testCheckout_throwsException_whenInsufficientStock(){
        Cart cart = createEmptyCart();
        createCartItem(cart, 4, 2, BigDecimal.valueOf(100));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        assertThrows(InsufficientStockException.class, () -> orderService.checkout());
    }

    @Test
    public void testCheckout_throwsException_whenCartIsEmpty(){
        Cart cart = createEmptyCart();
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        assertThrows(CustomIllegalStateException.class, () -> orderService.checkout());
    }

    @Test
    public void testCancelOrder_successfullyCancelsOrder(){
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.CREATED)
                .totalAmount(BigDecimal.valueOf(100))
                .orderItems(new ArrayList<>())
                .build();

        Product product = createProduct(3, BigDecimal.valueOf(100));

        OrderItem orderItem = OrderItem.builder()
                .order(order)
                .product(product)
                .total(BigDecimal.valueOf(100))
                .quantity(1)
                .price(BigDecimal.valueOf(100))
                .build();

        order.getOrderItems().add(orderItem);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        verify(productRepository, times(1)).save(any(Product.class));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
