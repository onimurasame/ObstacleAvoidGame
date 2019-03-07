package com.onimurasame.obstacleavoid.screen.game

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.onimurasame.obstacleavoid.config.AssetPath
import com.onimurasame.obstacleavoid.config.DifficultyLevel
import com.onimurasame.obstacleavoid.config.GameConfig
import com.onimurasame.obstacleavoid.entity.Obstacle
import com.onimurasame.obstacleavoid.entity.Player
import com.onimurasame.obstacleavoid.util.*
import com.onimurasame.obstacleavoid.util.debug.DebugCameraController

@Deprecated("Use GameScreen", ReplaceWith("GameScreen"), DeprecationLevel.WARNING)
class GameScreenOld : Screen {

    private lateinit var camera: OrthographicCamera
    private lateinit var viewport: Viewport
    private lateinit var uiCamera: OrthographicCamera
    private lateinit var uiViewport: Viewport
    private lateinit var renderer: ShapeRenderer
    private lateinit var player: Player
    private lateinit var debugCameraController: DebugCameraController
    private lateinit var batch: SpriteBatch
    private lateinit var uiFont: BitmapFont

    private var obstacleTimer = 0f
    private var scoreTimer = 0f
    private var score = 0
    private var displayScore = 0
    private var lives = GameConfig.LIVES_START
    private val layout = GlyphLayout()
    private val gameOver
        get() = lives <= 0
    private var difficultyLevel = DifficultyLevel.MEDIUM

    private var obstacles = GdxArray<Obstacle>()

    override fun hide() {
        dispose()
    }

    override fun show() {
        camera = OrthographicCamera()
        viewport = FitViewport(GameConfig.WORLD_WIDTH, GameConfig.WORLD_HEIGHT, camera)

        uiCamera = OrthographicCamera()
        uiViewport = FitViewport(GameConfig.HUD_WIDTH, GameConfig.HUD_HEIGHT, uiCamera)

        renderer = ShapeRenderer()
        player = Player()

        debugCameraController = DebugCameraController()
        debugCameraController.setStartPosition(GameConfig.WORLD_CENTER_X, GameConfig.WORLD_CENTER_Y)

        batch = SpriteBatch()
        uiFont = BitmapFont(AssetPath.VERDANA32_FONT.toInternalFile())


        val startPlayerX = GameConfig.WORLD_WIDTH / 2
        val startPlayerY = 1f

        player.setPosition(startPlayerX, startPlayerY)

    }

    override fun render(delta: Float) {
        // Debug Camera
        debugCameraController.handleDebugInput()
        debugCameraController.applyTo(camera)

        if(!gameOver) {
            update(delta)
        }

        clearScreen()
        renderer.projectionMatrix = camera.combined

        renderUi()

        viewport.drawGrid(renderer)

        renderer.use {
            player.drawDebug(renderer)
            obstacles.forEach { it.drawDebug(renderer) }
        }

    }

    private fun renderUi() {

        batch.projectionMatrix = uiCamera.combined

        batch.use {
            val livesText = "LIVES: $lives"
            layout.setText(uiFont, livesText)
            uiFont.draw(batch, layout, 20f, GameConfig.HUD_HEIGHT - layout.height)

            val scoreText = "SCORE: $displayScore"
            layout.setText(uiFont, scoreText)
            uiFont.draw(batch, layout, GameConfig.HUD_WIDTH - layout.width - 20f, GameConfig.HUD_HEIGHT - layout.height)
        }

    }

    private fun update(delta: Float) {
        player.update()
        blockPlayerFromLeavingTheWorld()

        updateObstacles()
        createNewObstacle(delta)
        updateScore(delta)
        updateDisplayScore(delta)

        if (playerCollidedWithObstacle()) {
            lives--
        }
    }

    private fun updateScore(delta: Float) {
        scoreTimer += delta

        if (scoreTimer >= GameConfig.SCORE_MAX_TIME) {
            scoreTimer = 0f
            score += MathUtils.random(1, 5)
        }
    }

    private fun updateDisplayScore(delta: Float) {
        if (displayScore < score) {
            displayScore = Math.min(score, displayScore + (60 * delta).toInt())
        }
    }

    private fun playerCollidedWithObstacle(): Boolean {
        obstacles.forEach {
            if (!it.isHit && it.isColliding(player)) {
                return true
            }
        }

        return false
    }

    private fun updateObstacles() {
        obstacles.forEach { it.update() }
    }

    private fun createNewObstacle(delta: Float) {
        obstacleTimer += delta

        if (obstacleTimer >= GameConfig.OBSTACLE_SPAWN_TIME) {
            obstacleTimer = 0f

            val obstacleX = MathUtils.random(0f + Obstacle.BOUNDS_RADIUS, GameConfig.WORLD_WIDTH - Obstacle.BOUNDS_RADIUS)
            val obstacle = Obstacle()

            obstacle.setPosition(obstacleX, GameConfig.WORLD_HEIGHT)
            obstacle.ySpeed = difficultyLevel.obstacleSpeed

            obstacles.add(obstacle)
        }

    }

    private fun blockPlayerFromLeavingTheWorld() {
        player.x = MathUtils.clamp(player.x, Player.HALF_SIZE, GameConfig.WORLD_WIDTH - Player.HALF_SIZE)
    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiViewport.update(width, height, true)
    }

    override fun dispose() {
        renderer.dispose()
        batch.dispose()
        uiFont.dispose()
    }

}