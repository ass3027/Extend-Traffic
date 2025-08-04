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

    @GetMapping("/name")
    fun getByName(name: String): ResponseEntity<List<SomeData>> {
        val data = searchRepository.findByName(name)
        return ResponseEntity.ok(data)
    }

    @GetMapping("/memory/status")
    fun getMemoryStatus(): ResponseEntity<String> {
        val memoryInfo = MemoryProfiler.getDetailedMemoryInfo()
        return ResponseEntity.ok(memoryInfo)
    }

    @GetMapping("/memory/measure-init")
    fun measureInitMemory(): ResponseEntity<String> {
        val result = StringBuilder()
        
        MemoryProfiler.measureMemoryUsage("Repository Initialization") {
            // Force re-initialization by creating new repository
            val tempRepo = when(searchRepository) {
                is MapSearchRepositoryV1 -> MapSearchRepositoryV1()
                is MapSearchRepositoryV2 -> MapSearchRepositoryV2() 
                is MapSearchRepositoryV3 -> MapSearchRepositoryV3()
                else -> throw IllegalStateException("Unknown repository type")
            }
        }
        
        result.append("Memory measurement completed. Check logs for details.\n")
        result.append(MemoryProfiler.getDetailedMemoryInfo())
        
        return ResponseEntity.ok(result.toString())
    }

    @GetMapping("/memory/measure-query")
    fun measureQueryMemory(groupId: Long = 1): ResponseEntity<String> {
        val result = StringBuilder()
        
        result.append("=== QUERY PERFORMANCE MEASUREMENT ===\n\n")
        
        // Measure findByGroupId
        MemoryProfiler.measureMemoryUsage("findByGroupId($groupId)") {
            val data = searchRepository.findByGroupId(groupId)
            result.append("Found ${data.size} records for groupId $groupId\n")
        }
        
        // Measure findByName
        MemoryProfiler.measureMemoryUsage("findByName('이세진')") {
            val data = searchRepository.findByName("이세진")
            result.append("Found ${data.size} records for name '이세진'\n")
        }
        
        result.append("\n").append(MemoryProfiler.getDetailedMemoryInfo())
        
        return ResponseEntity.ok(result.toString())
    }
}