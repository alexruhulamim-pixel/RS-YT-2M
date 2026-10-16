package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.Amber400
import com.example.ui.theme.Blue600
import com.example.ui.theme.Sky400
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun RedeemGuideDialog(
    isBengali: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .testTag("dialog_redeem_guide"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBengali) "💡 কীভাবে কোড রিডিম করবেন?" else "💡 How to Redeem Codes?",
                        color = Sky400,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp).testTag("btn_close_guide")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Slate400,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Steps
                GuideStep(
                    stepNumber = "১",
                    stepNumberEn = "1",
                    title = if (isBengali) "কোড কপি করুন" else "Copy the Code",
                    desc = if (isBengali) "পছন্দের গিফট বা টপ-আপ কোডের পাশে থাকা 'Copy' বাটনে চাপ দিন।" else "Tap the 'Copy' button beside your desired gift code.",
                    isBengali = isBengali
                )

                Spacer(modifier = Modifier.height(10.dp))

                GuideStep(
                    stepNumber = "২",
                    stepNumberEn = "2",
                    title = if (isBengali) "রিডিম ওয়েবসাইটে প্রবেশ করুন" else "Open Redemption Site",
                    desc = if (isBengali) "'রিডিম করুন' বাটনে চাপ দিলে সরাসরি অফিসিয়াল রিডেম্পশন সাইট বা প্লে স্টোর খুলে যাবে।" else "Tap 'Redeem' to launch the official game reward site or Play Store.",
                    isBengali = isBengali
                )

                Spacer(modifier = Modifier.height(10.dp))

                GuideStep(
                    stepNumber = "৩",
                    stepNumberEn = "3",
                    title = if (isBengali) "আইডি লগইন ও কোড পেস্ট" else "Login & Paste Code",
                    desc = if (isBengali) "আপনার গেম আইডি (Facebook/Google/VK) দিয়ে লগইন করে কোডটি পেস্ট করুন এবং কনফার্ম করুন।" else "Log in with your game linked account, paste the code and confirm.",
                    isBengali = isBengali
                )

                Spacer(modifier = Modifier.height(10.dp))

                GuideStep(
                    stepNumber = "৪",
                    stepNumberEn = "4",
                    title = if (isBengali) "ইন-গেম মেইলে পুরস্কার গ্রহণ" else "Receive In-Game Mail",
                    desc = if (isBengali) "সফল রিডিমের পর ২৪ ঘণ্টার মধ্যে ইন-গেম মেইলবক্সে বা প্লে ব্যালেন্সে রিওয়ার্ড জমা হবে।" else "Rewards arrive within in-game mailbox or Google Play account balance.",
                    isBengali = isBengali
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isBengali) "অফিসিয়াল রিডিম পোর্টালসমূহ:" else "Official Redemption Portals:",
                    color = Amber400,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Links Row
                OutlinedButton(
                    onClick = {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://reward.ff.garena.com/")))
                        } catch (_: Exception) {}
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_link_freefire"),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = Sky400,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBengali) "ফ্রি ফায়ার রিওয়ার্ড সাইট (Garena)" else "Garena Free Fire Rewards Portal",
                        color = Slate200,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedButton(
                    onClick = {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/redeem")))
                        } catch (_: Exception) {}
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_link_googleplay"),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInNew,
                        contentDescription = null,
                        tint = Sky400,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isBengali) "গুগল প্লে স্টোর রিডিম পেজ" else "Google Play Store Redeem Page",
                        color = Slate200,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ElevatedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().testTag("btn_guide_understood"),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Blue600,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isBengali) "ঠিক আছে, বুঝতে পেরেছি" else "Got it!",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideStep(
    stepNumber: String,
    stepNumberEn: String,
    title: String,
    desc: String,
    isBengali: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Slate800)
            .padding(10.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Sky400,
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (isBengali) stepNumber else stepNumberEn,
                    color = Slate900,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Slate200,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = desc,
                color = Slate400,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}
