package com.app.procom.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.procom.storage.AppDatabase
import com.app.procom.util.Constants
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppDatabaseTest {

    val mockContext: Context = mockk(relaxed = true)

    @BeforeAll
    fun beforeGroup() {
        mockkStatic(Room::class)
    }

    @AfterAll
    fun afterGroup() {
        clearAllMocks()
    }

    @DisplayName("Given context")
    @Nested
    inner class GivenContext {

        private val mockBuilder: RoomDatabase.Builder<AppDatabase> = mockk(relaxed = true)

        @DisplayName(
            "When you call the getAppDatabase, " +
                    "then it return the AppDatabase instance "
        )
        @Test
        fun test() {

            every { Room.databaseBuilder<AppDatabase>(any(), any(), any()) } returns mockBuilder
            every { mockBuilder.addMigrations(any()) } returns mockBuilder
            every { mockBuilder.build() } returns mockk(relaxed = true)

            AppDatabase.getAppDatabase(mockContext)
            verify {
                Room.databaseBuilder(mockContext, AppDatabase::class.java, Constants.DATABASE_NAME)
            }
        }
    }
}