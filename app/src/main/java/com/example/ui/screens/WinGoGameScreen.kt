package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ActiveBet
import com.example.data.model.BetChoiceType
import com.example.data.model.PeriodHistoryItem
import com.example.ui.theme.CasinoBackground
import com.example.ui.theme.CasinoBorder
import com.example.ui.theme.CasinoGold
import com.example.ui.theme.CasinoGoldBorder
import com.example.ui.theme.CasinoGoldDark
import com.example.ui.theme.CasinoGreen
import com.example.ui.theme.CasinoRed
import com.example.ui.theme.CasinoRedDark
import com.example.ui.theme.CasinoSurface
import com.example.ui.theme.CasinoSurfaceCard
import com.example.ui.theme.CasinoSurfaceElevated
import com.example.ui.theme.CasinoTextMuted
import com.example.ui.theme.CasinoTextPrimary
import com.example.ui.theme.CasinoTextSecondary
import com.example.ui.theme.CasinoViolet
import com.example.viewmodel.PK999ViewModel

@Composable
fun WinGoGameScreen(
    viewModel: PK999ViewModel,
    onNavigateDeposit: () -> Unit,
    onNavigateWithdraw: () -> Unit
) {
    val user by viewModel.userProfile.collectAsState()
    val periodId by viewModel.currentPeriodId.collectAsState()
    val timeLeft by viewModel.timeRemainingSeconds.collectAsState()
    val history by viewModel.history.collectAsState()
    val activeBets by viewModel.activeBets.collectAsState()
    val myRecentBets by viewModel.myRecentBets.collectAsState()
    val lastPopup by viewModel.lastRoundPopup.collectAsState()

    val isLocked = timeLeft <= 5
    var selectedBetChoice by remember { mutableStateOf<BetChoiceType?>(null) }
    var showHowToPlay by remember { mutableStateOf(false) }
    var selectedHistoryTab by remember { mutableIntStateOf(0) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp)
    ) {
        // 1. Top Wallet & VIP Banner
        item {
            WalletHeaderCard(
                balance = user.balance,
                lockedBonus = user.lockedBonus,
                isBonusUnlocked = user.isWelcomeBonusUnlocked,
                uid = user.uid,
                onDepositClick = onNavigateDeposit,
                onWithdrawClick = onNavigateWithdraw
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 2. Casino Hero Graphic Banner
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = CasinoSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .border(1.dp, CasinoBorder, RoundedCornerShape(14.dp))
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.img_pk999_banner),
                        contentDescription = "Casino Banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    // Dark luxury gradient overlay for legibility
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.75f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .align(Alignment.CenterStart)
                    ) {
                        Text(
                            text = "WIN GO 30S ARENA",
                            color = CasinoGold,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Ultra-Fast Color & Number Trading",
                            color = CasinoTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Green: 2x • Violet: 4.5x • Red: 2x • Number: 9x",
                            color = CasinoGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // 3. 30-Second Real-Time Countdown Card
        item {
            CountdownTimerCard(
                periodId = periodId,
                timeLeft = timeLeft,
                isLocked = isLocked,
                onHowToPlayClick = { showHowToPlay = true }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 4. Color & Number Betting Grid (91 Club Layout)
        item {
            BettingGridSection(
                isLocked = isLocked,
                onSelectChoice = { choice ->
                    if (!isLocked) {
                        selectedBetChoice = choice
                    } else {
                        viewModel.emitToast("Betting closed for current round!")
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 5. Active Bets For This Period
        if (activeBets.isNotEmpty()) {
            item {
                ActiveBetsSummaryCard(activeBets = activeBets)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // 6. History, Trend Chart & My Bets Tabs
        item {
            HistoryTabsSection(
                selectedTab = selectedHistoryTab,
                onTabSelect = { selectedHistoryTab = it },
                history = history,
                myRecentBets = myRecentBets
            )
        }
    }

    // Bet Confirmation Sheet
    selectedBetChoice?.let { choice ->
        BetConfirmationBottomSheet(
            choice = choice,
            userBalance = user.balance,
            onDismiss = { selectedBetChoice = null },
            onConfirmBet = { unitAmount, multiplier ->
                val success = viewModel.placeBet(choice, unitAmount, multiplier)
                if (success) {
                    selectedBetChoice = null
                }
            }
        )
    }

    // Last Round Settlement Dialog
    lastPopup?.let { (result, totalWon) ->
        RoundResultCelebrationDialog(
            result = result,
            totalWon = totalWon,
            onDismiss = { viewModel.dismissRoundPopup() }
        )
    }

    // How to Play Dialog
    if (showHowToPlay) {
        HowToPlayDialog(onDismiss = { showHowToPlay = false })
    }
}

@Composable
private fun WalletHeaderCard(
    balance: Long,
    lockedBonus: Long,
    isBonusUnlocked: Boolean,
    uid: String,
    onDepositClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CasinoGoldBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(CasinoGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👑",
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "UID: $uid",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CasinoGold
                        )
                        Text(
                            text = "VIP Level: Gold 2",
                            fontSize = 10.sp,
                            color = CasinoTextSecondary
                        )
                    }
                }

                // Locked Bonus Vault Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isBonusUnlocked) CasinoGreen.copy(alpha = 0.2f) else CasinoGoldDark.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isBonusUnlocked) Icons.Default.CheckCircle else Icons.Default.Lock,
                            contentDescription = "Bonus status",
                            tint = if (isBonusUnlocked) CasinoGreen else CasinoGold,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBonusUnlocked) "999 Bonus Active" else "999 Coins Vault",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isBonusUnlocked) CasinoGreen else CasinoGold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Balance",
                        fontSize = 11.sp,
                        color = CasinoTextSecondary
                    )
                    Text(
                        text = "Rs $balance",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Black,
                        color = CasinoGold
                    )
                }

                Row {
                    Button(
                        onClick = onDepositClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CasinoGreen,
                            contentColor = CasinoBackground
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .testTag("header_deposit_button")
                    ) {
                        Text(
                            text = "Deposit",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onWithdrawClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CasinoSurfaceElevated,
                            contentColor = CasinoGold
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier
                            .height(38.dp)
                            .border(1.dp, CasinoGold.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .testTag("header_withdraw_button")
                    ) {
                        Text(
                            text = "Withdraw",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CountdownTimerCard(
    periodId: String,
    timeLeft: Int,
    isLocked: Boolean,
    onHowToPlayClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseLock")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "lockFlash"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isLocked) CasinoRed.copy(alpha = pulseAlpha) else CasinoBorder,
                RoundedCornerShape(16.dp)
            )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = CasinoGold,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Period",
                        fontSize = 13.sp,
                        color = CasinoTextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = periodId,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = CasinoTextPrimary,
                        fontFamily = FontFamily.Monospace
                    )
                }

                IconButton(
                    onClick = onHowToPlayClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                        contentDescription = "Rules",
                        tint = CasinoGold,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Timer display & Progress Arc
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Time Remaining",
                        fontSize = 12.sp,
                        color = if (isLocked) CasinoRed else CasinoTextSecondary,
                        fontWeight = if (isLocked) FontWeight.Bold else FontWeight.Normal
                    )

                    val secondsStr = timeLeft.toString().padStart(2, '0')
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isLocked) CasinoRedDark else CasinoBackground)
                                .border(1.dp, if (isLocked) CasinoRed else CasinoBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "0",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isLocked) Color.White else CasinoGold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isLocked) CasinoRedDark else CasinoBackground)
                                .border(1.dp, if (isLocked) CasinoRed else CasinoBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "0",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isLocked) Color.White else CasinoGold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = ":",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = CasinoGold,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isLocked) CasinoRedDark else CasinoBackground)
                                .border(1.dp, if (isLocked) CasinoRed else CasinoBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = secondsStr[0].toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isLocked) Color.White else CasinoGold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isLocked) CasinoRedDark else CasinoBackground)
                                .border(1.dp, if (isLocked) CasinoRed else CasinoBorder, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = secondsStr[1].toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isLocked) Color.White else CasinoGold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // Pulsing Circular Timer Graphic
                Box(
                    modifier = Modifier.size(54.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { timeLeft / 30f },
                        modifier = Modifier.fillMaxSize(),
                        color = if (isLocked) CasinoRed else CasinoGreen,
                        trackColor = CasinoBackground,
                        strokeWidth = 5.dp
                    )
                    Text(
                        text = "${timeLeft}s",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isLocked) CasinoRed else CasinoGold
                    )
                }
            }

            if (isLocked) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CasinoRedDark.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = CasinoRed,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BETTING LOCKED - CALCULATING RNG RESULTS...",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BettingGridSection(
    isLocked: Boolean,
    onSelectChoice: (BetChoiceType) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CasinoBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "HIGH-FREQUENCY BETTING ARENA",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CasinoGold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 1. Color Selectors: Green (2x), Violet (4.5x), Red (2x)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Green Button
                ColorBetButton(
                    label = "Green",
                    multiplier = "2X",
                    gradient = Brush.verticalGradient(listOf(CasinoGreen, Color(0xFF00B248))),
                    isLocked = isLocked,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectChoice(BetChoiceType.GREEN) }
                )

                // Violet Button
                ColorBetButton(
                    label = "Violet",
                    multiplier = "4.5X",
                    gradient = Brush.verticalGradient(listOf(CasinoViolet, Color(0xFF7B1FA2))),
                    isLocked = isLocked,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectChoice(BetChoiceType.VIOLET) }
                )

                // Red Button
                ColorBetButton(
                    label = "Red",
                    multiplier = "2X",
                    gradient = Brush.verticalGradient(listOf(CasinoRed, Color(0xFFCC0000))),
                    isLocked = isLocked,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectChoice(BetChoiceType.RED) }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Number Buttons: 0 to 9 with exact 91-Club multi-color styling & 9X Payout
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CasinoBackground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "NUMBER SELECTOR (9X PAYOUT)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CasinoTextSecondary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Row 1: Numbers 0 to 4
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NumberBallButton(
                            number = 0,
                            isSplit = true,
                            splitColors = Pair(CasinoRed, CasinoViolet),
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_0) }
                        )
                        NumberBallButton(
                            number = 1,
                            isSplit = false,
                            singleColor = CasinoGreen,
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_1) }
                        )
                        NumberBallButton(
                            number = 2,
                            isSplit = false,
                            singleColor = CasinoRed,
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_2) }
                        )
                        NumberBallButton(
                            number = 3,
                            isSplit = false,
                            singleColor = CasinoGreen,
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_3) }
                        )
                        NumberBallButton(
                            number = 4,
                            isSplit = false,
                            singleColor = CasinoRed,
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_4) }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row 2: Numbers 5 to 9
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NumberBallButton(
                            number = 5,
                            isSplit = true,
                            splitColors = Pair(CasinoGreen, CasinoViolet),
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_5) }
                        )
                        NumberBallButton(
                            number = 6,
                            isSplit = false,
                            singleColor = CasinoRed,
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_6) }
                        )
                        NumberBallButton(
                            number = 7,
                            isSplit = false,
                            singleColor = CasinoGreen,
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_7) }
                        )
                        NumberBallButton(
                            number = 8,
                            isSplit = false,
                            singleColor = CasinoRed,
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_8) }
                        )
                        NumberBallButton(
                            number = 9,
                            isSplit = false,
                            singleColor = CasinoGreen,
                            isLocked = isLocked,
                            onClick = { onSelectChoice(BetChoiceType.NUMBER_9) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Big & Small Selectors: 2X Payout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Big Button (5-9)
                BigSmallButton(
                    label = "Big (5-9)",
                    multiplier = "2X",
                    color = Color(0xFFFF9800),
                    isLocked = isLocked,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectChoice(BetChoiceType.BIG) }
                )

                // Small Button (0-4)
                BigSmallButton(
                    label = "Small (0-4)",
                    multiplier = "2X",
                    color = Color(0xFF2196F3),
                    isLocked = isLocked,
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectChoice(BetChoiceType.SMALL) }
                )
            }
        }
    }
}

