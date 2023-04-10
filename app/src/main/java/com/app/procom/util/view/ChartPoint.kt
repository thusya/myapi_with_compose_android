package com.app.procom.util.view

class ChartPoint private constructor(val x: Float, val y: Float, val isEmpty: Boolean) {
    constructor(x: Float, y: Float) : this(x, y, false)

    companion object {
        fun empty(x: Float) = ChartPoint(x, Float.POSITIVE_INFINITY, true)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChartPoint

        if (x != other.x) return false
        if (y != other.y) return false
        if (isEmpty != other.isEmpty) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + isEmpty.hashCode()
        return result
    }
}


