package cz.applifting.graphqlempty.firebasechat.auth

data class BasicUser(
    val uid: String,
    val displayName: String,
    val photoUrl: String?,
)
