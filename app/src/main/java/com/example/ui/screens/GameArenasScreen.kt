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
import androidx.compose.material.icons.filled.AvTimer
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Token
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

data class GameArenaItem(
    val title: String,
    val subtitle: String,
    val tag: String,
    val icon: ImageVector,
    val iconColor: Color,
    val isLive: Boolean = true,
    val isCurrent: Boolean = false
)

@Composable
fun GameArenasScreen(
    onSelectArena: (String) -> Unit
) {
    val arenas = listOf(
        GameArenaItem(
            title = "Win Go 30s",
            subtitle = "Superfast 30s Color & Number Prediction",
            tag = "ACTIVE ARENA",
            icon = Icons.Default.AvTimer,
            iconColor = CasinoGold,
            isCurrent = true
        ),
        GameArenaItem(
            title = "Win Go 1 Min",
            subtitle = "Classic 60-Second Color Trading Round",
            tag = "HIGH TURNOVER",
            icon = Icons.Default.Casino,
            iconColor = CasinoGreen
        ),
        GameArenaItem(
            title = "Win Go 3 Min",
            subtitle = "Strategic 180s Round with Advanced Charts",
            tag = "VIP POPULAR",
            icon = Icons.Default.Gamepad,
            iconColor = CasinoViolet
        ),
        GameArenaItem(
            title = "Trx Hash Win Go",
            subtitle = "Block-Hash Provably Fair Color Lottery",
            tag = "BLOCKCHAIN RNG",
            icon = Icons.Default.Token,
            iconColor = Color(0xFF00E5FF)
        ),
        GameArenaItem(
            title = "Aviator / Crash Arena",
            subtitle = "Multiplier Curve Flight & Fast Cash-Out",
            tag = "HOT MULTIPLIER",
            icon = Icons.Default.FlightTakeoff,
            iconColor = CasinoRed
        ),
        GameArenaItem(
            title = "5D Lottery",
            subtitle = "5 Digit Ball Draw with 10,000x Jackpot",
            tag = "HIGH PAYOUT",
            icon = Icons.Default.Casino,
            iconColor = CasinoGold
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "PK999 ALL GAME ARENAS",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = CasinoGold
            )
            Text(
                text = "Official 91-Club Provably Fair Lottery & Fast Trading Engines",
                fontSize = 12.sp,
                color = CasinoTextSecondary
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(arenas.size) { index ->
            val arena = arenas[index]
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (arena.isCurrent) CasinoSurfaceElevated else CasinoSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectArena(arena.title) }
                    .border(
                        1.dp,
                        if (arena.isCurrent) CasinoGoldBorder else CasinoBorder,
                        RoundedCornerShape(14.dp)
                    )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(arena.iconColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = arena.icon,
                                contentDescription = arena.title,
                                tint = arena.iconColor,
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = arena.title,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CasinoTextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (arena.isCurrent) CasinoGreen.copy(alpha = 0.2f) else CasinoGold.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = arena.tag,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (arena.isCurrent) CasinoGreen else CasinoGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = arena.subtitle,
                                fontSize = 11.sp,
                                color = CasinoTextSecondary
                            )
                        }
                    }

                    Button(
                        onClick = { onSelectArena(arena.title) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (arena.isCurrent) CasinoGold else CasinoBackground,
                            contentColor = if (arena.isCurrent) CasinoBackground else CasinoGold
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = if (arena.isCurrent) "Enter" else "Play",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
