package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminRevenue
import com.example.ui.theme.CasinoBackground
import com.example.ui.theme.CasinoBorder
import com.example.ui.theme.CasinoGold
import com.example.ui.theme.CasinoGreen
import com.example.ui.theme.CasinoRed
import com.example.ui.theme.CasinoSurface
import com.example.ui.theme.CasinoTextPrimary
import com.example.ui.theme.CasinoTextSecondary

@Composable
fun AdminRevenueDialog(
    revenue: AdminRevenue,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CasinoSurface,
        title = {
            Column {
                Text(
                    text = "FIREBASE /ADMIN/REVENUE ENGINE",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    color = CasinoGold
                )
                Text(
                    text = "Real-Time Platform Commission & Turnover Node",
                    fontSize = 11.sp,
                    color = CasinoTextSecondary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                AdminStatCard(
                    title = "Total Platform Turnover",
                    value = "Rs ${revenue.totalPlatformTurnover}",
                    subtitle = "Aggregated across all Win Go & 91-Club arenas",
                    color = CasinoGold
                )

                AdminStatCard(
                    title = "5% Platform Commission Collected",
                    value = "Rs ${revenue.totalCommissionCollected}",
                    subtitle = "Automatically deducted on all verified cash-outs",
                    color = CasinoGreen
                )

                AdminStatCard(
                    title = "Platform Reserve Liquidity Fund",
                    value = "Rs ${revenue.platformReserveFund}",
                    subtitle = "Automated instant payout security pool",
                    color = Color(0xFF00E5FF)
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = CasinoBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CasinoBorder, RoundedCornerShape(10.dp))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Provably Fair Seed Node",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CasinoGold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "SHA256(periodId + clientSeed + 91CLUB_ENGINE)",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = CasinoTextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Completed Rounds: ${revenue.activeRoundsCount}",
                            fontSize = 11.sp,
                            color = CasinoTextPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CasinoGold,
                    contentColor = CasinoBackground
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Close Inspector", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun AdminStatCard(
    title: String,
    value: String,
    subtitle: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = CasinoBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = title, fontSize = 11.sp, color = CasinoTextSecondary)
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = color)
            Text(text = subtitle, fontSize = 10.sp, color = CasinoTextSecondary.copy(alpha = 0.8f))
        }
    }
}
