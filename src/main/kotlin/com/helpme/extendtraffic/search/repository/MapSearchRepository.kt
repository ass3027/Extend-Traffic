package com.helpme.extendtraffic.search.repository

import com.helpme.extendtraffic.search.SomeData
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

@Repository
class MapSearchRepository: SearchRepository {
    private val db = ConcurrentHashMap<Long, SomeData>()

    init{
        val names = arrayOf("이세진", "임도현", "노호관", "김동민", "지민우", "김원중")
        for(i: Int in 1..5_000_000){
            val name = names[(Math.random() * names.size).toInt()]
            db.put(i.toLong(), SomeData(i.toLong(), name, LocalDateTime.now(), null))
        }
    }

    override fun findById(id: Long) = db[id]

    override fun findByName(name: String): List<SomeData> {
        return db.values.filter { it.name.equals(name, ignoreCase = true) }
//        return ArrayList()
    }
}
