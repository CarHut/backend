package com.carhutchat.models.requests;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MessageRequestBody {

    private String senderId;
    private String recipientId;

}
