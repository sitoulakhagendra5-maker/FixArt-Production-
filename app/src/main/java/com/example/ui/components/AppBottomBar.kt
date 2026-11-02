package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AppLanguage
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.MainTab

private data class NavItemData(
    val tab: MainTab,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@Composable
fun FixArtBottomBar(
    currentDestination: AppDestination,
    language: AppLanguage,
    onTabSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItemData(MainTab.HOME, Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
        NavItemData(MainTab.SERVICES, Icons.Filled.DesignServices, Icons.Outlined.DesignServices, "nav_services"),
        NavItemData(MainTab.JOBS, Icons.Filled.Work, Icons.Outlined.WorkOutline, "nav_jobs"),
        NavItemData(MainTab.BOOKINGS, Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong, "nav_bookings"),
        NavItemData(MainTab.ACCOUNT, Icons.Filled.Person, Icons.Outlined.Person, "nav_account")
    )

    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .testTag("fixart_bottom_nav"),
        containerColor = NavySurface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = when (item.tab) {
                MainTab.HOME -> currentDestination == AppDestination.HOME
                MainTab.SERVICES -> currentDestination == AppDestination.SERVICES ||
                        currentDestination == AppDestination.PHOTO_VIDEO ||
                        currentDestination == AppDestination.PRINTING ||
                        currentDestination == AppDestination.DIGITAL_MARKETING ||
                        currentDestination == AppDestination.ONLINE_SERVICES
                MainTab.JOBS -> currentDestination == AppDestination.JOBS || currentDestination == AppDestination.MY_APPLICATIONS
                MainTab.BOOKINGS -> currentDestination == AppDestination.MY_BOOKINGS
                MainTab.ACCOUNT -> currentDestination == AppDestination.MY_ACCOUNT ||
                        currentDestination == AppDestination.ABOUT ||
                        currentDestination == AppDestination.CONTACT ||
                        currentDestination == AppDestination.ADMIN
            }

            val labelText = if (language == AppLanguage.ENGLISH) item.tab.titleEn else item.tab.titleNp

            NavigationBarItem(
                modifier = Modifier.testTag(item.tag),
                selected = isSelected,
                onClick = { onTabSelected(item.tab.destination) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = labelText,
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = labelText,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NavyDark,
                    selectedTextColor = VividSky,
                    indicatorColor = VividSky,
                    unselectedIconColor = DarkTextSecondary,
                    unselectedTextColor = DarkTextSecondary
                )
            )
        }
    }
}
