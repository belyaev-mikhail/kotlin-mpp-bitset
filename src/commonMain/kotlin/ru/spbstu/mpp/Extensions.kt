package ru.spbstu.mpp

operator fun BitSet.contains(index: Int): Boolean = index < size() && get(index)

fun BitSet.containsAll(other: BitSet) = other.andNotcardinality(this) == 0

private class BitSetAsSet(val inner: BitSet): AbstractMutableSet<Int>() {
    override var size: Int = inner.cardinality()
        private set

    private fun recalculateSize() {
        size = inner.cardinality()
    }

    override fun iterator(): MutableIterator<Int> {
        val innerIterator = inner.iterator()
        return object : MutableIterator<Int> {
            override fun hasNext(): Boolean = innerIterator.hasNext()
            override fun next(): Int = innerIterator.next()
            override fun remove() {
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

    override fun addAll(elements: Collection<Int>): Boolean {
        when (elements) {
            is BitSetAsSet -> inner.or(elements.inner)
            else -> super.addAll(elements)
        }
        val oldSize = size
        recalculateSize()
        return oldSize != size
    }

    override fun containsAll(elements: Collection<Int>): Boolean {
        return when (elements) {
            is BitSetAsSet -> inner.containsAll(elements.inner)
            else -> super.containsAll(elements)
        }
    }

    override fun removeAll(elements: Collection<Int>): Boolean {
        when (elements) {
            is BitSetAsSet -> inner.andNot(elements.inner)
            else -> super.removeAll(elements)
        }
        val oldSize = size
        recalculateSize()
        return oldSize != size
    }

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

fun BitSet.asMutableSet(): MutableSet<Int> = BitSetAsSet(this)
fun BitSet.asSet(): Set<Int> = asMutableSet()
