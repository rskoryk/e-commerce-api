package projects.projectTwo.e_commerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import projects.projectTwo.e_commerce.cart.dto.AddCartItemRequest;
import projects.projectTwo.e_commerce.cart.dto.UpdateCartItemRequest;
import projects.projectTwo.e_commerce.cart.entity.Cart;
import projects.projectTwo.e_commerce.cart.entity.CartItem;
import projects.projectTwo.e_commerce.cart.repository.CartRepository;
import projects.projectTwo.e_commerce.cart.service.impl.CartServiceImpl;
import projects.projectTwo.e_commerce.common.exceptions.InsufficientStockException;
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

public class CartServiceImplTest {

    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CartServiceImpl cartService;
    private User user;

    @BeforeEach
    void setUp(){
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        cartRepository = mock(CartRepository.class);
        UserRepository userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        cartService = new CartServiceImpl(userRepository, cartRepository, productRepository);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser123");

        SecurityContextHolder.setContext(securityContext);

        user = User.builder()
                .username("testuser123")
                .email("testuser123@gmail.com")
                .password("test")
                .orders(new ArrayList<>())
                .emailVerified(true)
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.findByUsername("testuser123")).thenReturn(Optional.of(user));
    }

    private AddCartItemRequest getAddCartItemRequest(){
        return AddCartItemRequest.builder()
                .productId(1L)
                .quantity(1)
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

    private CartItem createCartItem(Cart cart, int quantity, int stock, BigDecimal price){
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
        return cartItem;
    }

    private Cart createEmptyCart(){
        Cart cart = Cart.builder()
                .user(user)
                .items(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .build();

        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        return cart;
    }

    @Test
    public void testAddItem_successfullyAddsNewItem(){
        AddCartItemRequest request = getAddCartItemRequest();
        Product product = createProduct(1, BigDecimal.valueOf(100));
        Cart cart = createEmptyCart();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        cartService.addItem(request);

        CartItem cartItem = cart.getItems().getFirst();

        BigDecimal hundred = BigDecimal.valueOf(100);

        assertEquals(hundred, cartItem.getPrice());
        assertEquals(hundred, cartItem.getTotal());
        assertEquals(1, cartItem.getQuantity());
        assertEquals(product, cartItem.getProduct());
        assertEquals(1, cart.getItems().size());
        assertEquals(hundred, cart.getTotalAmount());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testAddItem_successfullyUpdatesExistingItem(){
        AddCartItemRequest request = getAddCartItemRequest();
        Cart cart = createEmptyCart();
        CartItem cartItem = createCartItem(cart, 1, 3, BigDecimal.valueOf(100));

        when(productRepository.findById(1L)).thenReturn(Optional.of(cartItem.getProduct()));

        cartService.addItem(request);

        assertEquals(2, cartItem.getQuantity());
        assertEquals(BigDecimal.valueOf(200), cartItem.getTotal());
        assertEquals(BigDecimal.valueOf(200), cart.getTotalAmount());
        assertEquals(1, cart.getItems().size());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testRemoveItem_successfullyRemovesItem(){
        Cart cart = createEmptyCart();
        createCartItem(cart, 1, 3, BigDecimal.valueOf(100));

        cartService.removeItem(1L);

        assertEquals(0, cart.getItems().size());
        assertEquals(BigDecimal.ZERO, cart.getTotalAmount());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testUpdateItem_throwsException_whenInsufficientStock(){
        Cart cart = createEmptyCart();
        CartItem cartItem = createCartItem(cart, 2, 3, BigDecimal.valueOf(100));

        UpdateCartItemRequest request = UpdateCartItemRequest.builder()
                .quantity(4)
                .build();

        assertThrows(InsufficientStockException.class, () -> cartService.updateItem(1L, request));
        verify(cartRepository, never()).save(any(Cart.class));
        assertEquals(2, cartItem.getQuantity());
        assertEquals(BigDecimal.valueOf(200), cartItem.getTotal());
        assertEquals(BigDecimal.valueOf(200), cart.getTotalAmount());
    }
}
