package com.electroniclibrary.data.supabase

import com.electroniclibrary.data.model.*
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

object SupabaseHelpers {
    @Serializable
    private data class UserDto(
        val id: String? = null,
        val username: String,
        @SerialName("first_name")
        val firstName: String,
        @SerialName("last_name")
        val lastName: String,
        val role: String? = null,
        val email: String? = null,
        val password: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null
    )

    private fun UserDto.toJava(): User {
        return User(
            id,
            username,
            firstName,
            lastName,
            role,
            email,
            password
        )
    }

    private fun User.toDto(): UserDto {
        return UserDto(
            id,
            username,
            firstName,
            lastName,
            role,
            email,
            password,
            null,
            null
        )
    }

    @Serializable
    private data class BookDto(
        val id: String? = null,
        val title: String,
        @SerialName("author_id")
        val authorId: String? = null,
        @SerialName("genre_id")
        val genreId: String? = null,
        val description: String? = null,
        val content: String? = null,
        @SerialName("cover_url")
        val coverUrl: String? = null,
        @SerialName("file_url")
        val fileUrl: String? = null,
        val rating: Double? = null,
        @SerialName("rating_count")
        val ratingCount: Int? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null,
        val author: AuthorDto? = null,
        val genre: GenreDto? = null
    )

    private fun BookDto.toJava(): Book {
        val b = Book()
        b.id = id
        b.title = title
        b.authorId = authorId
        b.genreId = genreId
        b.description = description
        b.content = content
        b.coverUrl = coverUrl
        b.fileUrl = fileUrl
        b.rating = rating
        b.ratingCount = ratingCount
        author?.let { b.author = it.toJava() }
        genre?.let { b.genre = it.toJava() }
        return b
    }

    @Serializable
    private data class GenreDto(
        val id: String? = null,
        val name: String,
        val description: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null
    )

    private fun GenreDto.toJava(): Genre {
        val g = Genre()
        g.id = id
        g.name = name
        g.description = description
        return g
    }

    @Serializable
    private data class AuthorDto(
        val id: String? = null,
        @SerialName("first_name")
        val firstName: String,
        @SerialName("last_name")
        val lastName: String,
        val biography: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null
    )

    private fun AuthorDto.toJava(): Author {
        val a = Author()
        a.id = id
        a.firstName = firstName
        a.lastName = lastName
        a.biography = biography
        return a
    }

    @Serializable
    private data class FavoriteDto(
        val id: String? = null,
        @SerialName("user_id")
        val userId: String,
        @SerialName("book_id")
        val bookId: String,
        val book: BookDto? = null,
        @SerialName("created_at")
        val createdAt: String? = null
    )

    private fun FavoriteDto.toJava(): Favorite {
        val f = Favorite()
        f.id = id
        f.userId = userId
        f.bookId = bookId
        f.createdAt = createdAt
        book?.let { f.book = it.toJava() }
        return f
    }

    private fun Favorite.toDto(): FavoriteDto {
        return FavoriteDto(
            id,
            userId ?: "",
            bookId ?: "",
            null,
            createdAt
        )
    }

    @Serializable
    private data class BookRatingDto(
        val id: String? = null,
        @SerialName("user_id")
        val userId: String,
        @SerialName("book_id")
        val bookId: String,
        val rating: Int,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null
    )

    @Serializable
    private data class BookRatingUpdateDto(
        val rating: Double,
        @SerialName("rating_count")
        val ratingCount: Int
    )

    @Serializable
    private data class BookRatingSingleUpdateDto(
        val rating: Int
    )

    @JvmStatic
    fun signUp(auth: Auth, email: String, password: String, username: String? = null, firstName: String? = null, lastName: String? = null) {
        runBlocking {
            val metadata = buildJsonObject {
                username?.let { put("username", JsonPrimitive(it)) }
                firstName?.let { put("first_name", JsonPrimitive(it)) }
                lastName?.let { put("last_name", JsonPrimitive(it)) }
                put("password", JsonPrimitive(password))
                put("role", JsonPrimitive("client"))
            }

            auth.signUpWith(Email) {
                this.email = email
                this.password = password
                this.data = metadata
            }
        }
    }
    
