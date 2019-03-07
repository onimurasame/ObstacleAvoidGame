package com.onimurasame.obstacleavoid.util.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.onimurasame.obstacleavoid.util.logger

class DebugCameraController {

    companion object {
        @JvmStatic
        private val log = logger<DebugCameraController>()
    }

    private val position = Vector2()
    private val startPosition = Vector2()
    private val config = DebugCameraConfig()

    init {
        log.debug("$config")
    }

    private var zoom = 1f
        set(value) {
            field = MathUtils.clamp(value, config.maxZoomIn, config.maxZoomOut)
        }

    fun setStartPosition(x: Float, y: Float) {
        startPosition.set(x, y)
        setPosition(x, y)
    }

    fun applyTo(camera: OrthographicCamera) {
        camera.position.set(position, 0f)
        camera.zoom = zoom
        camera.update()
    }

    fun handleDebugInput() {
        val deltaTime = Gdx.graphics.deltaTime

        val moveSpeed = config.moveSpeed * deltaTime
        val zoomSpeed = config.zoomSpeed * deltaTime

        when {
            config.isLeftPressed() -> moveLeft(moveSpeed)
            config.isRightPressed() -> moveRight(moveSpeed)
            config.isUpPressed() -> moveUp(moveSpeed)
            config.isDownPressed() -> moveDown(moveSpeed)
            config.isZoomInPressed() -> zoomIn(zoomSpeed)
            config.isZoomOutPressed() -> zoomOut(zoomSpeed)
            config.isResetPressed() -> resetZoom()
            config.isLogPressed() -> log.debug("Position = $position, Zoom = $zoom")
        }
    }


    private fun moveCamera(xSpeed: Float, ySpeed: Float) = setPosition(position.x + xSpeed, position.y + ySpeed)
    private fun setPosition(x: Float, y: Float) = position.set(x, y)
    private fun moveLeft(speed: Float) = moveCamera(-speed, 0f)
    private fun moveRight(speed: Float) = moveCamera(speed, 0f)
    private fun moveUp(speed: Float) = moveCamera(0f, speed)
    private fun moveDown(speed: Float) = moveCamera(0f, -speed)

    private fun zoomIn(zoomSpeed: Float) {
        zoom += zoomSpeed
    }

    private fun zoomOut(zoomSpeed: Float) {
        zoom -= zoomSpeed
    }

    private fun resetZoom() {
        zoom = 1f
    }

}