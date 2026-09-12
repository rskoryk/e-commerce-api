package projects.projectTwo.e_commerce.cart.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projects.projectTwo.e_commerce.cart.dto.AddCartItemRequest;
import projects.projectTwo.e_commerce.cart.dto.CartResponse;
import projects.projectTwo.e_commerce.cart.dto.UpdateCartItemRequest;
import projects.projectTwo.e_commerce.cart.service.CartService;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<Void> addItem(@Valid @RequestBody AddCartItemRequest request){
        cartService.addItem(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeItem(@PathVariable Long id){
        cartService.removeItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(){
        return ResponseEntity.ok(cartService.getCart());
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Void> updateItem(@PathVariable Long id, @Valid @RequestBody UpdateCartItemRequest request){
        cartService.updateItem(id, request);
        return ResponseEntity.ok().build();
    }
}
