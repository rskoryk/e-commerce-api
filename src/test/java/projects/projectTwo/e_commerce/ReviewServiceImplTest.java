package projects.projectTwo.e_commerce;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import projects.projectTwo.e_commerce.common.exceptions.CustomAccessDeniedException;
import projects.projectTwo.e_commerce.product.entity.Category;
import projects.projectTwo.e_commerce.product.entity.Product;
import projects.projectTwo.e_commerce.product.repository.ProductRepository;
import projects.projectTwo.e_commerce.review.dto.ReviewRequest;
import projects.projectTwo.e_commerce.review.dto.ReviewUpdateRequest;
import projects.projectTwo.e_commerce.review.entity.Review;
import projects.projectTwo.e_commerce.review.repository.ReviewRepository;
import projects.projectTwo.e_commerce.review.service.impl.ReviewServiceImpl;
import projects.projectTwo.e_commerce.user.entity.User;
import projects.projectTwo.e_commerce.user.enums.Role;
import projects.projectTwo.e_commerce.user.repository.UserRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReviewServiceImplTest {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ReviewRepository reviewRepository;
    private ReviewServiceImpl reviewService;
    private Authentication authentication;
    private SecurityContext securityContext;
    private User user;

    @BeforeEach
    void setUp(){
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        reviewRepository = mock(ReviewRepository.class);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        reviewService = new ReviewServiceImpl(userRepository, productRepository, reviewRepository);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser123");

        SecurityContextHolder.setContext(securityContext);

        user = User.builder()
                .id(1L)
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

    @Test
    public void testPost_successfullyCreatesReview(){
        ReviewRequest reviewRequest = ReviewRequest.builder()
                .productId(1L)
                .content("test")
                .rating(5)
                .build();

        Product product = Product.builder()
                .id(1L)
                .name("test")
                .description("test")
                .price(BigDecimal.valueOf(100))
                .stock(3)
                .category(Category.builder()
                        .name("test")
                        .build())
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reviewService.post(reviewRequest);

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);

        verify(reviewRepository, times(1)).save(captor.capture());

        Review review = captor.getValue();

        assertEquals(5, review.getRating());
        assertEquals("test", review.getContent());
        assertEquals(1L, review.getProduct().getId());
    }

    @Test
    public void testChange_throwsException_whenAccessDenied(){
        User anotherUser = User.builder()
                .id(2L)
                .username("testuser1234")
                .email("testuser1234@gmail.com")
                .password("test")
                .orders(new ArrayList<>())
                .emailVerified(true)
                .blocked(false)
                .role(Role.USER)
                .build();;

        Product product = Product.builder()
                .id(1L)
                .name("test")
                .description("test")
                .price(BigDecimal.valueOf(100))
                .stock(3)
                .category(Category.builder()
                        .name("test")
                        .build())
                .build();

        Review review = Review.builder()
                .id(1L)
                .user(anotherUser)
                .active(true)
                .content("test")
                .rating(5)
                .product(product)
                .build();

        ReviewUpdateRequest reviewUpdateRequest = ReviewUpdateRequest.builder()
                .content("test123")
                .rating(3)
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        assertThrows(CustomAccessDeniedException.class, () -> reviewService.change(1L, reviewUpdateRequest));
    }

    @Test
    public void testDelete_successfullyDeletesReview(){
        Product product = Product.builder()
                .id(1L)
                .name("test")
                .description("test")
                .price(BigDecimal.valueOf(100))
                .stock(3)
                .category(Category.builder()
                        .name("test")
                        .build())
                .build();

        Review review = Review.builder()
                .id(1L)
                .user(user)
                .active(true)
                .content("test")
                .rating(5)
                .product(product)
                .build();

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        reviewService.delete(1L);

        verify(reviewRepository, times(1)).delete(review);
    }
}
