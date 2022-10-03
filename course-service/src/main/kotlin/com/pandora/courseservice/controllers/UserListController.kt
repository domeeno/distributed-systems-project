package com.pandora.courseservice.controllers

import com.pandora.courseservice.dto.EntryDTO
import com.pandora.courseservice.dto.LikedDTO
import com.pandora.courseservice.dto.LikedListDTO
import com.pandora.courseservice.dto.SavedDTO
import com.pandora.courseservice.dto.SavedListDTO
import com.pandora.courseservice.extensions.toSubjectIdList
import com.pandora.courseservice.models.Liked
import com.pandora.courseservice.models.Saved
import com.pandora.courseservice.models.Subject
import com.pandora.courseservice.repository.LikedRepository
import com.pandora.courseservice.repository.SavedRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("user")
class UserListController(
    @Autowired private val likedRepository: LikedRepository,
    @Autowired private val savedRepository: SavedRepository,
    @Autowired private val mongoTemplate: MongoTemplate
) {
    /*
    This class is responsible for managing individuals' user's preferences like likes and saves
     */

    @PostMapping
    fun createNewUserEntries(@RequestBody entryDTO: EntryDTO): ResponseEntity<String> {
        val liked = Liked()
        liked.id = entryDTO.likedId
        liked.likedList = arrayListOf()

        val saved = Saved()
        saved.id = entryDTO.savedId
        saved.savedList = arrayListOf()

        likedRepository.save(liked)
        savedRepository.save(saved)

        return ResponseEntity.ok().body("Created")
    }

    @GetMapping("/likes/{id}")
    fun getUserLikes(@PathVariable id: String): ResponseEntity<LikedListDTO> {
        val likedList = likedRepository.findById(id).get().toSubjectIdList()

        val query = Query(Criteria.where("id").`in`(likedList))

        val result = mongoTemplate.find(query, Subject::class.java)

        val likedListDTO = LikedListDTO(
            userLikes = result.map {
                val likedDTO = LikedDTO(subjectName = it.subjectName, subjectId = it.id)
                likedDTO
            }
        )

        return ResponseEntity.ok().body(likedListDTO)
    }

    @GetMapping("/saves/{id}")
    fun getUserSaves(@PathVariable id: String): ResponseEntity<SavedListDTO> {
        val savedList = savedRepository.findById(id).get().toSubjectIdList()

        val query = Query(Criteria.where("id").`in`(savedList))

        val result = mongoTemplate.find(query, Subject::class.java)

        val savedListDTO = SavedListDTO(
            userSaves = result.map {
                val savedDTO = SavedDTO(subjectName = it.subjectName, subjectId = it.id)
                savedDTO
            }
        )

        return ResponseEntity.ok().body(savedListDTO)
    }
}
