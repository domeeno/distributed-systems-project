package com.pandora.userservice.service

import com.pandora.userservice.dto.EditUserDTO
import com.pandora.userservice.dto.UserEntryDTO
import com.pandora.userservice.dto.UserInfoDTO
import com.pandora.userservice.dto.UserLoginDTO
import com.pandora.userservice.models.User
import com.pandora.userservice.models.toInfo
import com.pandora.userservice.repository.CourseRepository
import com.pandora.userservice.repository.UserRepository
import com.pandora.userservice.utils.Utils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.UUID

@Service
class UserServiceImpl (
    @Autowired private val userRepository: UserRepository,
    @Autowired private val courseRepository: CourseRepository,
    @Autowired private val utils: Utils
) : UserService {

    val passwordEncoder = BCryptPasswordEncoder()

    override fun registerUser(user: User): String {
        user.password = passwordEncoder.encode(user.password)
        val savedUser = userRepository.save(user)
        courseRepository.createNewUserEntry(UserEntryDTO(user.likedId, user.savedId, user.subjectsId))
        return savedUser.userId.toString()
    }

    override fun loginUser(loginDTO: UserLoginDTO): String {
        val oUser = userRepository.findByEmail(loginDTO.email)
        val user: User
        if (oUser.isEmpty) {
            // TODO Use custom errors
            throw java.lang.Exception("not found")
        } else {
            user = oUser.get()
        }

        return if (!passwordEncoder.matches(
                loginDTO.password,
                user.password
            )
        ) {
            // TODO Use custom errors
            throw java.lang.Exception("Not good")
        } else {
            utils.generateJwt(user)
        }
    }

    override fun getUserInfo(userId: String): UserInfoDTO {
        return userRepository.findByUserId(UUID.fromString(userId)).get().toInfo()
    }

    override fun updateUser(userId: String, user: EditUserDTO): String {
        val dbUser = userRepository.findByUserId(UUID.fromString(userId)).get()

        dbUser.email = user.email ?: dbUser.email
        dbUser.firstname = user.firstName ?: dbUser.firstname
        dbUser.lastname = user.lastName ?: dbUser.lastname
        dbUser.dateOfBirth = user.dateOfBirth ?: dbUser.dateOfBirth
        dbUser.bio = user.bio ?: dbUser.bio
        dbUser.updateTimestamp = Timestamp(System.currentTimeMillis())

        userRepository.save(dbUser)

        return dbUser.userId.toString()
    }
}
