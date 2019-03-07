package com.onimurasame.obstacleavoid.entity

import com.badlogic.gdx.math.Circle

class Obstacle : GameObjectBase(){

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

}