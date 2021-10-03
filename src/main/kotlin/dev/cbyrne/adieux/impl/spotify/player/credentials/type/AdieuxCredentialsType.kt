package dev.cbyrne.adieux.impl.spotify.player.credentials.type

import java.io.File

sealed class AdieuxCredentialsType {
    class Stored(val file: File? = null): AdieuxCredentialsType()
    class UserPass(val username: String, val password: String): AdieuxCredentialsType()
}
