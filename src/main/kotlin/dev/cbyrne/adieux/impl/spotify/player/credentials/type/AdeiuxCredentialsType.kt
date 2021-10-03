package dev.cbyrne.adieux.impl.spotify.player.credentials.type

import java.io.File

sealed class AdeiuxCredentialsType {
    class Stored(val file: File? = null): AdeiuxCredentialsType()
    class UserPass(val username: String, val password: String): AdeiuxCredentialsType()
}
