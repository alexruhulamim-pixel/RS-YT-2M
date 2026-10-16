package com.example.ui.components

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.Amber500
import com.example.ui.theme.Blue600
import com.example.ui.theme.Green600
import com.example.ui.theme.Sky400
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate500
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

data class WebPortalSite(
    val title: String,
    val titleBn: String,
    val url: String
)

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun RedeemWebView(
    isBengali: Boolean,
    initialUrl: String = "https://reward.ff.garena.com/",
    onShowSnackbar: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var currentUrl by remember { mutableStateOf(initialUrl) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var canGoForward by remember { mutableStateOf(false) }
    var loadingProgress by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    val popularSites = remember {
        listOf(
            WebPortalSite("Free Fire Portal", "ফ্রি ফায়ার সাইট", "https://reward.ff.garena.com/"),
            WebPortalSite("Google Play", "গুগল প্লে স্টোর", "https://play.google.com/redeem"),
            WebPortalSite("BGMI Rewards", "বিজিএমআই রিডিম", "https://www.battlegroundsmobileindia.com/redeem"),
            WebPortalSite("Roblox Portal", "রোব্লক্স রিডিম", "https://www.roblox.com/redeem")
        )
    }

    BackHandler(enabled = canGoBack) {
        webViewInstance?.goBack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Slate900)
            .testTag("redeem_webview_container")
    ) {
        // Quick site chips
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Slate800,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, Slate700)
        ) {
            Column(modifier = Modifier.padding(vertical = 6.dp)) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(popularSites) { site ->
                        val isSelected = currentUrl.startsWith(site.url) || currentUrl == site.url
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) Blue600 else Slate700,
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isSelected) Sky400 else Slate600
                            ),
                            modifier = Modifier
                                .clickable {
                                    currentUrl = site.url
                                    webViewInstance?.loadUrl(site.url)
                                }
                                .testTag("btn_site_${site.title.replace(" ", "_").lowercase()}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = if (isSelected) Color.White else Sky400,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBengali) site.titleBn else site.title,
                                    color = if (isSelected) Color.White else Slate200,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Web Browser Toolbar (URL Bar + Controls)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Slate900,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, Slate700)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { webViewInstance?.goBack() },
                    enabled = canGoBack,
                    modifier = Modifier.size(32.dp).testTag("btn_web_back")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = if (canGoBack) Slate200 else Slate500,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { webViewInstance?.goForward() },
                    enabled = canGoForward,
                    modifier = Modifier.size(32.dp).testTag("btn_web_forward")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Forward",
                        tint = if (canGoForward) Slate200 else Slate500,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { webViewInstance?.reload() },
                    modifier = Modifier.size(32.dp).testTag("btn_web_reload")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reload",
                        tint = Slate200,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Current Domain Display
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Slate800)
                ) {
                    Text(
                        text = currentUrl.replace("https://", "").replace("http://", ""),
                        color = Slate400,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }

                IconButton(
                    onClick = {
                        try {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl)))
                        } catch (_: Exception) {}
                    },
                    modifier = Modifier.size(32.dp).testTag("btn_web_external")
                ) {
                    Icon(
                        imageVector = Icons.Default.OpenInBrowser,
                        contentDescription = "Open in External Browser",
                        tint = Sky400,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Loading Bar
        if (isLoading) {
            LinearProgressIndicator(
                progress = { loadingProgress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = Sky400,
                trackColor = Slate800,
            )
        }

        // Floating Quick Helper Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Slate800,
            border = androidx.compose.foundation.BorderStroke(0.5.dp, Slate700)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBengali) "ওয়েবসাইটে লগইন করে কোডটি পেস্ট করুন" else "Log in on site and paste your copied code",
                    color = Amber500,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )

                ElevatedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clipText = clipboard.primaryClip?.getItemAt(0)?.text?.toString() ?: ""
                        if (clipText.isNotBlank()) {
                            onShowSnackbar(if (isBengali) "ক্লিপবোর্ডের কোড: $clipText" else "Current code: $clipText")
                        } else {
                            onShowSnackbar(if (isBengali) "ক্লিপবোর্ডে কোনো কোড নেই, আগে কোড কপি করুন!" else "No code in clipboard yet!")
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Green600,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("btn_paste_helper")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isBengali) "কোড দেখুন" else "View Code",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Main WebView View
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false
                        settings.cacheMode = WebSettings.LOAD_DEFAULT

                        webViewClient = object : WebViewClient() {
                            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                super.onPageStarted(view, url, favicon)
                                isLoading = true
                                url?.let { currentUrl = it }
                                canGoBack = canGoBack()
                                canGoForward = canGoForward()
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                url?.let { currentUrl = it }
                                canGoBack = canGoBack()
                                canGoForward = canGoForward()
                            }
                        }

                        webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                loadingProgress = newProgress
                                if (newProgress == 100) {
                                    isLoading = false
                                }
                            }
                        }

                        loadUrl(currentUrl)
                        webViewInstance = this
                    }
                },
                update = { webView ->
                    webViewInstance = webView
                },
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("actual_webview")
            )
        }
    }
}
