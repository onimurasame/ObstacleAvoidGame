package com.onimurasame.obstacleavoid.entity

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Intersector
import com.onimurasame.obstacleavoid.util.circle

abstract class GameObjectBase {
    var x: Float = 0f
        set(x) {
            field = x
            updateBounds()
        }
    var y: Float = 0f
        set(y) {
            field = y
            updateBounds()
        }

    var width: Float = 1f
        set(value) {
            field = value
            updateBounds()
        }

    var height: Float = 1f
        set(value) {
            field = value
            updateBounds()
        }

    abstract val bounds: Circle

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    private fun updateBounds() = bounds.setPosition(x + width / 2f, y + height / 2f)

/*    fun drawDebug(renderer: ShapeRenderer) {
        renderer.x(x, y, 0.1f)
        renderer.circle(bounds)
    }*/

    open fun isColliding(gameObject: GameObjectBase): Boolean = Intersector.overlaps(gameObject.bounds, this.bounds)
}