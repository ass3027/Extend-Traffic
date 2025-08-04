package com.helpme.extendtraffic.search

import com.helpme.extendtraffic.search.repository.MapSearchRepositoryV1
import com.helpme.extendtraffic.search.repository.MapSearchRepositoryV2
import com.helpme.extendtraffic.search.repository.MapSearchRepositoryV3
import com.helpme.extendtraffic.search.repository.SearchRepository
import org.springframework.context.annotation.Import
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

//@Import(value = [MapSearchRepositoryV1::class])
//@Import(value = [MapSearchRepositoryV2::class])
@Import(value = [MapSearchRepositoryV3::class])
@RequestMapping("/api/search")
@RestController
class SearchController(val searchRepository : SearchRepository) {

    @GetMapping
    fun getById(id: Long): ResponseEntity<List<SomeData>> {
        val data = searchRepository.findById(id)
        return ResponseEntity.ok(data)
    }

    @GetMapping("/groupId")
    fun getByGroupIp(groupId: Long): ResponseEntity<List<SomeData>> {
        val data = searchRepository.findByGroupId(groupId)
        val subListedData = data.subList(0,100)
        return ResponseEntity.ok(subListedData)
    }
}