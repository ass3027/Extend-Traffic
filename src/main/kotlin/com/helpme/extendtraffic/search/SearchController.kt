package com.helpme.extendtraffic.search

import com.helpme.extendtraffic.search.repository.MapSearchRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/search")
@RestController
class SearchController(val searchRepository: MapSearchRepository) {

    @GetMapping
    fun getById(id: Long): ResponseEntity<SomeData?> {
        val data = searchRepository.findById(id)
        return ResponseEntity.ok(data)
    }

    @GetMapping("/name")
    fun getByName(name: String): ResponseEntity<List<SomeData>> {
        val data = searchRepository.findByName(name)
        val subListedData = data.subList(0,100)
        return ResponseEntity.ok(subListedData)
    }
}