package com.avdhoot.StudyGroupFinderAPI.model.dto.groupDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroupResponseDto {

    @NotBlank
    private String name;

    @NotBlank
    private String subject;
    private String field;
    private String description;

    @Min(1)
    private Integer maxMembers;
    private String tags;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
}
