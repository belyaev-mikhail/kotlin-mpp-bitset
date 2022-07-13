package ru.spbstu.mpp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BitSetTest {
    @Test
    fun simpleExample() {
        val Bitmap1 = BitSet.bitmapOf(0, 2, 55, 64, 512)
        val Bitmap2 = BitSet.bitmapOf(1, 3, 64, 512)
        Bitmap1.trim()
        Bitmap2.trim()
        assertTrue(Bitmap1.intersects(Bitmap2))
        assertFalse(Bitmap1.hashCode() == Bitmap2.hashCode())
        println("bitmap 1: $Bitmap1")
        println("bitmap 2: $Bitmap2")
        // or
        val orbitmap: BitSet = Bitmap1.toBitSet()
        val orcard = Bitmap1.orcardinality(Bitmap2)
        orbitmap.or(Bitmap2)
        assertEquals(orbitmap.cardinality(), orcard)
        println("bitmap 1 OR bitmap 2: $orbitmap")
        // and
        val andbitmap: BitSet = Bitmap1.toBitSet()
        val andcard = Bitmap1.andcardinality(Bitmap2)
        andbitmap.and(Bitmap2)
        assertEquals(andbitmap.cardinality(), andcard)
        println("bitmap 1 AND bitmap 2: $andbitmap")
        // xor
        val xorbitmap: BitSet = Bitmap1.toBitSet()
        val xorcard = Bitmap1.xorcardinality(Bitmap2)
        xorbitmap.xor(Bitmap2)
        assertEquals(xorbitmap.cardinality(), xorcard)
        println("bitmap 1 XOR bitmap 2:$xorbitmap")
        val andnotbitmap: BitSet = Bitmap1.toBitSet()
        val andnotcard = Bitmap1.andNotcardinality(Bitmap2)
        andnotbitmap.andNot(Bitmap2)
        assertEquals(andnotbitmap.cardinality(), andnotcard)
        println("bitmap 1 ANDNOT bitmap 2:$andnotbitmap")

    }

    @Test
    fun testFlipRanges() {
        val N = 256
        for (end in 1 until N) {
            for (start in 0 until end) {
                val bs1 = BitSet(N)
                for (k in start until end) {
                    bs1.flip(k)
                }
                val bs2 = BitSet(N)
                bs2.flip(start, end)
                assertEquals(bs2.cardinality(), end - start)
                assertEquals(bs1, bs2)
            }
        }
    }

    @Test
    fun testSetRanges() {
        val N = 256
        for (end in 1 until N) {
            for (start in 0 until end) {
                val bs1 = BitSet(N)
                for (k in start until end) {
                    bs1.set(k)
                }
                val bs2 = BitSet(N)
                bs2.set(start, end)
                assertEquals(bs1, bs2)
            }
        }
    }

    @Test
    fun testClearRanges() {
        val N = 256
        for (end in 1 until N) {
            for (start in 0 until end) {
                val bs1 = BitSet(N)
                bs1.set(0, N)
                for (k in start until end) {
                    bs1.clear(k)
                }
                val bs2 = BitSet(N)
                bs2.set(0, N)
                bs2.clear(start, end)
                assertEquals(bs1, bs2)
            }
        }
    }
}
