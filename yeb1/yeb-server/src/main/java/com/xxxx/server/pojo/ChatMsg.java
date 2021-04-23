package com.xxxx.server.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;


@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ChatMsg {
    private String from;
    private String to;
    private String content;
    private LocalDateTime date;
    private String fromNickName;
}
