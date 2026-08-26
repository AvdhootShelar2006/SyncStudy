package com.avdhoot.StudyGroupFinderAPI.mapper;

import com.avdhoot.StudyGroupFinderAPI.dto.answerQuery.AnswerQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.dto.queryDto.GroupQueryResponse;
import com.avdhoot.StudyGroupFinderAPI.entity.AnswerQuery;
import com.avdhoot.StudyGroupFinderAPI.entity.GroupQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QueryMapper {

    @Mapping(source = "postedBy.name", target = "postedByName")
    @Mapping(source = "studyGroup.id", target = "groupId")
    GroupQueryResponse toGroupQueryResponse(GroupQuery query);


    @Mapping(source = "studyGroup.id", target = "groupId")
    @Mapping(source = "postedBy.name", target = "postedByName")
    List<GroupQueryResponse> toGroupQueryResponses(List<GroupQuery> groupQueries);


    @Mapping(source = "user.name", target = "answeredByName")
    @Mapping(source = "groupQuery.title", target = "queryTitle")
    AnswerQueryResponse toAnswerQueryResponse(AnswerQuery answerQuery);

    List<AnswerQueryResponse> toAnswerQueryResponses(List<AnswerQuery> answerQueries);
}
