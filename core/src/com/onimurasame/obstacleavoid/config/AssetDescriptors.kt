package com.onimurasame.obstacleavoid.config

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont

object AssetDescriptors {

    val FONT = assetDescriptor<BitmapFont>(AssetPath.VERDANA32_FONT)
    val BACKGROUND = assetDescriptor<Texture>(AssetPath.BACKGROUND_TEXTURE)
    val OBSTACLE = assetDescriptor<Texture>(AssetPath.OBSTACLE_TEXTURE)
    val PLAYER = assetDescriptor<Texture>(AssetPath.PLAYER_TEXTURE)

    private inline fun <reified T : Any> assetDescriptor(path: String) : AssetDescriptor<T> = AssetDescriptor(path, T::class.java)

}