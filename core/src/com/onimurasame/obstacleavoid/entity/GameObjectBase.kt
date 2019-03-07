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
    abstract val bounds: Circle

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    private fun updateBounds() = bounds.setPosition(x, y)
    fun drawDebug(renderer: ShapeRenderer) = renderer.circle(bounds)
    open fun isColliding(gameObject: GameObjectBase): Boolean = Intersector.overlaps(gameObject.bounds, this.bounds)
}