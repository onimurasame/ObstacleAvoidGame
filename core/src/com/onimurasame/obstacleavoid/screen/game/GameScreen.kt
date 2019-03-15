package com.onimurasame.obstacleavoid.screen.game

import com.badlogic.gdx.Screen
import com.onimurasame.obstacleavoid.ObstacleAvoidGame
import com.onimurasame.obstacleavoid.config.AssetDescriptors
import com.onimurasame.obstacleavoid.util.logger

class GameScreen(game: ObstacleAvoidGame) : Screen {

    companion object {
            @JvmStatic
            private val log = logger<GameScreen>()
        }

    private val assetManager = game.assetManager

    private lateinit var controller: GameController
    private lateinit var renderer: GameRenderer

    override fun show() {
        assetManager.load(AssetDescriptors.FONT)
        assetManager.load(AssetDescriptors.BACKGROUND)
        assetManager.load(AssetDescriptors.OBSTACLE)
        assetManager.load(AssetDescriptors.PLAYER)

        // Blocks until all resources finished loading
        assetManager.finishLoading()


        log.debug("Asset manager diagnostics = ${assetManager.diagnostics}")

        controller = GameController()
        renderer = GameRenderer(assetManager, controller)
    }

    override fun render(delta: Float) {
        controller.update(delta)
        renderer.render()
    }

    override fun pause() {
        // Not needed for the moment
    }

    override fun resume() {
        // Not needed for the moment
    }

    override fun resize(width: Int, height: Int) {
        renderer.resize(width, height)
    }

    override fun dispose() {
        renderer.dispose()
    }

    override fun hide() {
        dispose()
    }
}