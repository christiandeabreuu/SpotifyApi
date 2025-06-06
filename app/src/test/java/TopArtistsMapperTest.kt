import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.model.ImageArtistResponse
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.model.ArtistResponse
import com.example.spotifyapi.app.domain.mapper.TopArtistsMapper
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class TopArtistsMapperTest {

    @Test
    fun `should map TopArtistsResponse to ArtistDB list correctly`() {
        // Arrange
        val image = ImageArtistResponse(url = "http://image.url")
        val artistResponse = ArtistResponse(
            id = "1",
            name = "Artist Name",
            popularity = 99,
            images = listOf(image)
        )
        val topArtistsResponse = TopArtistsResponse(
            items = listOf(artistResponse),
            total = 1,
            limit = 20,
            offset = 0,
            href = "http://api.spotify.com/artists",
            next = null,
            previous = null
        )
        val timeRange = "medium_term"

        // Act
        val result = TopArtistsMapper.toArtistsDB(topArtistsResponse, timeRange)

        // Assert
        val expected = ArtistDB(
            id = "1",
            name = "Artist Name",
            popularity = 99,
            imageUrl = "http://image.url",
            timeRange = timeRange,
            total = 1,
            limit = 20,
            offset = 0,
            href = "http://api.spotify.com/artists",
            next = null,
            previous = null
        )

        assertEquals(1, result.size)
        assertEquals(expected, result[0])
    }

    @Test
    fun `should map empty TopArtistsResponse to empty ArtistDB list`() {
        // Arrange
        val topArtistsResponse = TopArtistsResponse(
            items = emptyList(),
            total = 0,
            limit = 20,
            offset = 0,
            href = "",
            next = null,
            previous = null
        )
        val timeRange = "short_term"

        // Act
        val result = TopArtistsMapper.toArtistsDB(topArtistsResponse, timeRange)

        // Assert
        assertEquals(0, result.size)
    }
}