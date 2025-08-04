package com.helpme.extendtraffic.search

import java.lang.management.ManagementFactory
import java.text.DecimalFormat

object MemoryProfiler {
    private val memoryBean = ManagementFactory.getMemoryMXBean()
    private val formatter = DecimalFormat("#,###.##")
    
    data class MemorySnapshot(
        val usedHeapMB: Double,
        val maxHeapMB: Double,
        val freeHeapMB: Double,
        val usedPercentage: Double,
        val timestamp: Long = System.currentTimeMillis()
    ) {
        override fun toString(): String {
            return """
                Memory Usage:
                - Used Heap: ${formatter.format(usedHeapMB)} MB
                - Max Heap: ${formatter.format(maxHeapMB)} MB  
                - Free Heap: ${formatter.format(freeHeapMB)} MB
                - Usage: ${formatter.format(usedPercentage)}%
            """.trimIndent()
        }
    }
    
    fun getMemorySnapshot(): MemorySnapshot {
        val heapMemory = memoryBean.heapMemoryUsage
        val usedBytes = heapMemory.used.toDouble()
        val maxBytes = heapMemory.max.toDouble()
        
        val usedMB = usedBytes / (1024 * 1024)
        val maxMB = maxBytes / (1024 * 1024)
        val freeMB = maxMB - usedMB
        val usedPercentage = (usedBytes / maxBytes) * 100
        
        return MemorySnapshot(usedMB, maxMB, freeMB, usedPercentage)
    }
    
    fun forceGC() {
        System.gc()
        Thread.sleep(100) // Allow GC to complete
    }
    
    fun measureMemoryUsage(description: String, operation: () -> Unit): MemorySnapshot {
        forceGC()
        val beforeSnapshot = getMemorySnapshot()
        
        println("=== MEMORY MEASUREMENT: $description ===")
        println("BEFORE:")
        println(beforeSnapshot)
        
        val startTime = System.currentTimeMillis()
        operation()
        val executionTime = System.currentTimeMillis() - startTime
        
        forceGC()
        val afterSnapshot = getMemorySnapshot()
        
        println("\nAFTER:")
        println(afterSnapshot)
        
        val memoryDelta = afterSnapshot.usedHeapMB - beforeSnapshot.usedHeapMB
        println("\nMEMORY DELTA: ${formatter.format(memoryDelta)} MB")
        println("EXECUTION TIME: ${executionTime}ms")
        println("=====================================")
        
        return afterSnapshot
    }
    
    fun getDetailedMemoryInfo(): String {
        val runtime = Runtime.getRuntime()
        val heapMemory = memoryBean.heapMemoryUsage
        val nonHeapMemory = memoryBean.nonHeapMemoryUsage
        
        return """
            === DETAILED MEMORY INFORMATION ===
            
            HEAP MEMORY:
            - Used: ${formatter.format(heapMemory.used / (1024.0 * 1024))} MB
            - Committed: ${formatter.format(heapMemory.committed / (1024.0 * 1024))} MB
            - Max: ${formatter.format(heapMemory.max / (1024.0 * 1024))} MB
            
            NON-HEAP MEMORY:
            - Used: ${formatter.format(nonHeapMemory.used / (1024.0 * 1024))} MB
            - Committed: ${formatter.format(nonHeapMemory.committed / (1024.0 * 1024))} MB
            
            RUNTIME MEMORY:
            - Total: ${formatter.format(runtime.totalMemory() / (1024.0 * 1024))} MB
            - Free: ${formatter.format(runtime.freeMemory() / (1024.0 * 1024))} MB
            - Max: ${formatter.format(runtime.maxMemory() / (1024.0 * 1024))} MB
            
            GC INFORMATION:
            ${getGCInfo()}
        """.trimIndent()
    }
    
    private fun getGCInfo(): String {
        val gcBeans = ManagementFactory.getGarbageCollectorMXBeans()
        return gcBeans.joinToString("\n") { gc ->
            "- ${gc.name}: ${gc.collectionCount} collections, ${gc.collectionTime}ms total"
        }
    }
}