    @JvmStatic
    fun signIn(auth: Auth, email: String, password: String) {
        runBlocking {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
        }
    }
    
    @JvmStatic
    fun signOut(auth: Auth) {
        runBlocking {
            auth.signOut()
        }
    }
    
    // Java-compatible wrappers
    @JvmStatic
    fun insertUser(postgrest: Postgrest, table: String, data: User): User {
        return runBlocking {
            val dto = data.toDto()
            // Insert without expecting body to avoid empty-response decode errors
            postgrest.from(table).insert(dto)
            // Fetch the inserted row by username
            postgrest.from(table).select {
                filter { eq("username", dto.username) }
                limit(1)
            }.decodeSingle<UserDto>().toJava()
        }
    }
    
    @JvmStatic
    fun selectAllBooks(postgrest: Postgrest, table: String): List<Book> {
        return runBlocking {
            postgrest.from(table).select(
                columns = Columns.list("*, author:authors(*), genre:genres(*)")
            ).decodeList<BookDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectAllGenres(postgrest: Postgrest, table: String): List<Genre> {
        return runBlocking {
            postgrest.from(table).select().decodeList<GenreDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectAllAuthors(postgrest: Postgrest, table: String): List<Author> {
        return runBlocking {
            postgrest.from(table).select().decodeList<AuthorDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectAllFavorites(postgrest: Postgrest, table: String): List<Favorite> {
        return runBlocking {
            postgrest.from(table).select(
                columns = Columns.list("*, book:books(*)")
            ).decodeList<FavoriteDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectWhereBooks(postgrest: Postgrest, table: String, column: String, value: String): List<Book> {
        return runBlocking {
            postgrest.from(table).select(
                columns = Columns.list("*, author:authors(*), genre:genres(*)")
            ) {
                filter {
                    eq(column, value)
                }
            }.decodeList<BookDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectWhereUsers(postgrest: Postgrest, table: String, column: String, value: String): List<User> {
        return runBlocking {
            postgrest.from(table).select {
                filter {
                    eq(column, value)
                }
                limit(1)
            }.decodeList<UserDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectWhereFavorites(postgrest: Postgrest, table: String, column: String, value: String): List<Favorite> {
        return runBlocking {
            postgrest.from(table).select(
                columns = Columns.list("*, book:books(*)")
            ) {
                filter { eq(column, value) }
            }.decodeList<FavoriteDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectWhereLikeBooks(postgrest: Postgrest, table: String, column: String, pattern: String): List<Book> {
        return runBlocking {
            postgrest.from(table).select(
                columns = Columns.list("*, author:authors(*), genre:genres(*)")
            ) {
                filter {
                    ilike(column, pattern)
                }
            }.decodeList<BookDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectPopularBooks(postgrest: Postgrest, table: String, limit: Int = 20): List<Book> {
        return runBlocking {
            postgrest.from(table).select(
                columns = Columns.list("*, author:authors(*), genre:genres(*)")
            ) {
                order("rating", Order.DESCENDING)
                this.limit(limit.toLong())
            }.decodeList<BookDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun selectSingleBook(postgrest: Postgrest, table: String, column: String, value: String): Book {
        return runBlocking {
            postgrest.from(table).select(
                columns = Columns.list("*, author:authors(*), genre:genres(*)")
            ) {
                filter {
                    eq(column, value)
                }
            }.decodeSingle<BookDto>().toJava()
        }
    }
    
    @JvmStatic
    fun insertFavorite(postgrest: Postgrest, table: String, data: Favorite): Favorite {
        return runBlocking {
            val dto = data.toDto()
            postgrest.from(table).insert(dto)
            postgrest.from(table).select(
                columns = Columns.list("*, book:books(*)")
            ) {
                filter {
                    eq("user_id", dto.userId)
                    eq("book_id", dto.bookId)
                }
                limit(1)
            }.decodeSingle<FavoriteDto>().toJava()
        }
    }
    
    @JvmStatic
    fun deleteWhere(postgrest: Postgrest, table: String, column: String, value: String) {
        runBlocking {
            postgrest.from(table).delete {
                filter {
                    eq(column, value)
                }
            }
        }
    }
    
    @JvmStatic
    fun selectWhereMultipleFavorites(postgrest: Postgrest, table: String, conditions: Map<String, String>): List<Favorite> {
        return runBlocking {
            postgrest.from(table).select(
                columns = Columns.list("*, book:books(*)")
            ) {
                filter {
                    conditions.forEach { (key, value) ->
                        eq(key, value)
                    }
                }
            }.decodeList<FavoriteDto>().map { it.toJava() }
        }
    }
    
    @JvmStatic
    fun deleteWhereMultiple(postgrest: Postgrest, table: String, conditions: Map<String, String>) {
        runBlocking {
            postgrest.from(table).delete {
                filter {
                    conditions.forEach { (key, value) ->
                        eq(key, value)
                    }
                }
            }
        }
    }

    @JvmStatic
    fun getUserRatingForBook(postgrest: Postgrest, table: String, userId: String, bookId: String): Int? {
        return runBlocking {
            postgrest.from(table).select {
                filter {
                    eq("user_id", userId)
                    eq("book_id", bookId)
                }
                limit(1)
            }.decodeList<BookRatingDto>().firstOrNull()?.rating
        }
    }

    @JvmStatic
    fun rateBook(
        postgrest: Postgrest,
        ratingsTable: String,
        booksTable: String,
        userId: String,
        bookId: String,
        rating: Int
    ): Book {
        require(rating in 1..5) { "Оценка должна быть от 1 до 5" }

        return runBlocking {
            // Проверяем, существует ли уже оценка для этого пользователя и книги
            val existingRating = postgrest.from(ratingsTable).select {
                filter {
                    eq("user_id", userId)
                    eq("book_id", bookId)
                }
                limit(1)
            }.decodeList<BookRatingDto>().firstOrNull()

            val dto = if (existingRating != null) {
                // Обновляем существующую оценку
                BookRatingDto(
                    id = existingRating.id,
                    userId = userId,
                    bookId = bookId,
                    rating = rating
                )
            } else {
                // Создаём новую оценку
                BookRatingDto(
                    userId = userId,
                    bookId = bookId,
                    rating = rating
                )
            }

            // Сохраняем или обновляем оценку пользователя
            if (existingRating != null) {
                // Обновляем существующую оценку
                postgrest.from(ratingsTable).update(BookRatingSingleUpdateDto(rating)) {
                    filter {
                        eq("user_id", userId)
                        eq("book_id", bookId)
                    }
                }
            } else {
                // Вставляем новую оценку
                postgrest.from(ratingsTable).insert(dto)
            }

            // Получаем все оценки для книги и считаем среднее
            val ratings = postgrest.from(ratingsTable).select {
                filter { eq("book_id", bookId) }
            }.decodeList<BookRatingDto>()

            val ratingValues = ratings.map { it.rating }
            val average = if (ratingValues.isNotEmpty()) ratingValues.average() else 0.0
            val count = ratingValues.size

            // Обновляем агрегированные поля в таблице books
            postgrest.from(booksTable).update(
                BookRatingUpdateDto(
                    rating = average,
                    ratingCount = count
                )
            ) {
                filter { eq("id", bookId) }
            }

            // Возвращаем актуальные данные книги
            selectSingleBook(postgrest, booksTable, "id", bookId)
        }
    }
}

