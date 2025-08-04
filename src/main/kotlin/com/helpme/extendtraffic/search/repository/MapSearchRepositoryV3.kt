package com.helpme.extendtraffic.search.repository

import com.helpme.extendtraffic.search.SomeData
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

//@Repository
class MapSearchRepositoryV3: SearchRepository {
    private val db = ConcurrentHashMap<Long, MutableList<SomeData>>()
    private val roomIdUserIdIndexMap = ConcurrentHashMap<Long, MutableList<LongArray>>()
    private val nameIndexMap = ConcurrentHashMap<String, MutableList<LongArray>>()

    init{ init() }

    override fun save(someData: SomeData) {
        someData.id?.let { id ->
            val dataList = db.computeIfAbsent(id) { mutableListOf() }
            dataList.add(someData)
            val index = (dataList.size - 1).toLong()
            
            someData.groupId?.let { groupId ->
                roomIdUserIdIndexMap.computeIfAbsent(groupId) { mutableListOf() }
                    .add(longArrayOf(id, index))
            }
            
            someData.name?.let { name ->
                nameIndexMap.computeIfAbsent(name) { mutableListOf() }
                    .add(longArrayOf(id, index))
            }
        }
    }

    override fun findById(id: Long) =
        db.getOrDefault(id, mutableListOf())

    override fun findByGroupId(groupId: Long): List<SomeData> =
        roomIdUserIdIndexMap.getOrDefault(groupId, emptyList())
            .map { db.get(it[0])!![it[1].toInt()] }

    override fun findByName(name: String): List<SomeData> =
        nameIndexMap.getOrDefault(name, emptyList())
            .map { db.get(it[0])!![it[1].toInt()] }
}
