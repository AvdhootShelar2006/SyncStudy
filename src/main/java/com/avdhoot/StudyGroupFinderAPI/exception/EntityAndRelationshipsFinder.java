package com.avdhoot.StudyGroupFinderAPI.exception;

import com.avdhoot.StudyGroupFinderAPI.entity.Query;
import com.avdhoot.StudyGroupFinderAPI.entity.User;
import com.avdhoot.StudyGroupFinderAPI.entity.Group;
import com.avdhoot.StudyGroupFinderAPI.repository.UserRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.groupRepository.GroupRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.queryRepository.GroupQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityAndRelationshipsFinder {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupQueryRepository groupQueryRepository;

    public Group getGroupOrThrow(int groupId){
        return groupRepository.findById(groupId).orElseThrow(()-> new ResourceNotFoundException("Group with ID: " + groupId + " not found."));
    }

    public User getUserOrThrow(int userId){
        return userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User with ID: " + userId + " not found."));
    }

    public Query getQueryInGroupOrThrow(int groupId, int queryId) {
        Query query = groupQueryRepository.findById(queryId)
                .orElseThrow(() -> new ResourceNotFoundException("Query with ID: " + queryId + " not found"));

        if (!query.getGroup().getId().equals(groupId)) {
            throw new ResourceNotFoundException("Group with ID: " + groupId + " does not contain this query");
        }
        return query;
    }
}
