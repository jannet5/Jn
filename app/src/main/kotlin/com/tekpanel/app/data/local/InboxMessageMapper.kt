package com.tekpanel.app.data.local

import com.tekpanel.app.domain.model.CaptureMethod
import com.tekpanel.app.domain.model.InboxMessage
import com.tekpanel.app.domain.model.MessageState
import java.time.Instant

fun InboxMessage.toEntity(): InboxMessageEntity = InboxMessageEntity(
    id = id,
    notificationKey = notificationKey,
    fingerprint = fingerprint,
    sourcePackage = sourcePackage,
    conversationKey = conversationKey,
    conversationTitle = conversationTitle,
    isGroupConversation = isGroupConversation,
    senderName = senderName,
    messageText = messageText,
    receivedAtEpochMillis = receivedAt.toEpochMilli(),
    capturedAtEpochMillis = capturedAt.toEpochMilli(),
    captureMethod = captureMethod.name,
    state = state.name,
    avatarPath = avatarPath,
    picturePath = picturePath,
)

fun InboxMessageEntity.toDomain(): InboxMessage = InboxMessage(
    id = id,
    notificationKey = notificationKey,
    fingerprint = fingerprint,
    sourcePackage = sourcePackage,
    conversationKey = conversationKey,
    conversationTitle = conversationTitle,
    isGroupConversation = isGroupConversation,
    senderName = senderName,
    messageText = messageText,
    receivedAt = Instant.ofEpochMilli(receivedAtEpochMillis),
    capturedAt = Instant.ofEpochMilli(capturedAtEpochMillis),
    captureMethod = runCatching { CaptureMethod.valueOf(captureMethod) }.getOrDefault(CaptureMethod.NOTIFICATION),
    state = runCatching { MessageState.valueOf(state) }.getOrDefault(MessageState.UNREAD),
    avatarPath = avatarPath,
    picturePath = picturePath,
)
