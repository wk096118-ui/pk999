package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CasinoBackground
import com.example.ui.theme.CasinoBorder
import com.example.ui.theme.CasinoGold
import com.example.ui.theme.CasinoGoldBorder
import com.example.ui.theme.CasinoGoldDark
import com.example.ui.theme.CasinoGreen
import com.example.ui.theme.CasinoRed
import com.example.ui.theme.CasinoSurface
import com.example.ui.theme.CasinoSurfaceElevated
import com.example.ui.theme.CasinoTextMuted
import com.example.ui.theme.CasinoTextPrimary
import com.example.ui.theme.CasinoTextSecondary
import com.example.ui.theme.CasinoViolet
import com.example.viewmodel.PK999ViewModel

@Composable
fun ReferralScreen(viewModel: PK999ViewModel) {
    val user by viewModel.userProfile.collectAsState()
    val context = LocalContext.current
    val inviteLink = "https://pk999.vip/register?invitationCode=${user.referralCode}"

    fun copyToClipboard(text: String, label: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        viewModel.emitToast("$label copied to clipboard!")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp)
    ) {
        // 1. Referral Header Card
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "AGENT COMMISSION ENGINE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = CasinoGold
                        )
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = CasinoGreen.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "VIP AGENT LEVEL 3",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = CasinoGreen,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Total Commission Earned",
                        fontSize = 12.sp,
                        color = CasinoTextSecondary
                    )
                    Text(
                        text = "Rs ${user.totalCommissionEarned}",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = CasinoGold
                    )
                    Text(
                        text = "Daily settlement directly credited to Master Wallet",
                        fontSize = 11.sp,
                        color = CasinoGreen
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Tier 1 (30%)", fontSize = 11.sp, color = CasinoTextSecondary)
                            Text(text = "${user.tier1Count} Members", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CasinoTextPrimary)
                        }
                        Column {
                            Text(text = "Tier 2 (20%)", fontSize = 11.sp, color = CasinoTextSecondary)
                            Text(text = "${user.tier2Count} Members", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CasinoTextPrimary)
                        }
                        Column {
                            Text(text = "Tier 3 (10%)", fontSize = 11.sp, color = CasinoTextSecondary)
                            Text(text = "${user.tier3Count} Members", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CasinoTextPrimary)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 2. Invite Code & Link Box
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CasinoSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CasinoBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Unique Invitation Code",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CasinoGold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CasinoBackground)
                            .border(1.dp, CasinoGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = user.referralCode,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = CasinoTextPrimary,
                            letterSpacing = 2.sp
                        )

                        Button(
                            onClick = { copyToClipboard(user.referralCode, "Referral Code") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CasinoGold,
                                contentColor = CasinoBackground
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("copy_code_button")
                        ) {
                            Text("Copy Code", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Invitation Link",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CasinoGold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(CasinoBackground)
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inviteLink,
                            fontSize = 11.sp,
                            color = CasinoTextSecondary,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Link",
                            tint = CasinoGold,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { copyToClipboard(inviteLink, "Invitation Link") }
                                .testTag("copy_link_icon")
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 3. Multi-Tier Commission Hierarchy Card
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CasinoSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CasinoBorder, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Multi-Tier Commission Structure",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CasinoGold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    TierRow(
                        tier = "Tier 1: Direct Referrals",
                        percentage = "30%",
                        description = "Earn 30% of platform trading fee on every round your invited player bets.",
                        color = CasinoGreen
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TierRow(
                        tier = "Tier 2: Subordinate Agents",
                        percentage = "20%",
                        description = "Earn 20% on players invited by your tier-1 agents.",
                        color = CasinoGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TierRow(
                        tier = "Tier 3: Secondary Downline",
                        percentage = "10%",
                        description = "Earn 10% on players down to 3rd generation downline.",
                        color = CasinoViolet
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 4. Recent Downline Activity Live Feed
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
                        text = "Real-Time Downline Commission Feed",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CasinoGold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    listOf(
                        Triple("Downline Player 0301***920", "+Rs 180.00", "Tier 1 Bet Turnover (Win Go 30s)"),
                        Triple("Downline Player 0345***412", "+Rs 92.50", "Tier 2 Bet Turnover (Win Go 30s)"),
                        Triple("Downline Player 0312***881", "+Rs 450.00", "Tier 1 First Deposit Commission"),
                        Triple("Downline Player 0333***671", "+Rs 45.00", "Tier 3 Bet Turnover")
                    ).forEach { (player, amt, note) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = player, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CasinoTextPrimary)
                                Text(text = note, fontSize = 10.sp, color = CasinoTextSecondary)
                            }
                            Text(text = amt, fontSize = 13.sp, fontWeight = FontWeight.Black, color = CasinoGreen)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TierRow(tier: String, percentage: String, description: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = CasinoBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = tier, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CasinoTextPrimary)
                Text(text = percentage, fontSize = 15.sp, fontWeight = FontWeight.Black, color = color)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, fontSize = 11.sp, color = CasinoTextSecondary)
        }
    }
}
