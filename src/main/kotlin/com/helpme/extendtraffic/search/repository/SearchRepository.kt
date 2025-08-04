package com.helpme.extendtraffic.search.repository

import com.helpme.extendtraffic.search.SomeData
import java.time.LocalDateTime

interface SearchRepository {
    fun init(){
        val names = arrayOf("이세진", "임도현", "노호관", "김동민", "지민우", "김원중")
        for(i: Int in 1..5_000){
            for(j: Int in 1..1_000){
                val name = names[(Math.random() * names.size).toInt()]
                val someData = SomeData(i.toLong(), j.toLong(), name, LocalDateTime.now(), null)
                save(someData)
            }
        }
    }

    fun save(someData: SomeData)

    fun findById(id: Long): List<SomeData>

    fun findByGroupId(groupId: Long): List<SomeData>

    fun findByName(name: String): List<SomeData>
}