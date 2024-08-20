package com.didebbo.mappify.data.model

import com.didebbo.mappify.R

data class AvatarColor(
        val id: String = "avatar_color_gray",
        val name: String = "Gray",
        val resId: Int = R.color.avatar_gray
) {
        enum class AvatarColors(val avatarColor: AvatarColor) {
                Red(AvatarColor("avatar_color_red","Red",R.color.avatar_red)),
                Green(AvatarColor("avatar_color_green","Green",R.color.avatar_green))
        }
}