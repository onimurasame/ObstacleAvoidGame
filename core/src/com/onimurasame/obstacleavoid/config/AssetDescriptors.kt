package com.onimurasame.obstacleavoid.config

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas

object AssetDescriptors {

    val FONT = assetDescriptor<BitmapFont>(AssetPath.VERDANA32_FONT)
    val GAMEPLAY = assetDescriptor<TextureAtlas>(AssetPath.GAMEPLAY)

    private inline fun <reified T : Any> assetDescriptor(path: String) : AssetDescriptor<T> = AssetDescriptor(path, T::class.java)

}