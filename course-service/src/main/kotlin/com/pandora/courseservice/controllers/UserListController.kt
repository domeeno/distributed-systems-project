package com.pandora.courseservice.controllers

import com.pandora.courseservice.dto.EntryDTO
import com.pandora.courseservice.dto.LikedListDTO
import com.pandora.courseservice.dto.SavedListDTO
import com.pandora.courseservice.dto.SubjectDTO
import com.pandora.courseservice.dto.SubjectListDTO
import com.pandora.courseservice.extensions.toSubjectIdList
import com.pandora.courseservice.models.Liked
import com.pandora.courseservice.models.Saved
import com.pandora.courseservice.models.Subject
import com.pandora.courseservice.models.UserSubjects
import com.pandora.courseservice.repository.LikedRepository
import com.pandora.courseservice.repository.SavedRepository
import com.pandora.courseservice.repository.SubjectRepository
import com.pandora.courseservice.repository.UserSubjectsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserListController(
    @Autowired private val likedRepository: LikedRepository,
    @Autowired private val savedRepository: SavedRepository,
    @Autowired private val userSubjectsRepository: UserSubjectsRepository,
    @Autowired private val subjectRepository: SubjectRepository,
    @Autowired private val mongoTemplate: MongoTemplate
) {

    /*
        This class is responsible for managing individuals' user's preferences like likes and saves
    */

    @PostMapping("/new")
    fun createNewUserEntries(@RequestBody entryDTO: EntryDTO): String {
        val liked = Liked()
        liked.id = entryDTO.likedId
        liked.likedList = arrayListOf()

        val saved = Saved()
        saved.id = entryDTO.savedId
        saved.savedList = arrayListOf()

        val subjects = UserSubjects()
        subjects.id = entryDTO.subjectsId
        subjects.subjectsList = arrayListOf()

        likedRepository.save(liked)
        savedRepository.save(saved)
        userSubjectsRepository.save(subjects)

        return "Created"
    }

    @GetMapping("/likes/{id}")
    fun getUserLikes(@PathVariable id: String): LikedListDTO {
        val likedList = likedRepository.findById(id).get().toSubjectIdList()

        val query = Query(Criteria.where("id").`in`(likedList))

        val result = mongoTemplate.find(query, Subject::class.java)

        val likedListDTO = LikedListDTO(
            userLikes = result.map {
                val likedDTO = SubjectDTO(subjectName = it.subjectName, subjectId = it.id)
                likedDTO
            }
        )

        return likedListDTO
    }

    @GetMapping("/saves/{id}")
    fun getUserSaves(@PathVariable id: String): SavedListDTO {
        val savedList = savedRepository.findById(id).get().toSubjectIdList()

        val query = Query(Criteria.where("id").`in`(savedList))

        val result = mongoTemplate.find(query, Subject::class.java)

        val savedListDTO = SavedListDTO(
            userSaves = result.map {
                val savedDTO = SubjectDTO(subjectName = it.subjectName, subjectId = it.id)
                savedDTO
            }
        )

        return savedListDTO
    }

    @GetMapping("/subjects/{id}")
    fun getUserCreatedSubjects(@PathVariable id: String): SubjectListDTO {
        val savedList = savedRepository.findById(id).get().toSubjectIdList()

        val query = Query(Criteria.where("id").`in`(savedList))

        val result = mongoTemplate.find(query, Subject::class.java)

        val subjectListDTO = SubjectListDTO(
            userSubjects = result.map {
                val savedDTO = SubjectDTO(subjectName = it.subjectName, subjectId = it.id)
                savedDTO
            }
        )

        return subjectListDTO
    }

    @PutMapping("{likedId}/like/{subjectId}")
    fun addToUserLikedSubjects(@PathVariable likedId: String, @PathVariable subjectId: String): String {
        val liked = likedRepository.findById(likedId).get()

        val subject = subjectRepository.findById(subjectId).get()

        val newLiked = Liked()
        newLiked.id = liked.id
        if (liked.likedList.contains(subjectId)) {
            newLiked.likedList = liked.likedList.filter { it == subjectId }
        } else {
            liked.likedList.toMutableList().add(subjectId)
            newLiked.likedList = liked.likedList
        }
        likedRepository.save(newLiked)

        subject.likes += 1
        subjectRepository.save(subject)

        return subjectId
    }
}
