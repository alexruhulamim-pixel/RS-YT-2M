package com.example.data

import com.example.model.RedeemCategory
import com.example.model.RedeemCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RedeemRepository {

    private val initialCodes = listOf(
        RedeemCode(
            id = "rc_1",
            title = "Google Play Code (₹10 / $1)",
            titleBn = "গুগল প্লে কোড (₹১০ / $১)",
            code = "FF9M22MCE8SW",
            category = RedeemCategory.GOOGLE_PLAY,
            rewardSummary = "Instant Google Play Store balance balance credit",
            rewardSummaryBn = "গুগল প্লে স্টোরে ব্যালেন্স রিডিম করুন",
            officialUrl = "https://play.google.com/redeem?code=FF9M22MCE8SW",
            verifiedDate = "Today, 10:00 AM",
            expiresInHours = 6,
            copyCount = 384
        ),
        RedeemCode(
            id = "rc_2",
            title = "Free Fire Top-Up Special",
            titleBn = "ফ্রি ফায়ার টপ-আপ স্পেশাল (১০০ 💎)",
            code = "FFBCJVGYGG82",
            category = RedeemCategory.FREE_FIRE,
            rewardSummary = "100 Diamonds + Weapon Royale Voucher",
            rewardSummaryBn = "১০০ ডায়মন্ড এবং ওয়েপন ভাউচার",
            officialUrl = "https://reward.ff.garena.com/",
            verifiedDate = "Today, 11:30 AM",
            expiresInHours = 8,
            copyCount = 512
        ),
        RedeemCode(
            id = "rc_3",
            title = "Google Play Gift Code",
            titleBn = "গুগল প্লে গিফট কার্ড স্পেশাল",
            code = "3IBBMSL7AK8G",
            category = RedeemCategory.GOOGLE_PLAY,
            rewardSummary = "Special In-App Purchase discount voucher",
            rewardSummaryBn = "ইন-অ্যাপ ডিসকাউন্ট ভাউচার",
            officialUrl = "https://play.google.com/redeem?code=3IBBMSL7AK8G",
            verifiedDate = "Today, 1:15 PM",
            expiresInHours = 14,
            copyCount = 290
        ),
        RedeemCode(
            id = "rc_4",
            title = "Exclusive Bundle Code",
            titleBn = "এক্সক্লুসিভ বান্ডেল কোড (ফ্রি ফায়ার)",
            code = "FF11NJN5YS3E",
            category = RedeemCategory.FREE_FIRE,
            rewardSummary = "Arctic Blue Outfit & Cobra Emote Crate",
            rewardSummaryBn = "আর্কটিক ব্লু ড্রেস এবং ইমোট বক্স",
            officialUrl = "https://reward.ff.garena.com/",
            verifiedDate = "Today, 2:40 PM",
            expiresInHours = 9,
            copyCount = 743
        ),
        RedeemCode(
            id = "rc_5",
            title = "Free Fire Gloo Wall Skin",
            titleBn = "ফ্রি ফায়ার গ্লু ওয়াল স্কিন ভাউচার",
            code = "FFAC2YXE6RF2",
            category = RedeemCategory.FREE_FIRE,
            rewardSummary = "Dragon Seal Gloo Wall permanent skin",
            rewardSummaryBn = "ড্রাগন সিল গ্লু ওয়াল স্কিন",
            officialUrl = "https://reward.ff.garena.com/",
            verifiedDate = "Today, 4:00 PM",
            expiresInHours = 18,
            copyCount = 618
        ),
        RedeemCode(
            id = "rc_6",
            title = "Google Play ₹30 Airdrop Code",
            titleBn = "গুগল প্লে ₹৩০ স্পেশাল এয়ারড্রপ",
            code = "7KL92MSH402A",
            category = RedeemCategory.GOOGLE_PLAY,
            rewardSummary = "₹30 Google Play Special Airdrop balance",
            rewardSummaryBn = "₹৩০ মূল্যের প্লে স্টোর ব্যালেন্স",
            officialUrl = "https://play.google.com/redeem?code=7KL92MSH402A",
            verifiedDate = "Today, 5:20 PM",
            expiresInHours = 5,
            copyCount = 422
        ),
        RedeemCode(
            id = "rc_7",
            title = "BGMI Royal Pass UC Voucher",
            titleBn = "বিজিএমআই ইউসি এবং ক্রাফট কোড",
            code = "BGMICOP4K9SL",
            category = RedeemCategory.BGMI_PUBG,
            rewardSummary = "60 UC bonus & Classic Crate coupon",
            rewardSummaryBn = "৬০ ইউসি বোনাস এবং ক্লাসিক ক্রেট",
            officialUrl = "https://www.battlegroundsmobileindia.com/redeem",
            verifiedDate = "Today, 6:00 PM",
            expiresInHours = 12,
            copyCount = 330
        ),
        RedeemCode(
            id = "rc_8",
            title = "Roblox 100 Robux & Virtual Hat",
            titleBn = "রোব্লক্স ১০০ রিবক্স ও টুপি ভাউচার",
            code = "ROBLOX2026CODE",
            category = RedeemCategory.ROBLOX,
            rewardSummary = "100 Robux + Cyber Fedora item",
            rewardSummaryBn = "১০০ রিবক্স ও সাইবার ফেডোরা আইটেম",
            officialUrl = "https://www.roblox.com/redeem",
            verifiedDate = "Today, 7:15 PM",
            expiresInHours = 20,
            copyCount = 188
        )
    )

    private val _codes = MutableStateFlow(initialCodes)
    val codes: StateFlow<List<RedeemCode>> = _codes.asStateFlow()

    fun toggleFavorite(id: String) {
        _codes.update { list ->
            list.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    fun toggleUsed(id: String) {
        _codes.update { list ->
            list.map { if (it.id == id) it.copy(isUsed = !it.isUsed) else it }
        }
    }

    fun recordCopied(id: String) {
        _codes.update { list ->
            list.map { if (it.id == id) it.copy(copyCount = it.copyCount + 1) else it }
        }
    }

    fun addCode(
        title: String,
        code: String,
        category: RedeemCategory,
        reward: String,
        officialUrl: String
    ) {
        val newCode = RedeemCode(
            id = "custom_${System.currentTimeMillis()}",
            title = title,
            titleBn = title,
            code = code.trim().uppercase(),
            category = category,
            rewardSummary = reward.ifBlank { "User shared community code" },
            rewardSummaryBn = reward.ifBlank { "কমিউনিটি রিডিম কোড" },
            officialUrl = officialUrl.ifBlank {
                when (category) {
                    RedeemCategory.GOOGLE_PLAY -> "https://play.google.com/redeem"
                    RedeemCategory.FREE_FIRE -> "https://reward.ff.garena.com/"
                    RedeemCategory.BGMI_PUBG -> "https://www.battlegroundsmobileindia.com/redeem"
                    RedeemCategory.ROBLOX -> "https://www.roblox.com/redeem"
                    else -> "https://play.google.com/redeem"
                }
            },
            verifiedDate = "Just now",
            expiresInHours = 24,
            copyCount = 1,
            isUserAdded = true
        )
        _codes.update { listOf(newCode) + it }
    }

    fun refreshCodes() {
        // Reset or refresh statuses with updated counts
        _codes.update { current ->
            current.map {
                it.copy(
                    copyCount = it.copyCount + (1..5).random(),
                    isVerifiedToday = true
                )
            }
        }
    }
}
