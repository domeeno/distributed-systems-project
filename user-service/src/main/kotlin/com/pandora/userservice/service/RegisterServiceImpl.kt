package com.pandora.userservice.service

import com.pandora.userservice.dto.UserEntryDTO
import com.pandora.userservice.models.User
import com.pandora.userservice.repository.CourseRepository
import com.pandora.userservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class RegisterServiceImpl(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val courseRepository: CourseRepository
) : UserService {

    val passwordEncoder = BCryptPasswordEncoder()

    override fun registerUser(user: User): String {
        user.password = passwordEncoder.encode(user.password)
        val savedUser = userRepository.save(user)
        courseRepository.createNewUserEntry(UserEntryDTO(user.likedId, user.savedId, user.subjectsId))
        return savedUser.userId.toString()
    }
}
