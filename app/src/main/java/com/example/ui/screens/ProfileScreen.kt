package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CasinoBackground
import com.example.ui.theme.CasinoBorder
import com.example.ui.theme.CasinoGold
import com.example.ui.theme.CasinoGoldBorder
import com.example.ui.theme.CasinoGreen
import com.example.ui.theme.CasinoRed
import com.example.ui.theme.CasinoSurface
import com.example.ui.theme.CasinoSurfaceElevated
import com.example.ui.theme.CasinoTextPrimary
import com.example.ui.theme.CasinoTextSecondary
import com.example.ui.theme.CasinoViolet
import com.example.viewmodel.PK999ViewModel

@Composable
fun ProfileScreen(
    viewModel: PK999ViewModel,
    onNavigateWallet: () -> Unit,
    onOpenAdminDialog: () -> Unit
) {
    val user by viewModel.userProfile.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp)
    ) {
        // 1. VIP Profile Header Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CasinoSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CasinoGoldBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar Crown Badge
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(CasinoGold.copy(alpha = 0.2f))
                                .border(2.dp, CasinoGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👑", fontSize = 28.sp)
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.displayName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = CasinoTextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = CasinoGold
                                ) {
                                    Text(
                                        text = "VIP 2",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        color = CasinoBackground,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "UID: ${user.uid}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CasinoGold,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = user.email,
                                fontSize = 11.sp,
                                color = CasinoTextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Hardware hash security pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CasinoBackground,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = "Device Hash",
                                tint = CasinoGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Hardware Hash: ${user.deviceFingerprint}",
                                fontSize = 10.sp,
                                color = CasinoTextSecondary,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 2. Financial Overview Stats
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CasinoSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CasinoBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "ACCOUNT FINANCIAL LEDGER",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CasinoGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Game Balance", fontSize = 11.sp, color = CasinoTextSecondary)
                            Text(text = "Rs ${user.balance}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = CasinoGold)
                        }
                        Column {
                            Text(text = "Turnover Done", fontSize = 11.sp, color = CasinoTextSecondary)
                            Text(text = "Rs ${user.currentTurnoverCompleted}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = CasinoGreen)
                        }
                        Column {
                            Text(text = "Total Deposits", fontSize = 11.sp, color = CasinoTextSecondary)
                            Text(text = "Rs ${user.totalDeposited}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = CasinoTextPrimary)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 3. Settings & Options List
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CasinoSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CasinoBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    ProfileMenuRow(
                        icon = Icons.Default.Payment,
                        title = "Wallet & Fast-Cashout",
                        subtitle = "JazzCash, EasyPaisa, USDT & 29x Turnover",
                        iconColor = CasinoGreen,
                        onClick = onNavigateWallet
                    )

                    ProfileMenuRow(
                        icon = Icons.Default.AdminPanelSettings,
                        title = "Admin Revenue & Commission Inspector",
                        subtitle = "Inspect 5% cash-out fee and platform turnover node",
                        iconColor = CasinoGold,
                        onClick = onOpenAdminDialog
                    )

                    ProfileMenuRow(
                        icon = Icons.Default.Security,
                        title = "Provably Fair RNG & Fair Play",
                        subtitle = "Deterministic SHA-256 seed validation engine",
                        iconColor = Color(0xFF00E5FF),
                        onClick = {
                            viewModel.emitToast("Provably Fair Algorithm: SHA256(PeriodID + Seed) Active")
                        }
                    )

                    ProfileMenuRow(
                        icon = Icons.Default.Star,
                        title = "VIP Privileges & Rebates",
                        subtitle = "Level 2: 0.3% Daily Bet Cashback Active",
                        iconColor = CasinoViolet,
                        onClick = {
                            viewModel.emitToast("VIP Level 2 benefits active on all Win Go rounds")
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Logout / Switch Google Account Button
        item {
            OutlinedButton(
                onClick = { viewModel.logout() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = CasinoRed),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(CasinoRed.copy(alpha = 0.5f))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("logout_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = CasinoRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Switch Account / Exit Gate",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CasinoTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = CasinoTextSecondary
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Navigate",
            tint = CasinoTextSecondary,
            modifier = Modifier.size(14.dp)
        )
    }
}
