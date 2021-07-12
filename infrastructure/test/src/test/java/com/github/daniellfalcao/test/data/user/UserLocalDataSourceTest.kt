package com.github.daniellfalcao.test.data.user

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.daniellfalcao.data.user.model.view.ProfileView
import com.github.daniellfalcao.data.user.repository.UserLocalDataSource
import com.github.daniellfalcao.domain.user.model.UserDTO
import com.github.daniellfalcao.test._module.RepositoryTest
import com.github.daniellfalcao.test._module.mock.user1
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE, sdk = [28])
class UserLocalDataSourceTest : RepositoryTest() {

    private val userLocalDataSource by inject<UserLocalDataSource>()

    @Test
    fun `test if user is saved in database`() = runBlocking {
        // given
        val user = user1.build()
        // when
        userLocalDataSource.saveUser(user)
        // then
        val userEntity = userLocalDataSource.user()!!
        Assert.assertTrue(userEntity.id == user.id)
        Assert.assertTrue(userEntity.username == user.username)
        Assert.assertTrue(userEntity.parrot.name == user.parrot)
        Assert.assertTrue(userEntity.likes == user.likes)
        Assert.assertTrue(userEntity.bookmarks == user.bookmarks)
        Assert.assertTrue(userEntity.birthday!!.time == UserDTO.birthdayDateFormat.parse(user.birthday)!!.time)
    }

    @Test
    fun `test if token is saved in shared preferences`() = runBlocking {
        // given
        val token = "token-123"
        // when
        userLocalDataSource.saveToken(token)
        // then
        Assert.assertTrue(userLocalDataSource.token() == token)
    }

    @Test
    fun `test if token is not saved in shared preferences`() = runBlocking {
        // given
        val token = "token-123"
        // when
        userLocalDataSource.saveToken(token)
        userLocalDataSource.deleteToken()
        // then
        Assert.assertTrue(userLocalDataSource.token() == null)
    }

    @Test
    fun `test flow profile call 2 times`() = runBlocking {
        // given
        val user1update1 = user1.build()
        val user1update2 = user1.setLikes(2).build()
        // when
        var lastCollectedValue: ProfileView? = null
        val flowProfile = launch {
            userLocalDataSource.flowProfile()
                .take(2)
                .collect {
                    lastCollectedValue = it
                }
        }
        userLocalDataSource.saveUser(user1update1)
        userLocalDataSource.saveUser(user1update2)
        flowProfile.join()
        // then
        Assert.assertTrue(lastCollectedValue!!.user!!.id == user1update2.id)
        Assert.assertTrue(lastCollectedValue!!.user!!.username == user1update2.username)
        Assert.assertTrue(lastCollectedValue!!.user!!.parrot.name == user1update2.parrot)
        Assert.assertTrue(lastCollectedValue!!.user!!.likes == user1update2.likes)
        Assert.assertTrue(lastCollectedValue!!.user!!.bookmarks == user1update2.bookmarks)
        Assert.assertTrue(lastCollectedValue!!.user!!.birthday!!.time == UserDTO.birthdayDateFormat.parse(user1update2.birthday)!!.time)
    }
}