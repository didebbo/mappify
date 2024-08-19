package com.didebbo.mappify.data.model

import com.didebbo.mappify.R

data class AvatarColor(val name: String, val resId: Int) {
        enum class AvatarColors(val avatarColor: AvatarColor) {
                Red(AvatarColor("Red", R.color.avatar_red)),
                Green(AvatarColor("Green", R.color.avatar_green))
        }
}