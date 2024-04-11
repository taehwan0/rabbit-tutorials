package com.example.mqtutorials.controller;

import com.example.mqtutorials.controller.dto.request.ResourcePathRequest;
import com.example.mqtutorials.controller.dto.request.TopicPathRequest;
import com.example.mqtutorials.controller.dto.request.UserPathRequest;
import com.example.mqtutorials.controller.dto.request.VhostPathRequest;
import java.util.Arrays;
import java.util.List;
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

        if ("buddy".equals(request.getUsername()) && "chat".equals(request.getVhost())) {
            if ("exchange".equals(request.getResource()) && "request".equals(request.getName())) {
                List<String> exchangeAllowPermissions = Arrays.asList("configure", "write");

                boolean allow = exchangeAllowPermissions
                                        .stream()
                                        .anyMatch(request.getPermission()::equals);

                if (allow) {
                    return "allow";
                }
            }
        }
        return "deny";
    }

    @PostMapping(value = "/topic")
    public String postTopic(TopicPathRequest request) {
        log.info("rabbit auth vhost info: {}", request);

        Pattern pattern = Pattern.compile("(^(chat|command)\\.\\w+)");

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
