package projects.projectTwo.e_commerce.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private Long id;
    private Long userId;
    private String username;
    private String content;
    private Integer rating;
}
