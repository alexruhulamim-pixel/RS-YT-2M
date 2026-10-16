package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.RedeemCategory
import com.example.ui.theme.Blue600
import com.example.ui.theme.Sky400
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddCodeDialog(
    isBengali: Boolean,
    onDismiss: () -> Unit,
    onAdd: (title: String, code: String, category: RedeemCategory, reward: String, url: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var reward by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(RedeemCategory.FREE_FIRE) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp)
                .testTag("dialog_add_code"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Slate900),
            border = androidx.compose.foundation.BorderStroke(1.dp, Slate700)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBengali) "নতুন রিডিম কোড যোগ করুন" else "Add New Redeem Code",
                        color = Sky400,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp).testTag("btn_close_add_code")
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

                // Category selector
                Text(
                    text = if (isBengali) "ক্যাটাগরি নির্বাচন করুন:" else "Select Category:",
                    color = Slate400,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                val allowedCategories = listOf(
                    RedeemCategory.FREE_FIRE,
                    RedeemCategory.GOOGLE_PLAY,
                    RedeemCategory.BGMI_PUBG,
                    RedeemCategory.ROBLOX
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    allowedCategories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) Blue600 else Slate800,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) Sky400 else Slate700
                            ),
                            modifier = Modifier.clickable { selectedCategory = cat }
                        ) {
                            Text(
                                text = if (isBengali) cat.labelBn else cat.labelEn,
                                color = if (isSelected) Color.White else Slate400,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (isBengali) "শিরোনাম (যেমন: গুগল প্লে ₹২০ কোড)" else "Title (e.g. Play Store $1 Code)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Slate200,
                        unfocusedTextColor = Slate200,
                        focusedBorderColor = Sky400,
                        unfocusedBorderColor = Slate700,
                        focusedLabelColor = Sky400,
                        unfocusedLabelColor = Slate400
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_code_title")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Code Input
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it.uppercase() },
                    label = { Text(if (isBengali) "রিডিম কোড (যেমন: FF9M22MCE8SW)" else "Redeem Code (e.g. FF9M22MCE8SW)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Slate200,
                        unfocusedTextColor = Slate200,
                        focusedBorderColor = Sky400,
                        unfocusedBorderColor = Slate700,
                        focusedLabelColor = Sky400,
                        unfocusedLabelColor = Slate400
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_code_value")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Reward description
                OutlinedTextField(
                    value = reward,
                    onValueChange = { reward = it },
                    label = { Text(if (isBengali) "রিওয়ার্ডের বিবরণ (যেমন: ১০০ ডায়মন্ড)" else "Reward Details (e.g. 100 Diamonds)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Slate200,
                        unfocusedTextColor = Slate200,
                        focusedBorderColor = Sky400,
                        unfocusedBorderColor = Slate700,
                        focusedLabelColor = Sky400,
                        unfocusedLabelColor = Slate400
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("input_code_reward")
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage ?: "",
                        color = Color(0xFFEF4444),
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ElevatedButton(
                    onClick = {
                        if (title.isBlank() || code.isBlank()) {
                            errorMessage = if (isBengali) "অনুগ্রহ করে কোড এবং শিরোনাম লিখুন" else "Please enter both code and title"
                        } else {
                            onAdd(title.trim(), code.trim(), selectedCategory, reward.trim(), "")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("btn_submit_code"),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Blue600,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isBengali) "কোড সেভ করুন" else "Save Code",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
