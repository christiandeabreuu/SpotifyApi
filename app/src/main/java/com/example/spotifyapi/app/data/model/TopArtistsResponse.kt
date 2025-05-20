//package com.example.spotifyapi.app.data.model
//
//import com.google.gson.annotations.SerializedName
//
//data class TopArtistsResponse(
//    @SerializedName("items") val items: List<ArtistResponse>,
//    @SerializedName("total") val total: Int,
//    @SerializedName("limit") val limit: Int,
//    @SerializedName("offset") val offset: Int,
//    @SerializedName("href") val href: String?,
//    @SerializedName("next") val next: String?,
//    @SerializedName("previous") val previous: String?
//)
//
//data class ArtistResponse(
//    @SerializedName("id") val id: String,
//    @SerializedName("name") val name: String,
//    @SerializedName("popularity") val popularity: Int,
//    @SerializedName("images") val images: List<ImageArtist>
//)
//
//data class ImageArtist(
//    @SerializedName("url") val url: String
//)
