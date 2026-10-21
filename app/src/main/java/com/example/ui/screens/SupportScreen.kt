package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
import com.example.ui.theme.CasinoTextPrimary
import com.example.ui.theme.CasinoTextSecondary

data class ChatMessage(
    val sender: String, // "USER" or "AGENT"
    val text: String,
    val time: String = "Just now"
)

@Composable
fun SupportScreen() {
    var inputText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("AGENT", "Welcome to PK999 24/7 Official VIP Support! How can we assist you with your 91 Club account today?"),
            ChatMessage("AGENT", "Quick Topics:\n• Deposit: JazzCash & EasyPaisa are instant.\n• Withdrawal: Requires 29x turnover fulfillment.\n• Bonus: 999 coins unlocked automatically on Rs 200+ deposit.")
        )
    }

    fun handleSend(query: String) {
        if (query.isBlank()) return
        messages.add(ChatMessage("USER", query))
        inputText = ""

        val lower = query.lowercase()
        val reply = when {
            lower.contains("deposit") -> "Deposits via JazzCash Direct & EasyPaisa are auto-verified within 30-60 seconds. Make sure your TID is entered correctly."
            lower.contains("withdraw") || lower.contains("turnover") -> "Withdrawals are processed automatically with a 5% platform fee once your 29x wagering turnover is completed."
            lower.contains("bonus") || lower.contains("999") -> "Your 999 coins locked welcome bonus unlocks instantly upon completing your first deposit of at least Rs 200."
            lower.contains("agent") || lower.contains("referral") -> "Agent commission is paid out automatically in 3 tiers: 30% direct, 20% subordinate, and 10% 3rd tier."
            else -> "Thank you for reaching out! A dedicated PK999 VIP Representative has reviewed your account (Status: Active & In Good Standing)."
        }
        messages.add(ChatMessage("AGENT", reply))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(horizontal = 14.dp)
            .padding(bottom = 90.dp, top = 10.dp)
    ) {
        // Support Header Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = CasinoSurface,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CasinoGoldBorder, RoundedCornerShape(14.dp))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(CasinoGold.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = "VIP Support",
                        tint = CasinoGold,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "PK999 24/7 VIP LIVE HELP",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = CasinoGold
                    )
                    Text(
                        text = "Online • Instant Automated Response Engine",
                        fontSize = 11.sp,
                        color = CasinoGreen
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Quick query chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf("Deposit Help", "29x Turnover", "999 Bonus", "Withdrawal").forEach { topic ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CasinoSurfaceElevated,
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, CasinoBorder, RoundedCornerShape(8.dp))
                ) {
                    Button(
                        onClick = { handleSend(topic) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = CasinoGold
                        ),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(text = topic, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Message Log
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages) { msg ->
                val isAgent = msg.sender == "AGENT"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isAgent) Arrangement.Start else Arrangement.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = if (isAgent) 2.dp else 12.dp,
                            bottomEnd = if (isAgent) 12.dp else 2.dp
                        ),
                        color = if (isAgent) CasinoSurface else CasinoGold,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .border(
                                1.dp,
                                if (isAgent) CasinoBorder else CasinoGoldDark,
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isAgent) "VIP Support Agent" else "You",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isAgent) CasinoGold else CasinoBackground
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.text,
                                fontSize = 13.sp,
                                color = if (isAgent) CasinoTextPrimary else CasinoBackground,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Input Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask about deposits, rules, turnover...", fontSize = 12.sp) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CasinoGold,
                    unfocusedBorderColor = CasinoBorder,
                    focusedTextColor = CasinoTextPrimary,
                    unfocusedTextColor = CasinoTextPrimary
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("support_input_field")
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { handleSend(inputText) },
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(CasinoGold)
                    .testTag("support_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = CasinoBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