@Composable
private fun ColorBetButton(
    label: String,
    multiplier: String,
    gradient: Brush,
    isLocked: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
            .height(54.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !isLocked, onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = multiplier,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CasinoGold
                )
            }
        }
    }
}

@Composable
private fun NumberBallButton(
    number: Int,
    isSplit: Boolean,
    singleColor: Color = Color.Transparent,
    splitColors: Pair<Color, Color> = Pair(Color.Transparent, Color.Transparent),
    isLocked: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .clip(CircleShape)
            .border(2.dp, CasinoBorder, CircleShape)
            .clickable(enabled = !isLocked, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSplit) {
            // Half & Half split diagonal
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            0.0f to splitColors.first,
                            0.5f to splitColors.first,
                            0.51f to splitColors.second,
                            1.0f to splitColors.second
                        )
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(singleColor)
            )
        }

        Text(
            text = number.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
    }
}

@Composable
private fun BigSmallButton(
    label: String,
    multiplier: String,
    color: Color,
    isLocked: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color,
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = !isLocked, onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = multiplier,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = CasinoGold
                )
            }
        }
    }
}

@Composable
private fun ActiveBetsSummaryCard(activeBets: List<ActiveBet>) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CasinoGold.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "MY CURRENT ROUND BETS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = CasinoGold
                )
                Text(
                    text = "Total: Rs ${activeBets.sumOf { it.totalBet }}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = CasinoGreen
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                activeBets.forEach { bet ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(CasinoBackground)
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = bet.choice.name.replace("NUMBER_", "Number "),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CasinoTextPrimary
                        )
                        Text(
                            text = "Rs ${bet.amount} x ${bet.multiplier} = Rs ${bet.totalBet}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CasinoGold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryTabsSection(
    selectedTab: Int,
    onTabSelect: (Int) -> Unit,
    history: List<PeriodHistoryItem>,
    myRecentBets: List<Pair<ActiveBet, Long?>>
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CasinoBorder, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CasinoSurface,
                contentColor = CasinoGold,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CasinoGold,
                        height = 3.dp
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { onTabSelect(0) },
                    text = {
                        Text(
                            text = "Game History",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { onTabSelect(1) },
                    text = {
                        Text(
                            text = "Trend Chart",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { onTabSelect(2) },
                    text = {
                        Text(
                            text = "My Bets (${myRecentBets.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (selectedTab) {
                0 -> GameHistoryTable(history = history)
                1 -> TrendChartSection(history = history)
                2 -> MyBetsHistoryList(myRecentBets = myRecentBets)
            }
        }
    }
}

@Composable
private fun GameHistoryTable(history: List<PeriodHistoryItem>) {
    Column {
        // Table Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Period",
                fontSize = 12.sp,
                color = CasinoTextMuted,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1.4f)
            )
            Text(
                text = "Number",
                fontSize = 12.sp,
                color = CasinoTextMuted,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.8f)
            )
            Text(
                text = "Big/Small",
                fontSize = 12.sp,
                color = CasinoTextMuted,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(0.9f)
            )
            Text(
                text = "Color",
                fontSize = 12.sp,
                color = CasinoTextMuted,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        history.take(15).forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.periodId.takeLast(7),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CasinoTextPrimary,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.weight(1.4f)
                )

                // Number ball
                Box(
                    modifier = Modifier
                        .weight(0.8f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.number.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = item.primaryColor
                    )
                }

                // Big / Small badge
                Box(
                    modifier = Modifier.weight(0.9f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (item.isBig) "Big" else "Small",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isBig) Color(0xFFFF9800) else Color(0xFF2196F3)
                    )
                }

                // Color dots
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(item.primaryColor)
                    )
                    item.secondaryColor?.let { sec ->
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(sec)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TrendChartSection(history: List<PeriodHistoryItem>) {
    Column {
        Text(
            text = "Live Trend Sequence (Last 20 Rounds)",
            fontSize = 12.sp,
            color = CasinoGold,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Horizontal visual stream of colored result balls
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(history.take(20)) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(item.primaryColor)
                            .border(1.dp, CasinoBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.number.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (item.isBig) "B" else "S",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isBig) Color(0xFFFF9800) else Color(0xFF2196F3)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Big/Small & Red/Green stats
        val total = history.take(20).size
        val redCount = history.take(20).count { it.colorName.contains("Red") }
        val greenCount = history.take(20).count { it.colorName.contains("Green") }
        val bigCount = history.take(20).count { it.isBig }
        val smallCount = total - bigCount

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatPill(label = "Red Rate", value = "${if (total > 0) (redCount * 100 / total) else 50}%", color = CasinoRed)
            StatPill(label = "Green Rate", value = "${if (total > 0) (greenCount * 100 / total) else 50}%", color = CasinoGreen)
            StatPill(label = "Big Rate", value = "${if (total > 0) (bigCount * 100 / total) else 50}%", color = Color(0xFFFF9800))
            StatPill(label = "Small Rate", value = "${if (total > 0) (smallCount * 100 / total) else 50}%", color = Color(0xFF2196F3))
        }
    }
}

@Composable
private fun StatPill(label: String, value: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = CasinoBackground,
        modifier = Modifier.border(1.dp, CasinoBorder, RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 10.sp, color = CasinoTextSecondary)
            Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun MyBetsHistoryList(myRecentBets: List<Pair<ActiveBet, Long?>>) {
    if (myRecentBets.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No bets placed yet. Select Green, Red, Violet, or a Number to place your first bet!",
                fontSize = 12.sp,
                color = CasinoTextSecondary,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            myRecentBets.forEach { (bet, payout) ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = CasinoBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Period: ${bet.periodId.takeLast(7)}",
                                fontSize = 11.sp,
                                color = CasinoTextSecondary
                            )
                            Text(
                                text = "Bet: ${bet.choice.name.replace("NUMBER_", "No. ")} (Rs ${bet.totalBet})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CasinoTextPrimary
                            )
                        }

                        if (payout == null) {
                            Text(
                                text = "Pending...",
                                fontSize = 12.sp,
                                color = CasinoGold,
                                fontWeight = FontWeight.Bold
                            )
                        } else if (payout > 0) {
                            Text(
                                text = "+Rs $payout (WIN)",
                                fontSize = 14.sp,
                                color = CasinoGreen,
                                fontWeight = FontWeight.Black
                            )
                        } else {
                            Text(
                                text = "-Rs ${bet.totalBet} (LOSS)",
                                fontSize = 13.sp,
                                color = CasinoRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BetConfirmationBottomSheet(
    choice: BetChoiceType,
    userBalance: Long,
    onDismiss: () -> Unit,
    onConfirmBet: (unitAmount: Long, multiplier: Int) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val presets = listOf(10L, 50L, 100L, 500L, 1000L, 5000L, 10000L)
    var selectedUnitAmount by remember { mutableLongStateOf(10L) }
    var multiplier by remember { mutableIntStateOf(1) }
    val totalBet = selectedUnitAmount * multiplier

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CasinoSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Win Go 30s • Bet Confirmation",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = CasinoGold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Selected: ${choice.name.replace("NUMBER_", "Number ")}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CasinoTextPrimary
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Presets row
            Text(
                text = "Contract Money (Unit Amount)",
                fontSize = 12.sp,
                color = CasinoTextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(presets) { preset ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (selectedUnitAmount == preset) CasinoGold else CasinoBackground,
                        modifier = Modifier
                            .clickable { selectedUnitAmount = preset }
                            .border(1.dp, if (selectedUnitAmount == preset) CasinoGold else CasinoBorder, RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = "Rs $preset",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedUnitAmount == preset) CasinoBackground else CasinoTextPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Multiplier Stepper: x1, x5, x10, x20, x50, x100
            Text(
                text = "Number of Bets (Multiplier)",
                fontSize = 12.sp,
                color = CasinoTextSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // - / + Stepper
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (multiplier > 1) multiplier-- },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CasinoBackground)
                            .border(1.dp, CasinoBorder, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus", tint = CasinoGold)
                    }

                    Text(
                        text = "x$multiplier",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = CasinoGold,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = { multiplier++ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CasinoBackground)
                            .border(1.dp, CasinoBorder, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Plus", tint = CasinoGold)
                    }
                }

                // Fast multipliers
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(1, 5, 10, 20, 50, 100).forEach { m ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (multiplier == m) CasinoGold else CasinoBackground,
                            modifier = Modifier.clickable { multiplier = m }
                        ) {
                            Text(
                                text = "x$m",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (multiplier == m) CasinoBackground else CasinoTextSecondary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Total Bet & Balance Check
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = CasinoBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Total Bet Amount", fontSize = 12.sp, color = CasinoTextSecondary)
                        Text(
                            text = "Rs $totalBet",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = if (totalBet > userBalance) CasinoRed else CasinoGreen
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Available Balance", fontSize = 12.sp, color = CasinoTextSecondary)
                        Text(
                            text = "Rs $userBalance",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CasinoGold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = { onConfirmBet(selectedUnitAmount, multiplier) },
                enabled = totalBet <= userBalance,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CasinoGold,
                    contentColor = CasinoBackground
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_bet_action_button")
            ) {
                Text(
                    text = if (totalBet <= userBalance) "Confirm Bet (Rs $totalBet)" else "Insufficient Balance",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun RoundResultCelebrationDialog(
    result: PeriodHistoryItem,
    totalWon: Long,
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
                Text(
                    text = if (totalWon > 0) "CONGRATULATIONS!" else "ROUND SETTLED",
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = if (totalWon > 0) CasinoGreen else CasinoGold
                )
                Text(
                    text = "Period ${result.periodId.takeLast(7)} Results",
                    fontSize = 12.sp,
                    color = CasinoTextSecondary
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Winning Ball
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(result.primaryColor)
                        .border(3.dp, CasinoGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = result.number.toString(),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = result.colorName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = result.primaryColor
                    )
                    Text(
                        text = "•",
                        color = CasinoTextSecondary
                    )
                    Text(
                        text = if (result.isBig) "Big (5-9)" else "Small (0-4)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (result.isBig) Color(0xFFFF9800) else Color(0xFF2196F3)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CasinoBackground,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (totalWon > 0) "Total Won" else "Outcome",
                            fontSize = 12.sp,
                            color = CasinoTextSecondary
                        )
                        Text(
                            text = if (totalWon > 0) "+Rs $totalWon" else "Better Luck Next Round!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = if (totalWon > 0) CasinoGreen else CasinoTextMuted
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
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Continue Trading",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
private fun HowToPlayDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CasinoSurface,
        title = {
            Text(
                text = "Win Go 30s Presale Rules",
                fontWeight = FontWeight.Bold,
                color = CasinoGold
            )
        },
        text = {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "1. 30 seconds 1 issue, 25 seconds to order, 5 seconds to show the lottery result.\n\n" +
                            "2. Green: If result is 1, 3, 7, 9 get (2x). If 5, get (1.5x).\n\n" +
                            "3. Red: If result is 2, 4, 6, 8 get (2x). If 0, get (1.5x).\n\n" +
                            "4. Violet: If result is 0 or 5, get (4.5x).\n\n" +
                            "5. Number (0-9): Exact match pays 9x.\n\n" +
                            "6. Big (5-9) & Small (0-4): Exact match pays 2x.\n\n" +
                            "7. Provably Fair RNG hashes are pre-generated deterministically.",
                    fontSize = 12.sp,
                    color = CasinoTextPrimary,
                    lineHeight = 17.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "I Understand", color = CasinoGold, fontWeight = FontWeight.Bold)
            }
        }
    )
}
