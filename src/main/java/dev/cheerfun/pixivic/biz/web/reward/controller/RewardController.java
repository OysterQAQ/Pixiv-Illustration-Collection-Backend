package dev.cheerfun.pixivic.biz.web.reward.controller;

import dev.cheerfun.pixivic.basic.auth.annotation.PermissionRequired;
import dev.cheerfun.pixivic.basic.auth.constant.PermissionLevel;
import dev.cheerfun.pixivic.basic.event.constant.ActionType;
import dev.cheerfun.pixivic.basic.event.constant.ObjectType;
import dev.cheerfun.pixivic.basic.event.domain.Event;
import dev.cheerfun.pixivic.basic.event.publisher.EventPublisher;
import dev.cheerfun.pixivic.basic.ratelimit.annotation.RateLimit;
import dev.cheerfun.pixivic.biz.web.comment.po.Comment;
import dev.cheerfun.pixivic.biz.web.reward.po.Reward;
import dev.cheerfun.pixivic.biz.web.reward.service.RewardService;
import dev.cheerfun.pixivic.common.constant.AuthConstant;
import dev.cheerfun.pixivic.common.context.AppContext;
import dev.cheerfun.pixivic.common.po.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2020/10/15 7:13 PM
 * @description RewardController
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RewardController {
    private final RewardService rewardService;
    private final EventPublisher eventPublisher;

    @PostMapping("/{appType}/{appId}/rewards")
    @PermissionRequired
    public ResponseEntity<Result<String>> pushReward(@PathVariable String appType, @PathVariable int appId, @RequestBody Reward reward, @RequestHeader(AuthConstant.AUTHORIZATION) String token) {
        int userId = (int) AppContext.get().get(AuthConstant.USER_ID);
        reward.init(userId);
        rewardService.pushReward(reward);
        eventPublisher.publish(new Event(userId, ActionType.REWARD, appType, appId, LocalDateTime.now()));
        return ResponseEntity.ok().body(new Result<>("打赏成功"));
    }

    @GetMapping("/{appType}/{appId}/rewards")
    public ResponseEntity<Result<List<Reward>>> pullReward(@PathVariable String appType, @PathVariable int appId, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return ResponseEntity.ok().body(new Result<>("拉取打赏信息成功", rewardService.pullRewardCount(appType, appId), rewardService.pullReward(appType, appId, page, pageSize)));
    }

}
