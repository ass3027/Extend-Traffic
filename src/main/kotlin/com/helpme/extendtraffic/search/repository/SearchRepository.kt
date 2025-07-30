package com.helpme.extendtraffic.search.repository

import com.helpme.extendtraffic.search.SomeData

interface SearchRepository {
    fun findById(id: Long): SomeData?

    fun findByName(name: String): List<SomeData>
}