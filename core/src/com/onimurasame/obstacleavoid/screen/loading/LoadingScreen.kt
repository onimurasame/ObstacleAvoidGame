package com.onimurasame.obstacleavoid.screen.loading

import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.onimurasame.obstacleavoid.ObstacleAvoidGame
import com.onimurasame.obstacleavoid.config.AssetDescriptors
import com.onimurasame.obstacleavoid.config.GameConfig
import com.onimurasame.obstacleavoid.screen.game.GameScreen
import com.onimurasame.obstacleavoid.util.clearScreen
import com.onimurasame.obstacleavoid.util.logger

class LoadingScreen(private val game: ObstacleAvoidGame) : ScreenAdapter() {

    companion object {
        @JvmStatic
        private val log = logger<LoadingScreen>()

        private const val PROGRESS_BAR_WIDTH = GameConfig.HUD_WIDTH / 2
        private const val PROGRESS_BAR_HEIGHT = 60f
    }

    private val assetManager = game.assetManager
    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: FitViewport
    private lateinit var renderer: ShapeRenderer

    private var progress = 0f
    private var waitTime = 0.75f
    private var changeScreen = false

    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT)
        renderer = ShapeRenderer()

        assetManager.load(AssetDescriptors.FONT)
        assetManager.load(AssetDescriptors.GAMEPLAY)
    }

    override fun render(delta: Float) {
        update(delta)
        clearScreen()
        viewport.apply()

        renderer.projectionMatrix = camera.combined

        renderer.begin(ShapeRenderer.ShapeType.Filled)
        draw()
        renderer.end()

        if (changeScreen) {
            log.debug("Asset Manager Diagnostics = ${assetManager.diagnostics}")
            game.screen = GameScreen(game)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun hide() {
        dispose()
    }

    override fun dispose() {
        log.debug("Dispose...")
        renderer.dispose()
    }

    private fun update(delta: Float) {
        // PRogress between 0 and 1
        progress = assetManager.progress

        // Update returns true when all assets are loaded
        if (assetManager.update()) {
            waitTime -= delta

            if (waitTime <= 0) {
                changeScreen = true
            }
        }

    }

    private fun draw() {
        val progressBarX = (GameConfig.HUD_WIDTH - PROGRESS_BAR_WIDTH) / 2f
        val progressBarY = (GameConfig.HUD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2f
        val oldColor = renderer.color.cpy()
        renderer.color = Color.WHITE
        renderer.rect(progressBarX, progressBarY, progress * PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT)
        renderer.color = oldColor

    }

}