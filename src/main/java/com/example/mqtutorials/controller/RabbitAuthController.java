package com.example.mqtutorials.controller;

import com.example.mqtutorials.controller.dto.request.ResourcePathRequest;
import com.example.mqtutorials.controller.dto.request.TopicPathRequest;
import com.example.mqtutorials.controller.dto.request.UserPathRequest;
import com.example.mqtutorials.controller.dto.request.VhostPathRequest;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rabbit/auth")
@RestController
public class RabbitAuthController {

    @GetMapping("")
    public String index() {
        return "ok";
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String postUser(UserPathRequest request) {
        log.info("rabbit auth user info: {}", request);

        if ("buddy".equals(request.getUsername())) {
            return "allow";
        }
        return "deny";
    }

    @PostMapping(value = "/vhost")
    public String postVhost(VhostPathRequest request) {
        log.info("rabbit auth vhost info: {}", request);

        if ("chat".equals(request.getVhost())) {
            return "allow";
        }
        return "deny";
    }

    @PostMapping(value = "/resource")
    public String postResource(ResourcePathRequest request) {
        log.info("rabbit auth vhost info: {}", request);

        // vhost_chat 으로의 접근만 허용
        if ("chat".equals(request.getVhost())) {
            // exchange 허용 케이스
            // exchange "request" 에 대해서 허용
            if ("exchange".equals(request.getResource()) && "request".equals(request.getName())) {
                // 허용하는 permission은 publish뿐
                List<String> exchangeAllowPermissions = Arrays.asList("write");

                boolean allow = exchangeAllowPermissions
                                        .stream()
                                        .anyMatch(request.getPermission()::equals);

                // 보내기라면 허용함
                if (allow) {
                    return "allow";
                }
            }

            // queue 허용 케이스
            if ("queue".equals(request.getResource())) {
                String username = request.getUsername();
                // 허용하는 패턴은 user.[username]
                Pattern queueNamePattern = Pattern.compile("user." + username);
                Matcher matcher = queueNamePattern.matcher(request.getName());

                if (matcher.find()) {
                    // 패턴에 맞는다면 큐 생성 허가
                    if ("configure".equals(request.getPermission())) {
                        return "allow";
                    }

                    // 패턴에 맞는다면 queue binding 허가
                    if ("write".equals(request.getPermission())) {
                        return "allow";
                    }

                    // 패턴에 맞는다면 consume 허가
                    if ("read".equals(request.getPermission())) {
                        return "allow";
                    }
                }
            }
        }
        return "deny";
    }

    @PostMapping(value = "/topic")
    public String postTopic(TopicPathRequest request) {
        log.info("rabbit auth vhost info: {}", request);

        Pattern pattern = Pattern.compile("(^(chat|command)\\.\\w+)");

//        if ("write".equals(request.getPermission())) {
//            if (("chat.user." + username).equals(request.getRoutingKey())) {
//                return "allow";
//            }
//        }
//
        if ("buddy".equals(request.getUsername())
                    && "chat".equals(request.getVhost())
                    && "topic".equals(request.getResource())
                    && "request".equals(request.getName())
                    && "write".equals(request.getPermission())
                    && (request.getRoutingKey() == null || pattern.matcher(request.getRoutingKey()).find())) {
            return "allow";
        }
        return "deny";
    }
}
