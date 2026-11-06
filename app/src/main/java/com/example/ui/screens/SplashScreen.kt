package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CasinoBackground
import com.example.ui.theme.CasinoBorder
import com.example.ui.theme.CasinoGold
import com.example.ui.theme.CasinoGoldDark
import com.example.ui.theme.CasinoGreen
import com.example.ui.theme.CasinoRed
import com.example.ui.theme.CasinoSurface
import com.example.ui.theme.CasinoSurfaceElevated
import com.example.ui.theme.CasinoTextPrimary
import com.example.ui.theme.CasinoTextSecondary
import com.example.ui.theme.CasinoViolet

@Composable
fun SplashScreen(
    onLoginClick: () -> Unit = {},
    onGuestEnterClick: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "crownPulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF13131F),
                        CasinoBackground,
                        Color(0xFF0F0F1A)
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Animated Crown Logo
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(pulseScale),
                contentAlignment = Alignment.Center
            ) {
                // Golden Glow Ring
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    CasinoGold.copy(alpha = 0.35f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Image(
                    painter = painterResource(id = R.drawable.pk999_app_icon),
                    contentDescription = "PK999 Crown Logo",
                    modifier = Modifier
                        .size(108.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .border(2.dp, CasinoGold, RoundedCornerShape(28.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Brand Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "PK",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CasinoGreen,
                    letterSpacing = 2.sp
                )
                Text(
                    text = "999",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = CasinoGold,
                    letterSpacing = 2.sp
                )
            }

            Text(
                text = "91 CLUB OFFICIAL GAMING ARENA",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = CasinoGoldDark,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Color Prediction • Win Go 30s • Provably Fair RNG",
                fontSize = 13.sp,
                color = CasinoTextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Bonus Card Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CasinoSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CasinoGoldDark.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Bonus",
                            tint = CasinoGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "EXCLUSIVE SIGNUP REWARDS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = CasinoGold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Instant Welcome Bonus: Rs 39 - Rs 99\n+ 999 Coins Locked Welcome Gift",
                        fontSize = 13.sp,
                        color = CasinoTextPrimary,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Protected",
                            tint = CasinoGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Hardware Fingerprint Anti-Fraud Guarded",
                            fontSize = 11.sp,
                            color = CasinoTextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Primary Username & Password Login Button
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CasinoGold,
                    contentColor = Color(0xFF1F1F1F)
                ),
                shape = RoundedCornerShape(28.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("login_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Login",
                        tint = Color(0xFF1F1F1F),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Login with Username & Password",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Demo / Quick Access Button
            OutlinedButton(
                onClick = onGuestEnterClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = CasinoGold
                ),
                shape = RoundedCornerShape(28.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(listOf(CasinoGold, CasinoGreen))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("guest_enter_button")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Enter",
                        tint = CasinoGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Instant Demo Play (Rs 2,500 Loaded)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "By signing in, you agree to 91 Club 18+ Presale Rules and Fair Play Terms.",
                fontSize = 11.sp,
                color = CasinoTextSecondary.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}
