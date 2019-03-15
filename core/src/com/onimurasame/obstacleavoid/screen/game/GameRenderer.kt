package com.onimurasame.obstacleavoid.screen.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.FitViewport
import com.onimurasame.obstacleavoid.config.AssetDescriptors
import com.onimurasame.obstacleavoid.config.AssetPath.BACKGROUND_TEXTURE
import com.onimurasame.obstacleavoid.config.AssetPath.OBSTACLE_TEXTURE
import com.onimurasame.obstacleavoid.config.AssetPath.PLAYER_TEXTURE
import com.onimurasame.obstacleavoid.config.GameConfig
import com.onimurasame.obstacleavoid.config.GameConfig.WORLD_WIDTH
import com.onimurasame.obstacleavoid.entity.Obstacle
import com.onimurasame.obstacleavoid.entity.Player
import com.onimurasame.obstacleavoid.util.*
import com.onimurasame.obstacleavoid.util.debug.DebugCameraController

class GameRenderer(assetManager: AssetManager, private val controller: GameController) : Disposable {

    companion object {
        @JvmStatic
        private val log = logger<GameRenderer>()
    }

    private val camera = OrthographicCamera()
    private val viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)
    private val uiCamera = OrthographicCamera()
    private val uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)
    private val renderer = ShapeRenderer()
    private val batch: SpriteBatch = SpriteBatch()
    private val layout = GlyphLayout()
    private val debugCameraController = DebugCameraController().apply {
        setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)
    }

    private val uiFont: BitmapFont = assetManager[AssetDescriptors.FONT]
    private val playerTexture = assetManager[AssetDescriptors.PLAYER]
    private val obstacleTexture = assetManager[AssetDescriptors.OBSTACLE]
    private val backgroundTexture = assetManager[AssetDescriptors.BACKGROUND]


    fun render() {
        // Debug Camera
        debugCameraController.handleDebugInput()
        debugCameraController.applyTo(camera)

        clearScreen()

        if (Gdx.input.isTouched && !controller.gameOver) {
            val screenTouch = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            val worldTouch = viewport.unproject(Vector2(screenTouch))

            log.debug("screentouch = $screenTouch")
            log.debug("worldtouch = $worldTouch")

            val player = controller.player
            worldTouch.x = MathUtils.clamp(worldTouch.x, 0f, WORLD_WIDTH - Player.SIZE)
            player.x = worldTouch.x
        }

        renderGameplay()
        renderDebug()

        renderUi()

        viewport.drawGrid(renderer)

    }

    private fun renderGameplay() {
        viewport.apply()
        batch.projectionMatrix = camera.combined

        batch.use {
            batch.draw(backgroundTexture, 0f, 0f, GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT)

            val player = controller.player
            batch.draw(playerTexture, player.x, player.y, Player.SIZE, Player.SIZE)

            controller.obstacles.forEach {
                batch.draw(obstacleTexture, it.x, it.y, Obstacle.SIZE, Obstacle.SIZE)
            }
        }
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
            val playerBounds = controller.player.bounds
            renderer.x(playerBounds.x, playerBounds.y, 0.1f)
            renderer.circle(playerBounds)

            controller.obstacles.forEach {
                renderer.x(it.bounds.x, it.bounds.y, 0.1f)
                renderer.circle(it.bounds)
            }
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
    }

}