package ru.spbstu.mpp

/*
* This implementation is a direct port of BitSet implementation from java EWAH: https://github.com/lemire/javaewah
* ported to Kotlin MPP and to 32-bit arrays
*
* Comments below are from original implementation
* */

interface WordArray {
    /**
     * Get the total number of words contained in this data structure.
     * @return the number
     */
    fun getNumberOfWords(): Int

    /**
     * Get the word at the given index
     * @param index the index
     * @return the word
     */
    fun getWord(index: Int): Int
}
