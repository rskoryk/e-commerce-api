package projects.projectTwo.e_commerce.review.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank
    @Size(min = 10, max = 2000)
    private String content;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    @NotNull
    private Long productId;
}
