package com.avdhoot.StudyGroupFinderAPI.model.dto.groupDto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String subject;
    private String field;
    private String description;

    @Min(1)
    private Integer maxMembers;
    private String tags;


}
