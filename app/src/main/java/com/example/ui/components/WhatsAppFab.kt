package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.NavyDark

@Composable
fun WhatsAppFab(
    phoneNumber: String,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier.padding(bottom = 8.dp, end = 12.dp)
    ) {
        // WhatsApp floating action
        FloatingActionButton(
            onClick = {
                val greeting = if (language == AppLanguage.ENGLISH) {
                    "Hello FixArt Production! I am inquiring about your media and digital services."
                } else {
                    "नमस्ते फिक्सआर्ट प्रोडक्सन! म तपाईंको सेवाहरूबारे जानकारी लिन चाहन्छु।"
                }
                launchWhatsApp(context, phoneNumber, greeting)
            },
            modifier = Modifier
                .scale(pulseScale)
                .shadow(12.dp, CircleShape)
                .testTag("whatsapp_fab"),
            containerColor = Color(0xFF25D366),
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "WhatsApp Chat",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (language == AppLanguage.ENGLISH) "WhatsApp" else "ह्वाट्सएप",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
