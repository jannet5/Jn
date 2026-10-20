package com.sende.app.domain.model

/**
 * UNREAD is the only state shown in the inbox. READ means the user pressed "Okundu" on that
 * message; HIDDEN means it was removed by "Ekranı tertemiz yap". Both leave the local store
 * intact (so the duplicate guard keeps working) and can be undone for a short while.
 */
enum class MessageState {
    UNREAD,
    READ,
    HIDDEN,
}
