package com.onimurasame.obstacleavoid.screen.game

import com.badlogic.gdx.Screen

class GameScreen : Screen {

    private lateinit var controller: GameController
    private lateinit var renderer: GameRenderer

    override fun show() {
        controller = GameController()
        renderer = GameRenderer(controller)
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