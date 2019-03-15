package com.onimurasame.obstacleavoid.entity

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.utils.Pool

class Obstacle : GameObjectBase(), Pool.Poolable{

    companion object {
        const val HALF_SIZE = 0.3f
        const val SIZE = HALF_SIZE * 2
    }

    var ySpeed = 0.1f
    var isHit = false

    override val bounds: Circle = Circle(x, y, Obstacle.HALF_SIZE)

    fun update() {
        y -= ySpeed
    }

    override fun isColliding(gameObject: GameObjectBase): Boolean {
        this.isHit = super.isColliding(gameObject)
        return this.isHit
    }

    override fun reset() {
        x = 0f
        y = 0f
        height = 1f
        width = 1f
        ySpeed = 0.1f
        isHit = false
    }

}