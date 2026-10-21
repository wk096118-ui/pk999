package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.CasinoGreen
import com.example.ui.theme.CasinoRed
import com.example.ui.theme.CasinoViolet

enum class BetChoiceType {
    GREEN, VIOLET, RED,
    NUMBER_0, NUMBER_1, NUMBER_2, NUMBER_3, NUMBER_4,
    NUMBER_5, NUMBER_6, NUMBER_7, NUMBER_8, NUMBER_9,
    BIG, SMALL
}

data class BetChoice(
    val type: BetChoiceType,
    val label: String,
    val multiplier: Float,
    val primaryColor: Color,
    val secondaryColor: Color? = null
)

data class ActiveBet(
    val id: String,
    val periodId: String,
    val choice: BetChoiceType,
    val amount: Long,
    val multiplier: Int,
    val totalBet: Long,
    val timestamp: Long = System.currentTimeMillis()
)

data class PeriodHistoryItem(
    val periodId: String,
    val number: Int,
    val isBig: Boolean,
    val primaryColor: Color,
    val secondaryColor: Color? = null,
    val colorName: String,
    val timestamp: Long = System.currentTimeMillis(),
    val serverHash: String = ""
)

data class UserProfile(
    val uid: String = "PK999-784920",
    val email: String = "younaskha0909@gmail.com",
    val displayName: String = "Younas Khan (VIP 2)",
    val photoUrl: String = "",
    val balance: Long = 2500L, // Rs in balance
    val lockedBonus: Long = 999L, // 999 coins locked welcome reward
    val isWelcomeBonusUnlocked: Boolean = false,
    val initialDepositBonus: Long = 69L, // Random welcome bonus: 39, 69, 99
    val totalDeposited: Long = 0L,
    val totalWithdrawn: Long = 0L,
    val totalTurnoverRequired: Long = 0L, // 29x requirement
    val currentTurnoverCompleted: Long = 0L,
    val deviceFingerprint: String = "DEV-PK999-ARM64-SHA256-492F",
    val referralCode: String = "PK999INVITE888",
    val referredBy: String = "PK999-OFFICIAL",
    val tier1Count: Int = 14,
    val tier2Count: Int = 38,
    val tier3Count: Int = 92,
    val totalCommissionEarned: Long = 18450L
)

data class AdminRevenue(
    val totalPlatformTurnover: Long = 14850000L,
    val totalCommissionCollected: Long = 742500L, // 5% platform commission
    val activeRoundsCount: Long = 34912L,
    val platformReserveFund: Long = 4920000L
)

enum class PaymentGateway(val displayName: String, val minDeposit: Long, val tag: String) {
    JAZZCASH_DIRECT("JazzCash Direct", 200, "Instant OTP"),
    EASYPAISA_DIRECT("EasyPaisa Direct", 200, "One-Tap Auto"),
    EASYPAISA_RAAST("EasyPaisa QR Raast", 100, "0% Fee Govt Raast"),
    USDT_TRC20("USDT Crypto (TRC20)", 1000, "Global Fast 1-3m")
}

data class TransactionRecord(
    val id: String,
    val type: String, // "DEPOSIT", "WITHDRAWAL", "BET_WIN", "BET_LOSS", "BONUS", "COMMISSION"
    val amount: Long,
    val status: String, // "SUCCESS", "PENDING", "PROCESSING"
    val timestamp: Long = System.currentTimeMillis(),
    val gatewayOrNote: String
)
