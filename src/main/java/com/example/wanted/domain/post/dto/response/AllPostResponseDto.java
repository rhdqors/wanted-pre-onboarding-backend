package com.example.wanted.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(value = {
        "pageable",
        "sort",
        "numberOfElements",
        "empty"
})
public class AllPostResponseDto {

    private Long id;
    private String title;
}
