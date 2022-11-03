package cz.applifting.graphqlempty.firebase.auth

data class BasicUser(
    val uid: String,
    val displayName: String,
    val photoUrl: String?,
)
