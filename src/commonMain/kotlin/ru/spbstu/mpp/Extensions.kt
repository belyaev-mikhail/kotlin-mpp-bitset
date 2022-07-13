package ru.spbstu.mpp

/**
 * Adhere to kotlin `in` operator convention
 *
 * @param element index to check
 * @see BitSet.get
 **/
operator fun BitSet.contains(element: Int): Boolean = element < size() && get(element)

/**
 * Check whether `this` bitset has all `other` bitset bits also set
 * @param index index to check
 **/
fun BitSet.containsAll(other: BitSet) = other.andNotcardinality(this) == 0

/**
 * MutableSet wrapper for BitSet
 *
 * @see MutableSet
 */
private class BitSetAsSet(val inner: BitSet): AbstractMutableSet<Int>() {
    /**
     * size is handled and stored separately from the bitset because calculating it is not free
     */
    override var size: Int = inner.cardinality()
        private set

    /**
     * recalculate size based on `inner` contents and assign it
     */
    private fun recalculateSize() {
        size = inner.cardinality()
    }

    override fun iterator(): MutableIterator<Int> {
        val innerIterator = inner.iterator()
        return object : MutableIterator<Int> {
            override fun hasNext(): Boolean = innerIterator.hasNext()
            override fun next(): Int = innerIterator.next()
            override fun remove() {
                /* we need to provide our own remove() because of size */
                innerIterator.remove()
                recalculateSize()
            }
        }
    }

    override fun add(element: Int): Boolean {
        if (element > inner.size()) {
            inner.resize((inner.size() * growFactor).toInt())
        } else {
            if (inner[element]) return false
        }
        inner.set(element)
        size++
        return true
    }

    override fun remove(element: Int): Boolean {
        if (!inner.contains(element)) return false
        inner.unset(element)
        size--
        return true
    }

    /*
    * Optimized set union: if `elements` is also a bitset, use bitset or operation
    * */
    override fun addAll(elements: Collection<Int>): Boolean {
        when (elements) {
            is BitSetAsSet -> inner.or(elements.inner)
            else -> super.addAll(elements)
        }
        val oldSize = size
        recalculateSize()
        return oldSize != size
    }

    /*
    * Optimized set inclusion: if `elements` is also a bitset, use bitset containsAll operation
    * */
    override fun containsAll(elements: Collection<Int>): Boolean {
        return when (elements) {
            is BitSetAsSet -> inner.containsAll(elements.inner)
            else -> super.containsAll(elements)
        }
    }

    /*
    * Optimized set difference: if `elements` is also a bitset, use bitset andNot operation
    * */
    override fun removeAll(elements: Collection<Int>): Boolean {
        when (elements) {
            is BitSetAsSet -> inner.andNot(elements.inner)
            else -> super.removeAll(elements)
        }
        val oldSize = size
        recalculateSize()
        return oldSize != size
    }

    /*
    * Optimized set intersection: if `elements` is also a bitset, use bitset and operation
    * */
    override fun retainAll(elements: Collection<Int>): Boolean {
        when (elements) {
            is BitSetAsSet -> inner.and(elements.inner)
            else -> super.retainAll(elements)
        }
        val oldSize = size
        recalculateSize()
        return oldSize != size
    }

    override fun contains(element: Int): Boolean = element in inner

    override fun clear() {
        inner.clear()
    }

    companion object {
        const val growFactor = 1.5
    }
}

/**
 * Wrap this BitSet as a MutableSet
 *
 * Any changes to the resulting set will change the contents of BitSet
 *
 * It is unsafe to change the contents of this BitSet after it has been wrapped
 */
fun BitSet.asMutableSet(): MutableSet<Int> = BitSetAsSet(this)

/**
 * Wrap this BitSet as a Set
 *
 * It is unsafe to change the contents of this BitSet after it has been wrapped
 */
fun BitSet.asSet(): Set<Int> = asMutableSet()
