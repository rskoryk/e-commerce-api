package projects.projectTwo.e_commerce.review.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewUpdateRequest {
    @NotBlank
    @Size(min = 10, max = 2000)
    private String content;
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
}
