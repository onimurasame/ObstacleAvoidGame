package com.onimurasame.obstacleavoid.entity

import com.badlogic.gdx.math.Circle

class Obstacle : GameObjectBase(){

    companion object {
        const val BOUNDS_RADIUS = 0.3f
        const val SIZE = BOUNDS_RADIUS * 2
    }

    var ySpeed = 0.1f
    var isHit = false

    override val bounds: Circle = Circle(x, y, Obstacle.BOUNDS_RADIUS)

    fun update() {
        y -= ySpeed
    }

    override fun isColliding(gameObject: GameObjectBase): Boolean {
        this.isHit = super.isColliding(gameObject)
        return this.isHit
    }

}