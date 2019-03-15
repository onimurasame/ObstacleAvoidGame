package com.onimurasame.obstacleavoid

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.utils.Logger
import com.onimurasame.obstacleavoid.screen.game.GameScreen

class ObstacleAvoidGame : Game() {

    val assetManager = AssetManager()

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        assetManager.logger.level = Logger.DEBUG

        setScreen(GameScreen(this))
    }

    override fun dispose() {
        super.dispose()
        assetManager.dispose()
    }

}
