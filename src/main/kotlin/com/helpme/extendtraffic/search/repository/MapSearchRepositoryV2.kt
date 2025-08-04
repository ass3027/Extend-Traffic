package com.helpme.extendtraffic.search.repository

import com.helpme.extendtraffic.search.SomeData
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

//@Repository
class MapSearchRepositoryV2: SearchRepository {
    private val dbById = ConcurrentHashMap<Long, MutableList<SomeData>>()
    private val dbByGroupId = ConcurrentHashMap<Long, MutableList<SomeData>>()
    private val dbByName = ConcurrentHashMap<String, MutableList<SomeData>>()

    init{ init() }

    override fun save(someData: SomeData) {
        someData.id?.let { id ->
            dbById.computeIfAbsent(id) { mutableListOf() }.add(someData)
        }
        someData.groupId?.let { groupId ->
            dbByGroupId.computeIfAbsent(groupId) { mutableListOf() }.add(someData)
        }
        someData.name?.let { name ->
            dbByName.computeIfAbsent(name) { mutableListOf() }.add(someData)
        }
    }

    override fun findById(id: Long) =
        dbById.getOrDefault(id, mutableListOf())

    override fun findByGroupId(groupId: Long): List<SomeData> =
        dbByGroupId.getOrDefault(groupId, emptyList())

    override fun findByName(name: String): List<SomeData> =
        dbByName.getOrDefault(name, emptyList())
}
