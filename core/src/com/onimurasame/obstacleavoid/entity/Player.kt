package com.onimurasame.obstacleavoid.entity

import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Circle
import com.onimurasame.obstacleavoid.util.isKeyPressed

class Player : GameObjectBase() {

    companion object {
        // Constants

        const val SIZE = 0.8f
        const val HALF_SIZE = SIZE / 2
        const val MAX_X_SPEED = 0.25f


    }

    override val bounds: Circle = Circle(x, y, Player.HALF_SIZE)
}