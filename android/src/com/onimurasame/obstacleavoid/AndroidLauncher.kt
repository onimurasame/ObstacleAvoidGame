package com.onimurasame.obstacleavoid

import android.os.Bundle

import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.onimurasame.obstacleavoid.ObstacleAvoidGame

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize(ObstacleAvoidGame(), AndroidApplicationConfiguration())
    }
}
