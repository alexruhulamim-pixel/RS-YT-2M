package com.example.model

enum class RedeemCategory(val labelEn: String, val labelBn: String) {
    ALL("All", "সবগুলো"),
    GOOGLE_PLAY("Google Play", "গুগল প্লে"),
    FREE_FIRE("Free Fire", "ফ্রি ফায়ার"),
    BGMI_PUBG("BGMI / PUBG", "বিজিএমআই"),
    ROBLOX("Roblox", "রোব্লক্স"),
    FAVORITES("Saved", "পছন্দের")
}

data class RedeemCode(
    val id: String,
    val title: String,
    val titleBn: String,
    val code: String,
    val category: RedeemCategory,
    val rewardSummary: String,
    val rewardSummaryBn: String,
    val officialUrl: String,
    val verifiedDate: String = "Today",
    val expiresInHours: Int = 12,
    val copyCount: Int = 142,
    val isFavorite: Boolean = false,
    val isUsed: Boolean = false,
    val isVerifiedToday: Boolean = true,
    val isUserAdded: Boolean = false
)
