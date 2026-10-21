package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameEngine
import com.example.data.model.ActiveBet
import com.example.data.model.AdminRevenue
import com.example.data.model.BetChoiceType
import com.example.data.model.PaymentGateway
import com.example.data.model.PeriodHistoryItem
import com.example.data.model.TransactionRecord
import com.example.data.model.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class PK999ViewModel(application: Application) : AndroidViewModel(application) {

    private val vibrator: Vibrator? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = application.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        vibratorManager?.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        application.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _adminRevenue = MutableStateFlow(AdminRevenue())
    val adminRevenue: StateFlow<AdminRevenue> = _adminRevenue.asStateFlow()

    private var roundCounter = 30093L
    private val _currentPeriodId = MutableStateFlow(GameEngine.generatePeriodId(roundCounter))
    val currentPeriodId: StateFlow<String> = _currentPeriodId.asStateFlow()

    private val _timeRemainingSeconds = MutableStateFlow(30)
    val timeRemainingSeconds: StateFlow<Int> = _timeRemainingSeconds.asStateFlow()

    private val _history = MutableStateFlow<List<PeriodHistoryItem>>(GameEngine.generateInitialHistory())
    val history: StateFlow<List<PeriodHistoryItem>> = _history.asStateFlow()

    private val _activeBets = MutableStateFlow<List<ActiveBet>>(emptyList())
    val activeBets: StateFlow<List<ActiveBet>> = _activeBets.asStateFlow()

    private val _myRecentBets = MutableStateFlow<List<Pair<ActiveBet, Long?>>>(emptyList())
    val myRecentBets: StateFlow<List<Pair<ActiveBet, Long?>>> = _myRecentBets.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionRecord>>(
        listOf(
            TransactionRecord("TX-INIT-01", "BONUS", 69, "SUCCESS", System.currentTimeMillis() - 3600000, "PK999 Welcome Gift"),
            TransactionRecord("TX-INIT-02", "BONUS_LOCKED", 999, "LOCKED", System.currentTimeMillis() - 3600000, "999 Welcome Reward (Min Rs 200 Deposit)")
        )
    )
    val transactions: StateFlow<List<TransactionRecord>> = _transactions.asStateFlow()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated.asStateFlow()

    private val _showGoogleModal = MutableStateFlow(false)
    val showGoogleModal: StateFlow<Boolean> = _showGoogleModal.asStateFlow()

    private val _welcomeBonusAwarded = MutableStateFlow<Long?>(null)
    val welcomeBonusAwarded: StateFlow<Long?> = _welcomeBonusAwarded.asStateFlow()

    private val _lastRoundPopup = MutableStateFlow<Pair<PeriodHistoryItem, Long>?>(null)
    val lastRoundPopup: StateFlow<Pair<PeriodHistoryItem, Long>?> = _lastRoundPopup.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    init {
        startRoundTimer()
    }

    private fun startRoundTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentSec = _timeRemainingSeconds.value
                if (currentSec > 1) {
                    _timeRemainingSeconds.value = currentSec - 1
                    // Audio / Haptic feedback during last 5 seconds (Betting Lock Period)
                    if (currentSec - 1 in 1..5) {
                        triggerTickHaptic()
                    }
                } else {
                    // Round ends! Execute Provably Fair RNG
                    _timeRemainingSeconds.value = 0
                    triggerFinishHaptic()
                    executeRoundSettlement()
                    delay(2000) // Brief delay to show result
                    // Reset to 30s next round
                    roundCounter++
                    _currentPeriodId.value = GameEngine.generatePeriodId(roundCounter)
                    _activeBets.value = emptyList()
                    _timeRemainingSeconds.value = 30
                }
            }
        }
    }

    private fun executeRoundSettlement() {
        val period = _currentPeriodId.value
        val result = GameEngine.calculateRoundResult(period)
        val currentBets = _activeBets.value

        var totalWonThisRound = 0L
        val updatedRecent = _myRecentBets.value.toMutableList()

        for (bet in currentBets) {
            val payout = GameEngine.evaluateBet(bet, result)
            totalWonThisRound += payout
            updatedRecent.add(0, Pair(bet, payout))
        }

        if (totalWonThisRound > 0) {
            _userProfile.value = _userProfile.value.copy(
                balance = _userProfile.value.balance + totalWonThisRound
            )
            _transactions.value = listOf(
                TransactionRecord(
                    id = "WIN-${System.currentTimeMillis() % 100000}",
                    type = "BET_WIN",
                    amount = totalWonThisRound,
                    status = "SUCCESS",
                    gatewayOrNote = "Win Go 30s Period $period"
                )
            ) + _transactions.value
        }

        _myRecentBets.value = updatedRecent.take(30)
        _history.value = (listOf(result) + _history.value).take(50)

        // Show popup if user placed bets in this round
        if (currentBets.isNotEmpty()) {
            _lastRoundPopup.value = Pair(result, totalWonThisRound)
        }
    }

    fun dismissRoundPopup() {
        _lastRoundPopup.value = null
    }

    fun placeBet(choice: BetChoiceType, unitAmount: Long, multiplier: Int): Boolean {
        if (_timeRemainingSeconds.value <= 5) {
            emitToast("Betting is locked for current round!")
            return false
        }

        val totalBet = unitAmount * multiplier
        if (_userProfile.value.balance < totalBet) {
            emitToast("Insufficient balance! Please deposit to continue.")
            return false
        }

        // Deduct balance, add to turnover
        val updatedUser = _userProfile.value.copy(
            balance = _userProfile.value.balance - totalBet,
            currentTurnoverCompleted = _userProfile.value.currentTurnoverCompleted + totalBet
        )
        _userProfile.value = updatedUser

        val newBet = ActiveBet(
            id = "BET-${System.currentTimeMillis() % 1000000}",
            periodId = _currentPeriodId.value,
            choice = choice,
            amount = unitAmount,
            multiplier = multiplier,
            totalBet = totalBet
        )

        _activeBets.value = _activeBets.value + newBet
        triggerBetSuccessHaptic()
        emitToast("Bet of Rs $totalBet placed on ${choice.name}!")
        return true
    }

    fun deposit(gateway: PaymentGateway, amount: Long, txId: String) {
        val user = _userProfile.value
        val newTurnoverReq = user.totalTurnoverRequired + (amount * 29)
        var newBalance = user.balance + amount
        var welcomeUnlocked = user.isWelcomeBonusUnlocked
        var lockedBonus = user.lockedBonus

        // 999 coins locked welcome reward that automatically unlocks upon completing an initial deposit of minimum Rs 200
        if (!welcomeUnlocked && amount >= 200) {
            welcomeUnlocked = true
            newBalance += lockedBonus
            emitToast("Rs 200+ Deposit Confirmed! 999 Coins Welcome Bonus Unlocked!")
        } else {
            emitToast("Deposit of Rs $amount via ${gateway.displayName} Successful!")
        }

        _userProfile.value = user.copy(
            balance = newBalance,
            totalDeposited = user.totalDeposited + amount,
            totalTurnoverRequired = newTurnoverReq,
            isWelcomeBonusUnlocked = welcomeUnlocked,
            lockedBonus = if (welcomeUnlocked) 0L else lockedBonus
        )

        _transactions.value = listOf(
            TransactionRecord(
                id = if (txId.isNotBlank()) txId else "DEP-${System.currentTimeMillis() % 100000}",
                type = "DEPOSIT",
                amount = amount,
                status = "SUCCESS",
                gatewayOrNote = gateway.displayName
            )
        ) + _transactions.value
    }

    fun withdraw(gateway: PaymentGateway, amount: Long, accountNo: String): Boolean {
        val user = _userProfile.value
        if (amount > user.balance) {
            emitToast("Withdrawal amount exceeds available balance!")
            return false
        }

        // 29x Wager / Turnover Requirement Engine
        if (user.currentTurnoverCompleted < user.totalTurnoverRequired) {
            val remaining = user.totalTurnoverRequired - user.currentTurnoverCompleted
            emitToast("Turnover Requirement not met! Need Rs $remaining more betting turnover before withdrawal.")
            return false
        }

        // Admin Revenue: 5% platform commission on every cash-out
        val commission = (amount * 0.05).toLong()
        val netPayout = amount - commission

        _userProfile.value = user.copy(
            balance = user.balance - amount,
            totalWithdrawn = user.totalWithdrawn + amount
        )

        // Route commission to admin node
        _adminRevenue.value = _adminRevenue.value.copy(
            totalCommissionCollected = _adminRevenue.value.totalCommissionCollected + commission,
            totalPlatformTurnover = _adminRevenue.value.totalPlatformTurnover + amount
        )

        _transactions.value = listOf(
            TransactionRecord(
                id = "WD-${System.currentTimeMillis() % 100000}",
                type = "WITHDRAWAL",
                amount = amount,
                status = "PROCESSING",
                gatewayOrNote = "${gateway.displayName} to $accountNo (Fee 5%: Rs $commission, Net: Rs $netPayout)"
            )
        ) + _transactions.value

        emitToast("Withdrawal of Rs $amount submitted! Net: Rs $netPayout (5% fee deducted).")
        return true
    }

    fun completeGoogleSignIn(email: String, displayName: String) {
        val bonuses = listOf(39L, 69L, 99L)
        val selectedBonus = bonuses[Random.nextInt(bonuses.size)]
        val deviceHash = "DEV-" + Integer.toHexString((email + "PK999_SALT").hashCode()).uppercase()

        _userProfile.value = _userProfile.value.copy(
            email = email,
            displayName = displayName,
            balance = _userProfile.value.balance + selectedBonus,
            initialDepositBonus = selectedBonus,
            deviceFingerprint = deviceHash
        )
        _welcomeBonusAwarded.value = selectedBonus
        _isAuthenticated.value = true
        _showGoogleModal.value = false
        emitToast("Welcome $displayName! Rs $selectedBonus dynamic signup bonus credited!")
    }

    fun dismissWelcomeBonusDialog() {
        _welcomeBonusAwarded.value = null
    }

    fun openGoogleAuthModal() {
        _showGoogleModal.value = true
    }

    fun closeGoogleAuthModal() {
        _showGoogleModal.value = false
    }

    fun logout() {
        _isAuthenticated.value = false
    }

    private fun triggerTickHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(40)
            }
        } catch (_: Exception) {}
    }

    private fun triggerBetSuccessHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 30, 40, 60), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(60)
            }
        } catch (_: Exception) {}
    }

    private fun triggerFinishHaptic() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 80, 50, 100), -1))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(120)
            }
        } catch (_: Exception) {}
    }

    fun emitToast(msg: String) {
        viewModelScope.launch {
            _toastEvent.emit(msg)
        }
    }
}
