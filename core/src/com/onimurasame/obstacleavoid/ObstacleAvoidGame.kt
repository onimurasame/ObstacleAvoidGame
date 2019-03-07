package com.onimurasame.obstacleavoid

import com.badlogic.gdx.Application
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.onimurasame.obstacleavoid.screen.game.GameScreen

class ObstacleAvoidGame : Game() {

    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG
        setScreen(GameScreen())
    }

}
