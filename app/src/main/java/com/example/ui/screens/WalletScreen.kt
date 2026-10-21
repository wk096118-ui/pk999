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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentGateway
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
fun WalletScreen(
    viewModel: PK999ViewModel,
    initialTab: Int = 0
) {
    val user by viewModel.userProfile.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(CasinoBackground)
            .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(top = 10.dp, bottom = 90.dp)
    ) {
        // 1. Balance Summary Card
        item {
            WalletBalanceOverviewCard(user = user)
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 2. 29x Turnover Requirement Engine Card
        item {
            TurnoverRequirementCard(
                required = user.totalTurnoverRequired,
                completed = user.currentTurnoverCompleted
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 3. Locked Welcome Bonus 999 Coins Banner
        item {
            LockedWelcomeRewardBanner(
                lockedBonus = user.lockedBonus,
                isUnlocked = user.isWelcomeBonusUnlocked
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. Deposit / Withdraw / History Tabs
        item {
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
                    onClick = { selectedTab = 0 },
                    text = { Text("Deposit", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Withdraw", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Transactions", fontWeight = FontWeight.Bold) }
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 5. Tab Content
        when (selectedTab) {
            0 -> {
                item {
                    DepositSection(
                        onDepositSubmit = { gateway, amount, txId ->
                            viewModel.deposit(gateway, amount, txId)
                        }
                    )
                }
            }
            1 -> {
                item {
                    WithdrawSection(
                        balance = user.balance,
                        requiredTurnover = user.totalTurnoverRequired,
                        completedTurnover = user.currentTurnoverCompleted,
                        onWithdrawSubmit = { gateway, amount, account ->
                            viewModel.withdraw(gateway, amount, account)
                        }
                    )
                }
            }
            2 -> {
                item {
                    TransactionHistorySection(transactions = transactions)
                }
            }
        }
    }
}

@Composable
private fun WalletBalanceOverviewCard(user: com.example.data.model.UserProfile) {
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
                    text = "PK999 MASTER WALLET",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = CasinoGold
                )
                Text(
                    text = "ID: ${user.uid}",
                    fontSize = 11.sp,
                    color = CasinoTextSecondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Rs ${user.balance}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = CasinoGold
            )
            Text(
                text = "Total Available Withdrawable Balance",
                fontSize = 12.sp,
                color = CasinoTextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Total Deposited", fontSize = 11.sp, color = CasinoTextSecondary)
                    Text(text = "Rs ${user.totalDeposited}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CasinoGreen)
                }
                Column {
                    Text(text = "Total Withdrawn", fontSize = 11.sp, color = CasinoTextSecondary)
                    Text(text = "Rs ${user.totalWithdrawn}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CasinoRed)
                }
                Column {
                    Text(text = "Bonus Vault", fontSize = 11.sp, color = CasinoTextSecondary)
                    Text(text = "${if (user.isWelcomeBonusUnlocked) 0 else user.lockedBonus} Coins", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CasinoGold)
                }
            }
        }
    }
}

@Composable
private fun TurnoverRequirementCard(required: Long, completed: Long) {
    val progress = if (required <= 0) 1.0f else (completed.toFloat() / required.toFloat()).coerceIn(0f, 1f)
    val percentage = (progress * 100).toInt()
    val isFulfilled = completed >= required

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isFulfilled) CasinoGreen.copy(alpha = 0.5f) else CasinoBorder, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isFulfilled) Icons.Default.CheckCircle else Icons.Default.Lock,
                        contentDescription = "Turnover",
                        tint = if (isFulfilled) CasinoGreen else CasinoGold,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "29X WAGER / TURNOVER ENGINE",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isFulfilled) CasinoGreen else CasinoGold
                    )
                }

                Text(
                    text = "$percentage%",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isFulfilled) CasinoGreen else CasinoGold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (isFulfilled) CasinoGreen else CasinoGold,
                trackColor = CasinoBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Completed: Rs $completed",
                    fontSize = 11.sp,
                    color = CasinoTextSecondary
                )
                Text(
                    text = "Target (29x): Rs $required",
                    fontSize = 11.sp,
                    color = CasinoTextSecondary
                )
            }

            if (!isFulfilled && required > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Note: Complete Rs ${required - completed} more in bets to enable 100% automated cashouts.",
                    fontSize = 10.sp,
                    color = CasinoGoldDark
                )
            }
        }
    }
}

