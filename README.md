# kotlin-mpp-bitset

Reusable MPP bitset data structure for kotlin MPP

This is a direct source-to-source port of BitSet from java EWAH (https://github.com/lemire/javaewah) to kotlin MPP.

The differences between this version and java EWAH:

- Serialization and NIO facilities removed (inapplicable in Kotlin MPP context)
- 32-bit words used in the backing array instead of 64-bit ones (64-bit arrays are very inefficient in kotlin-js backend)
- MutableSet adapter introduced
