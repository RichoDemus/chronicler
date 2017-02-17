package com.richodemus.chronicler.server.core

interface Version {
    override fun equals(other: Any?): Boolean
    fun after(other: Version): Boolean
    fun before(other: Version): Boolean
}

class NumericalVersion(val version: Long) : Version {
    override fun equals(other: Any?): Boolean {
        if (other is AnyVersion) {
            return true
        }
        if (other is NumericalVersion) {
            return version == other.version
        }
        throw IllegalStateException("This shouldn't happen :(")
    }

    override fun after(other: Version): Boolean {
        if (other is NumericalVersion) {
            return version > other.version
        }
        throw IllegalStateException("This shouldn't happen :(")
    }

    override fun before(other: Version): Boolean {
        if (other is NumericalVersion) {
            return version < other.version
        }
        throw IllegalStateException("This shouldn't happen :(")
    }

    fun next() = NumericalVersion(version + 1)
    override fun hashCode() = version.hashCode()
    override fun toString() = "Version $version"
}

class AnyVersion : Version {
    override fun equals(other: Any?): Boolean {
        return true
    }

    override fun after(other: Version): Boolean {
        throw NotImplementedError("This shouldn't be needed")
    }

    override fun before(other: Version): Boolean {
        throw NotImplementedError("This shouldn't be needed")
    }

    override fun hashCode() = 0
    override fun toString() = "AnyVersion"
}
