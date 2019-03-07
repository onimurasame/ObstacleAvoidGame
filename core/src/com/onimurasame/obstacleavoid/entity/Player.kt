package com.onimurasame.obstacleavoid.entity

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Circle
import com.onimurasame.obstacleavoid.util.isKeyPressed

class Player : GameObjectBase() {

    companion object {
        // Constants

        private const val BOUNDS_RADIUS = 0.4f
        private const val SIZE = BOUNDS_RADIUS * 2
        private const val MAX_X_SPEED = 0.25f

        const val HALF_SIZE = BOUNDS_RADIUS

    }

    override val bounds: Circle = Circle(x, y, Player.BOUNDS_RADIUS)


    fun update() {
        var xSpeed = 0f

        when {
            Input.Keys.D.isKeyPressed() -> xSpeed = MAX_X_SPEED
            Input.Keys.A.isKeyPressed() -> xSpeed = -MAX_X_SPEED
        }

        x += xSpeed
    }


}