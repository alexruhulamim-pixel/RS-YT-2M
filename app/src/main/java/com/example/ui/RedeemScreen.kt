package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.RedeemCategory
import com.example.ui.components.AddCodeDialog
import com.example.ui.components.CodeCard
import com.example.ui.components.HeaderSection
import com.example.ui.components.RedeemGuideDialog
import com.example.ui.components.RedeemWebView
import com.example.ui.theme.Blue600
import com.example.ui.theme.Sky400
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate300
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedeemScreen(
    viewModel: RedeemViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredCodes by viewModel.filteredCodes.collectAsState()
    val totalCopiedCount by viewModel.totalCopiedCount.collectAsState()
    val activeCount by viewModel.activeCount.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BackHandler(enabled = uiState.activeTab != 0) {
        viewModel.setActiveTab(0)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("redeem_screen_root"),
        containerColor = Slate900,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (uiState.activeTab == 0) {
                                if (uiState.isBengali) "Daily Redeem Codes" else "Daily Redeem Codes"
                            } else {
                                if (uiState.isBengali) "ওয়েবসাইট রিডিম পোর্টাল" else "Redemption Website"
                            },
                            color = Slate200,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Slate900
                ),
                actions = {
                    // Language Toggle
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Slate800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Slate700),
                        modifier = Modifier
                            .clickable { viewModel.toggleLanguage() }
                            .testTag("btn_toggle_lang")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = Sky400,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (uiState.isBengali) "বাংলা" else "EN",
                                color = Sky400,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (uiState.activeTab == 0) {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { viewModel.setShowAddDialog(true) },
                            modifier = Modifier.testTag("btn_top_add")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Code",
                                tint = Sky400
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Slate800,
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = uiState.activeTab == 0,
                    onClick = { viewModel.setActiveTab(0) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = "Codes",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.isBengali) "কোডসমূহ" else "Codes",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.activeTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Sky400,
                        selectedTextColor = Sky400,
                        indicatorColor = Slate700,
                        unselectedIconColor = Slate400,
                        unselectedTextColor = Slate400
                    ),
                    modifier = Modifier.testTag("nav_tab_codes")
                )

                NavigationBarItem(
                    selected = uiState.activeTab == 1,
                    onClick = { viewModel.setActiveTab(1) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Website",
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    label = {
                        Text(
                            text = if (uiState.isBengali) "ওয়েবসাইট" else "Website",
                            fontSize = 11.sp,
                            fontWeight = if (uiState.activeTab == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Sky400,
                        selectedTextColor = Sky400,
                        indicatorColor = Slate700,
                        unselectedIconColor = Slate400,
                        unselectedTextColor = Slate400
                    ),
                    modifier = Modifier.testTag("nav_tab_website")
                )
            }
        },
        floatingActionButton = {
            if (uiState.activeTab == 0) {
                FloatingActionButton(
                    onClick = { viewModel.setShowAddDialog(true) },
                    containerColor = Blue600,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.testTag("fab_add_code")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Code"
                    )
                }
            }
        }
    ) { innerPadding ->
        if (uiState.activeTab == 1) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                RedeemWebView(
                    isBengali = uiState.isBengali,
                    initialUrl = uiState.targetWebUrl,
                    onShowSnackbar = { msg ->
                        scope.launch { snackbarHostState.showSnackbar(msg) }
                    }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Header Hero Section
                item(key = "header_section") {
                    HeaderSection(
                        isBengali = uiState.isBengali,
                        secondsRemaining = uiState.secondsRemaining,
                        activeCodesCount = activeCount,
                        totalCopiedCount = totalCopiedCount,
                        isRefreshing = uiState.isRefreshing,
                        onRefresh = {
                            viewModel.refreshCodes()
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (uiState.isBengali) "কোডের তালিকা আপডেট করা হয়েছে!" else "Redeem codes list updated!"
                                )
                            }
                        },
                        onOpenGuide = { viewModel.setShowGuideDialog(true) }
                    )
                }

                // Search Bar
                item(key = "search_bar") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = {
                                Text(
                                    text = if (uiState.isBengali) "কোড বা গেমের নাম অনুসন্ধান করুন..." else "Search codes or games...",
                                    color = Slate500,
                                    fontSize = 13.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = Slate400,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            tint = Slate400,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Slate200,
                                unfocusedTextColor = Slate200,
                                focusedContainerColor = Slate800,
                                unfocusedContainerColor = Slate800,
                                focusedBorderColor = Sky400,
                                unfocusedBorderColor = Slate700
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_search_codes")
                        )
                    }
                }

                // Category Chips Row
                item(key = "category_tabs") {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(RedeemCategory.entries.toTypedArray()) { category ->
                            val isSelected = uiState.selectedCategory == category
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) Blue600 else Slate800,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) Sky400 else Slate700
                                ),
                                modifier = Modifier
                                    .clickable { viewModel.selectCategory(category) }
                                    .testTag("chip_category_${category.name.lowercase()}")
                            ) {
                                Text(
                                    text = if (uiState.isBengali) category.labelBn else category.labelEn,
                                    color = if (isSelected) Color.White else Slate300,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Filter status & Hide used toggle
                item(key = "filter_toggle") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.isBengali) "পাওয়া গেছে: ${filteredCodes.size} টি কোড" else "Found: ${filteredCodes.size} codes",
                            color = Slate400,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (uiState.hideUsed) Sky400.copy(alpha = 0.2f) else Slate800,
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, if (uiState.hideUsed) Sky400 else Slate700),
                            modifier = Modifier
                                .clickable { viewModel.toggleHideUsed() }
                                .testTag("btn_toggle_hide_used")
                        ) {
                            Text(
                                text = if (uiState.hideUsed) {
                                    if (uiState.isBengali) "✓ ব্যবহৃতগুলো গোপন" else "✓ Used hidden"
                                } else {
                                    if (uiState.isBengali) "ব্যবহৃতগুলো লুকান" else "Hide used"
                                },
                                color = if (uiState.hideUsed) Sky400 else Slate400,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Empty state or Code cards
                if (filteredCodes.isEmpty()) {
                    item(key = "empty_state") {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SentimentDissatisfied,
                                contentDescription = null,
                                tint = Slate500,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (uiState.isBengali) "কোনো কোড পাওয়া যায়নি" else "No redeem codes found",
                                color = Slate300,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (uiState.isBengali) "অন্য ক্যাটাগরি বা সার্চ ফিল্টার দিয়ে চেষ্টা করুন" else "Try clearing your search query or picking another category",
                                color = Slate500,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(
                        items = filteredCodes,
                        key = { it.id }
                    ) { codeItem ->
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            CodeCard(
                                codeItem = codeItem,
                                isBengali = uiState.isBengali,
                                onCopy = { id ->
                                    viewModel.onCodeCopied(id)
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (uiState.isBengali) "কোড ক্লিপবোর্ডে কপি হয়েছে!" else "Code copied to clipboard!"
                                        )
                                    }
                                },
                                onToggleFavorite = { viewModel.toggleFavorite(it) },
                                onToggleUsed = { viewModel.toggleUsed(it) },
                                onRedeemInApp = { url ->
                                    viewModel.openInWebsite(url)
                                }
                            )
                        }
                    }
                }

                // Footer Note
                item(key = "footer_note") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (uiState.isBengali) "কোডগুলি সীমিত সময়ের জন্য প্রযোজ্য। দ্রুত রিডিম করুন!" else "Codes are valid for a limited time. Redeem quickly!",
                            color = Slate500,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (uiState.isBengali) "রিডিম কোডগুলি সার্ভার লিমিট ও রিজিয়ন ভেদে কার্যকর হয়।" else "Official promo codes are subject to game developer quota & region rules.",
                            color = Slate600,
                            fontSize = 10.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    // Dialogs
    if (uiState.showGuideDialog) {
        RedeemGuideDialog(
            isBengali = uiState.isBengali,
            onDismiss = { viewModel.setShowGuideDialog(false) }
        )
    }

    if (uiState.showAddDialog) {
        AddCodeDialog(
            isBengali = uiState.isBengali,
            onDismiss = { viewModel.setShowAddDialog(false) },
            onAdd = { title, code, cat, reward, url ->
                viewModel.addNewCode(title, code, cat, reward, url)
                scope.launch {
                    snackbarHostState.showSnackbar(
                        if (uiState.isBengali) "নতুন কোড সফলভাবে যোগ করা হয়েছে!" else "New code added successfully!"
                    )
                }
            }
        )
    }
}
