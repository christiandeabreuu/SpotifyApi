package com.example.spotify.utils

object Constants {
    const val CLIENT_ID = "9cde7198eaf54c06860b6d0257dcd893"
    const val CLIENT_SECRET = "d601127a963c4791a61e9145bedd7fe6"
    const val REDIRECT_URI = "meuapp://callback"
    const val AUTHORIZATION_CODE = ""
    const val AUTH_URL = "https://accounts.spotify.com/authorize?client_id=9cde7198eaf54c06860b6d0257dcd893&response_type=code&redirect_uri=meuapp://callback&scope=user-read-private%20user-read-email%20playlist-modify-public%20playlist-modify-private%20user-top-read"
    const val TOKEN_URL = "https://accounts.spotify.com/api/token"
}
