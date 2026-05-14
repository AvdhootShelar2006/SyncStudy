package com.avdhoot.StudyGroupFinderAPI.aop;

import com.avdhoot.StudyGroupFinderAPI.model.dto.group_member_dto.JoinLeaveRequest;
import com.avdhoot.StudyGroupFinderAPI.model.entities.ActionType;
import com.avdhoot.StudyGroupFinderAPI.model.entities.AuditLog;
import com.avdhoot.StudyGroupFinderAPI.model.entities.Member;
import com.avdhoot.StudyGroupFinderAPI.repository.AuditLogRepository;
import com.avdhoot.StudyGroupFinderAPI.repository.MemberRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditLoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLoggingAspect.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuditLogRepository repository;


    @AfterReturning( pointcut = "execution (* com.avdhoot.StudyGroupFinderAPI.service.GroupService.joinGroup(..))")
    public void logSuccessfulJoin(JoinPoint joinPoint){
        LOGGER.info("Member added: " + joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();

        Integer dynamicGroupId = (Integer) args[0];

        JoinLeaveRequest request = (JoinLeaveRequest) args[1];

        Member member = memberRepository
                .findById((request.memberId()))
                .orElseThrow(() -> new RuntimeException());

        AuditLog log = new AuditLog();

        log.setUsername(member.getName());
        log.setGroup_Id(dynamicGroupId);
        log.setActionType(ActionType.JOIN_GROUP);
        log.setTimestamp(LocalDateTime.now());

        repository.save(log);
    }

    @AfterReturning(pointcut = "execution (* com.avdhoot.StudyGroupFinderAPI.service.GroupService.leaveGroup(..))")
    public void logSuccessfulLeave(JoinPoint joinPoint){
        LOGGER.info("Member left: " + joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();

        int dynamicGroupId = (Integer) args[0];

        JoinLeaveRequest request = (JoinLeaveRequest) args[1];

        Member member = memberRepository
                .findById(request.memberId())
                .orElseThrow(()-> new RuntimeException());

        AuditLog log = new AuditLog();

        log.setUsername(member.getName());
        log.setGroup_Id(dynamicGroupId);
        log.setActionType(ActionType.LEAVE_GROUP);
        log.setTimestamp(LocalDateTime.now());

        repository.save(log);

    }

}
