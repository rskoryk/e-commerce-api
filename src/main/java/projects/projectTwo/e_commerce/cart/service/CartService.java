package projects.projectTwo.e_commerce.cart.service;

import projects.projectTwo.e_commerce.cart.dto.AddCartItemRequest;
import projects.projectTwo.e_commerce.cart.dto.CartResponse;
import projects.projectTwo.e_commerce.cart.dto.UpdateCartItemRequest;

public interface CartService {
    void addItem(AddCartItemRequest request);
    void removeItem(Long cartItemId);
    CartResponse getCart();
    void updateItem(Long cartItemId, UpdateCartItemRequest request);
}
