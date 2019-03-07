package com.onimurasame.obstacleavoid.screen.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.onimurasame.obstacleavoid.config.AssetPath
import com.onimurasame.obstacleavoid.config.GameConfig
import com.onimurasame.obstacleavoid.entity.Obstacle
import com.onimurasame.obstacleavoid.util.*
import com.onimurasame.obstacleavoid.util.debug.DebugCameraController

class GameRenderer(private val controller: GameController) : Disposable {

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiCamera = OrthographicCamera()
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
    private val renderer = ShapeRenderer()
    private val batch: SpriteBatch = SpriteBatch()
    private val uiFont: BitmapFont = BitmapFont(AssetPath.VERDANA32_FONT.toInternalFile())
    private val debugCameraController = DebugCameraController().apply {
        setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)
    }

    private val layout = GlyphLayout()


    fun render() {
        // Debug Camera
        debugCameraController.handleDebugInput()
        debugCameraController.applyTo(camera)

        clearScreen()

        renderDebug()

        renderUi()

        viewport.drawGrid(renderer)

    }

    private fun renderDebug() {
        viewport.apply()

        renderer.projectionMatrix = camera.combined

        val oldColor = renderer.color.cpy()
        renderer.color = Color.BLUE

        renderer.use {
            renderer.line(Obstacle.HALF_SIZE, 0f, Obstacle.HALF_SIZE, GameConfig.WORLD_HEIGHT)
            renderer.line(GameConfig.WORLD_WIDTH - Obstacle.HALF_SIZE, 0f, GameConfig.WORLD_WIDTH - Obstacle.HALF_SIZE, GameConfig.WORLD_HEIGHT)
        }

        renderer.color = oldColor

        renderer.use {
            controller.player.drawDebug(renderer)
            controller.obstacles.forEach { it.drawDebug(renderer) }
        }
    }

    private fun renderUi() {
        uiViewport.apply()

        batch.projectionMatrix = uiCamera.combined

        batch.use {
            val livesText = "LIVES: ${controller.lives}"
            layout.setText(uiFont, livesText)
            uiFont.draw(batch, layout, 20f, GameConfig.HUD_HEIGHT - layout.height)

            val scoreText = "SCORE: ${controller.displayScore}"
            layout.setText(uiFont, scoreText)
            uiFont.draw(batch, layout, GameConfig.HUD_WIDTH - layout.width - 20f, GameConfig.HUD_HEIGHT - layout.height)
        }

    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        uiFont.dispose()
    }

}