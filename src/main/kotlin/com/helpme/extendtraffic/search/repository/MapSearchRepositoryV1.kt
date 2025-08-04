package com.helpme.extendtraffic.search.repository

import com.helpme.extendtraffic.search.SomeData
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

//@Repository
class MapSearchRepositoryV1: SearchRepository {
    private val db = ConcurrentHashMap<Long, MutableList<SomeData>>()

    init{ init() }

    override fun save(someData: SomeData) {
        someData.id?.let { id ->
            db.computeIfAbsent(id) { mutableListOf() }.add(someData)
        }
    }

    override fun findById(id: Long) = db.getOrDefault(id, mutableListOf())

    override fun findByGroupId(groupId: Long): List<SomeData> =
        db.values.flatten().filter { groupId == it.groupId }

    override fun findByName(name: String): List<SomeData> =
        db.values.flatten().filter { name == it.name }
}