@Composable
private fun LockedWelcomeRewardBanner(lockedBonus: Long, isUnlocked: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isUnlocked) CasinoGreen.copy(alpha = 0.15f) else CasinoBackground,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isUnlocked) CasinoGreen else CasinoGold.copy(alpha = 0.4f),
                RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(if (isUnlocked) CasinoGreen.copy(alpha = 0.2f) else CasinoGold.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isUnlocked) Icons.Default.LockOpen else Icons.Default.Lock,
                    contentDescription = "Bonus Lock",
                    tint = if (isUnlocked) CasinoGreen else CasinoGold,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = if (isUnlocked) "999 Coins Welcome Bonus Unlocked!" else "999 Coins Locked Welcome Vault",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isUnlocked) CasinoGreen else CasinoGold
                )
                Text(
                    text = if (isUnlocked)
                        "Credited directly into your active game balance."
                    else
                        "Automatically unlocks on your first deposit of minimum Rs 200.",
                    fontSize = 11.sp,
                    color = CasinoTextSecondary
                )
            }
        }
    }
}

@Composable
private fun DepositSection(
    onDepositSubmit: (PaymentGateway, Long, String) -> Unit
) {
    val gateways = listOf(
        PaymentGateway.JAZZCASH_DIRECT,
        PaymentGateway.EASYPAISA_DIRECT,
        PaymentGateway.EASYPAISA_RAAST,
        PaymentGateway.USDT_TRC20
    )
    var selectedGateway by remember { mutableStateOf(gateways[0]) }
    val amounts = listOf(200L, 500L, 1000L, 2000L, 5000L, 10000L, 50000L)
    var selectedAmount by remember { mutableLongStateOf(500L) }
    var accountOrPhone by remember { mutableStateOf("03001234567") }
    var transactionIdInput by remember { mutableStateOf("") }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CasinoBorder, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "1. Select Payment Channel",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CasinoGold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                gateways.forEach { gateway ->
                    val isSelected = selectedGateway == gateway
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) CasinoSurfaceElevated else CasinoBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedGateway = gateway }
                            .border(
                                1.dp,
                                if (isSelected) CasinoGold else CasinoBorder,
                                RoundedCornerShape(10.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = when (gateway) {
                                        PaymentGateway.JAZZCASH_DIRECT -> Icons.Default.PhoneAndroid
                                        PaymentGateway.EASYPAISA_DIRECT -> Icons.Default.CreditCard
                                        PaymentGateway.EASYPAISA_RAAST -> Icons.Default.QrCode2
                                        PaymentGateway.USDT_TRC20 -> Icons.Default.CurrencyBitcoin
                                    },
                                    contentDescription = gateway.displayName,
                                    tint = if (isSelected) CasinoGold else CasinoTextSecondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = gateway.displayName,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = CasinoTextPrimary
                                )
                            }
                            Text(
                                text = gateway.tag,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) CasinoGreen else CasinoTextMuted
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "2. Select Deposit Amount (PKR)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CasinoGold
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(amounts) { amt ->
                    val isSelected = selectedAmount == amt
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) CasinoGold else CasinoBackground,
                        modifier = Modifier
                            .clickable { selectedAmount = amt }
                            .border(
                                1.dp,
                                if (isSelected) CasinoGold else CasinoBorder,
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Text(
                            text = "Rs $amt",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) CasinoBackground else CasinoTextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Account details input
            OutlinedTextField(
                value = accountOrPhone,
                onValueChange = { accountOrPhone = it },
                label = { Text("Sender Mobile / Account Number") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CasinoGold,
                    unfocusedBorderColor = CasinoBorder,
                    focusedTextColor = CasinoTextPrimary,
                    unfocusedTextColor = CasinoTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("deposit_phone_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = transactionIdInput,
                onValueChange = { transactionIdInput = it },
                label = { Text("Transaction ID / TID (Optional or Auto)") },
                placeholder = { Text("e.g. 2026102148921") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CasinoGold,
                    unfocusedBorderColor = CasinoBorder,
                    focusedTextColor = CasinoTextPrimary,
                    unfocusedTextColor = CasinoTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("deposit_tid_input")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onDepositSubmit(selectedGateway, selectedAmount, transactionIdInput)
                    transactionIdInput = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CasinoGreen,
                    contentColor = CasinoBackground
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_deposit_submit_button")
            ) {
                Text(
                    text = "Deposit Rs $selectedAmount via ${selectedGateway.displayName}",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun WithdrawSection(
    balance: Long,
    requiredTurnover: Long,
    completedTurnover: Long,
    onWithdrawSubmit: (PaymentGateway, Long, String) -> Unit
) {
    var withdrawAmountStr by remember { mutableStateOf("500") }
    var accountTitle by remember { mutableStateOf("Younas Khan") }
    var accountNumber by remember { mutableStateOf("03001234567") }
    val gateways = listOf(PaymentGateway.JAZZCASH_DIRECT, PaymentGateway.EASYPAISA_DIRECT, PaymentGateway.USDT_TRC20)
    var selectedGateway by remember { mutableStateOf(gateways[0]) }

    val amount = withdrawAmountStr.toLongOrNull() ?: 0L
    val commission = (amount * 0.05).toLong()
    val netAmount = (amount - commission).coerceAtLeast(0L)
    val isTurnoverMet = completedTurnover >= requiredTurnover

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CasinoBorder, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Fast-Action Cash Out",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CasinoGold
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Channel selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                gateways.forEach { g ->
                    val isSelected = selectedGateway == g
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) CasinoGold else CasinoBackground,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedGateway = g }
                            .border(1.dp, if (isSelected) CasinoGold else CasinoBorder, RoundedCornerShape(8.dp))
                    ) {
                        Text(
                            text = g.displayName.split(" ")[0],
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) CasinoBackground else CasinoTextPrimary,
                            modifier = Modifier.padding(vertical = 10.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = accountTitle,
                onValueChange = { accountTitle = it },
                label = { Text("Account Title (Full Name)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CasinoGold,
                    unfocusedBorderColor = CasinoBorder,
                    focusedTextColor = CasinoTextPrimary,
                    unfocusedTextColor = CasinoTextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Account Number / IBAN / Wallet") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CasinoGold,
                    unfocusedBorderColor = CasinoBorder,
                    focusedTextColor = CasinoTextPrimary,
                    unfocusedTextColor = CasinoTextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = withdrawAmountStr,
                onValueChange = { withdrawAmountStr = it },
                label = { Text("Withdraw Amount (PKR)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CasinoGold,
                    unfocusedBorderColor = CasinoBorder,
                    focusedTextColor = CasinoTextPrimary,
                    unfocusedTextColor = CasinoTextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fee and Net Calculation Box (Admin 5% commission engine)
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = CasinoBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Requested Amount:", fontSize = 12.sp, color = CasinoTextSecondary)
                        Text("Rs $amount", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CasinoTextPrimary)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Platform Commission (5%):", fontSize = 12.sp, color = CasinoTextSecondary)
                        Text("-Rs $commission", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CasinoRed)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Net Cash-Out Amount:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CasinoGold)
                        Text("Rs $netAmount", fontSize = 14.sp, fontWeight = FontWeight.Black, color = CasinoGreen)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (!isTurnoverMet && requiredTurnover > 0) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = CasinoRed.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning", tint = CasinoRed)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "29x Turnover requirement incomplete (Rs ${requiredTurnover - completedTurnover} remaining).",
                            fontSize = 11.sp,
                            color = CasinoRed
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = {
                    onWithdrawSubmit(selectedGateway, amount, "$accountNumber ($accountTitle)")
                },
                enabled = amount > 0 && amount <= balance && isTurnoverMet,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CasinoGold,
                    contentColor = CasinoBackground
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("confirm_withdraw_submit_button")
            ) {
                Text(
                    text = if (amount > balance) "Exceeds Available Balance"
                    else if (!isTurnoverMet && requiredTurnover > 0) "Turnover Required First"
                    else "Submit Withdrawal (Rs $amount)",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun TransactionHistorySection(transactions: List<com.example.data.model.TransactionRecord>) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = CasinoSurface,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CasinoBorder, RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "Transaction History",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = CasinoGold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                transactions.forEach { tx ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CasinoBackground,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tx.gatewayOrNote,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CasinoTextPrimary
                                )
                                Text(
                                    text = "ID: ${tx.id} • ${tx.type}",
                                    fontSize = 11.sp,
                                    color = CasinoTextSecondary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (tx.type.contains("DEPOSIT") || tx.type.contains("WIN") || tx.type.contains("BONUS"))
                                        "+Rs ${tx.amount}"
                                    else
                                        "-Rs ${tx.amount}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (tx.type.contains("DEPOSIT") || tx.type.contains("WIN")) CasinoGreen else CasinoRed
                                )
                                Text(
                                    text = tx.status,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (tx.status == "SUCCESS") CasinoGreen else CasinoGold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
