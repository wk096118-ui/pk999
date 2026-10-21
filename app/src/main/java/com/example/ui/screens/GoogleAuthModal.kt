package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CasinoBackground
import com.example.ui.theme.CasinoBorder
import com.example.ui.theme.CasinoGold
import com.example.ui.theme.CasinoGreen
import com.example.ui.theme.CasinoRed
import com.example.ui.theme.CasinoSurface
import com.example.ui.theme.CasinoSurfaceElevated
import com.example.ui.theme.CasinoTextPrimary
import com.example.ui.theme.CasinoTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleAuthModal(
    onDismiss: () -> Unit,
    onAccountSelected: (email: String, name: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CasinoSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Google G Icon
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "G",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF4285F4)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Sign in with Google",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = CasinoTextPrimary
                        )
                        Text(
                            text = "to continue to PK999 Gaming Arena",
                            fontSize = 12.sp,
                            color = CasinoTextSecondary
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = CasinoTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hardware Hash Banner
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CasinoBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CasinoBorder, RoundedCornerShape(10.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Device Hash",
                        tint = CasinoGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Device Hash: DEV-PK999-492F (Multi-Account Fraud Guarded)",
                        fontSize = 11.sp,
                        color = CasinoTextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Choose an account",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CasinoGold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Account 1 (matches user email in prompt)
            AccountItem(
                email = "younaskha0909@gmail.com",
                name = "Younas Khan",
                avatarLetter = "Y",
                avatarColor = Color(0xFF34A853),
                onClick = { onAccountSelected("younaskha0909@gmail.com", "Younas Khan") }
            )

            HorizontalDivider(
                color = CasinoBorder.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Account 2
            AccountItem(
                email = "pk999.vip.player@gmail.com",
                name = "PK999 VIP Pro",
                avatarLetter = "P",
                avatarColor = Color(0xFFEA4335),
                onClick = { onAccountSelected("pk999.vip.player@gmail.com", "PK999 VIP Pro") }
            )

            HorizontalDivider(
                color = CasinoBorder.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Add Another Account
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAccountSelected("new.gamer@pk999.vip", "New High Roller")
                    }
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CasinoSurfaceElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Account",
                        tint = CasinoTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = "Add another Google account",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = CasinoTextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "To continue, Google will share your name, email address, language preference, and profile picture with PK999. See PK999's Privacy Policy and Terms of Service.",
                fontSize = 11.sp,
                color = CasinoTextSecondary.copy(alpha = 0.7f),
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun AccountItem(
    email: String,
    name: String,
    avatarLetter: String,
    avatarColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = avatarLetter,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = CasinoTextPrimary
            )
            Text(
                text = email,
                fontSize = 13.sp,
                color = CasinoTextSecondary
            )
        }
    }
}

@Composable
fun WelcomeBonusCelebrationDialog(
    bonusAmount: Long,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CasinoSurface,
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(CasinoGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        contentDescription = "Bonus",
                        tint = CasinoGold,
                        modifier = Modifier.size(42.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "WELCOME BONUS AWARDED!",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = CasinoGold,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Congratulations! Your Google ID registered successfully.",
                    fontSize = 13.sp,
                    color = CasinoTextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CasinoBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CasinoGold, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Rs $bonusAmount",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = CasinoGreen
                        )
                        Text(
                            text = "Free Dynamic Welcome Cash Credited",
                            fontSize = 12.sp,
                            color = CasinoTextPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "+ 999 Coins Locked in Vault\n(Unlocks on first Rs 200 deposit)",
                            fontSize = 11.sp,
                            color = CasinoGold,
                            textAlign = TextAlign.Center
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
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("claim_bonus_confirm_button")
            ) {
                Text(
                    text = "Enter Game Arena Now",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }
    )
}
