package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.HapticFeedbackConstants
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RedeemCategory
import com.example.model.RedeemCode
import com.example.ui.theme.Amber500
import com.example.ui.theme.Blue600
import com.example.ui.theme.Green600
import com.example.ui.theme.Red500
import com.example.ui.theme.Sky400
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import kotlinx.coroutines.delay

@Composable
fun CodeCard(
    codeItem: RedeemCode,
    isBengali: Boolean,
    onCopy: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onToggleUsed: (String) -> Unit,
    onRedeemInApp: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val view = LocalView.current
    var isCopied by remember { mutableStateOf(false) }

    LaunchedEffect(isCopied) {
        if (isCopied) {
            delay(2500)
            isCopied = false
        }
    }

    val copyButtonBg by animateColorAsState(
        targetValue = if (isCopied) Green600 else Blue600,
        animationSpec = tween(durationMillis = 200),
        label = "copyButtonBg"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("code_card_${codeItem.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Slate900
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Category & Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    CategoryBadge(category = codeItem.category, isBengali = isBengali)
                    if (codeItem.isVerifiedToday) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Green600.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, Green600)
                        ) {
                            Text(
                                text = if (isBengali) "যাচাইকৃত" else "Verified",
                                color = Green600,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onToggleFavorite(codeItem.id) },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_favorite_${codeItem.id}")
                    ) {
                        Icon(
                            imageVector = if (codeItem.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (codeItem.isFavorite) Red500 else Slate400,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "🔥 ${codeItem.title}: ${codeItem.code}\nRedeem at: ${codeItem.officialUrl}"
                                )
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "Share Redeem Code"))
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_share_${codeItem.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Slate400,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Title & Reward
            Text(
                text = if (isBengali) codeItem.titleBn else codeItem.title,
                color = Amber500,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (isBengali) codeItem.rewardSummaryBn else codeItem.rewardSummary,
                color = Slate400,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Code & Copy Row (Faithful to user's design)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Slate800)
                    .border(1.dp, Slate700, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = codeItem.code,
                        color = if (codeItem.isUsed) Slate500 else Slate200,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp,
                        textDecoration = if (codeItem.isUsed) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                ElevatedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Redeem Code", codeItem.code)
                        clipboard.setPrimaryClip(clip)
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                        isCopied = true
                        onCopy(codeItem.id)
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = copyButtonBg,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_copy_${codeItem.id}")
                ) {
                    Icon(
                        imageVector = if (isCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCopied) {
                            if (isBengali) "কপি হয়েছে!" else "Copied!"
                        } else {
                            if (isBengali) "Copy" else "Copy"
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action footer: Redeem Now + Mark As Used + Claims counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = Slate500,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = if (isBengali) "${codeItem.expiresInHours} ঘণ্টা বাকি" else "${codeItem.expiresInHours}h left",
                        color = Slate500,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "•",
                        color = Slate500,
                        fontSize = 11.sp
                    )
                    Text(
                        text = if (isBengali) "${codeItem.copyCount} জন নিয়েছেন" else "${codeItem.copyCount} copied",
                        color = Slate400,
                        fontSize = 11.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Mark as used toggle
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (codeItem.isUsed) Slate700 else Color.Transparent)
                            .clickable { onToggleUsed(codeItem.id) }
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = if (codeItem.isUsed) {
                                if (isBengali) "✓ ব্যবহার হয়েছে" else "✓ Used"
                            } else {
                                if (isBengali) "ক্লেম করা হয়নি" else "Unused"
                            },
                            color = if (codeItem.isUsed) Sky400 else Slate500,
                            fontSize = 11.sp
                        )
                    }

                    // Direct Redeem URL action
                    OutlinedButton(
                        onClick = {
                            if (onRedeemInApp != null) {
                                onRedeemInApp(codeItem.officialUrl)
                            } else {
                                try {
                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(codeItem.officialUrl))
                                    context.startActivity(browserIntent)
                                } catch (_: Exception) {}
                            }
                        },
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                        modifier = Modifier.testTag("btn_redeem_link_${codeItem.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = "Redeem link",
                            tint = Sky400,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = if (isBengali) "রিডিম করুন" else "Redeem",
                            color = Sky400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryBadge(category: RedeemCategory, isBengali: Boolean) {
    val (bg, fg) = when (category) {
        RedeemCategory.GOOGLE_PLAY -> Pair(Color(0xFF00875A).copy(alpha = 0.2f), Color(0xFF34D399))
        RedeemCategory.FREE_FIRE -> Pair(Color(0xFFFF5722).copy(alpha = 0.2f), Color(0xFFFF8A65))
        RedeemCategory.BGMI_PUBG -> Pair(Color(0xFFEAB308).copy(alpha = 0.2f), Color(0xFFFDE047))
        RedeemCategory.ROBLOX -> Pair(Color(0xFFE11D48).copy(alpha = 0.2f), Color(0xFFFB7185))
        RedeemCategory.FAVORITES -> Pair(Red500.copy(alpha = 0.2f), Red500)
        RedeemCategory.ALL -> Pair(Sky400.copy(alpha = 0.2f), Sky400)
    }

    Surface(
        shape = RoundedCornerShape(4.dp),
        color = bg
    ) {
        Text(
            text = if (isBengali) category.labelBn else category.labelEn,
            color = fg,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
