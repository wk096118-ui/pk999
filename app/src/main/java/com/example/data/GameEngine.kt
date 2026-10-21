package com.example.data

import com.example.data.model.ActiveBet
import com.example.data.model.BetChoiceType
import com.example.data.model.PeriodHistoryItem
import com.example.ui.theme.CasinoGreen
import com.example.ui.theme.CasinoRed
import com.example.ui.theme.CasinoViolet
import java.security.MessageDigest
import kotlin.random.Random

object GameEngine {

    fun generatePeriodId(counter: Long): String {
        val todayStr = "20261021"
        return "$todayStr${counter.toString().padStart(6, '0')}"
    }

    /**
     * Provably fair deterministic RNG calculation
     */
    fun calculateRoundResult(periodId: String, clientSeed: String = "PK999_PROVABLY_FAIR"): PeriodHistoryItem {
        val combined = "$periodId:$clientSeed:91CLUB_ENGINE"
        val hash = sha256(combined)
        // Take first 4 bytes hex to determine 0-9 number
        val subHex = hash.take(8)
        val numLong = subHex.toLong(16)
        val number = (numLong % 10).toInt()

        val isBig = number >= 5
        val (pColor, sColor, colorName) = when (number) {
            0 -> Triple(CasinoRed, CasinoViolet, "Red+Violet")
            5 -> Triple(CasinoGreen, CasinoViolet, "Green+Violet")
            1, 3, 7, 9 -> Triple(CasinoGreen, null, "Green")
            else -> Triple(CasinoRed, null, "Red") // 2, 4, 6, 8
        }

        return PeriodHistoryItem(
            periodId = periodId,
            number = number,
            isBig = isBig,
            primaryColor = pColor,
            secondaryColor = sColor,
            colorName = colorName,
            serverHash = hash
        )
    }

    /**
     * Evaluates a bet against the winning round result.
     * Returns total payout (including stake). Returns 0 if lost.
     */
    fun evaluateBet(bet: ActiveBet, result: PeriodHistoryItem): Long {
        val totalBet = bet.totalBet
        val winMultiplier: Float = when (bet.choice) {
            BetChoiceType.GREEN -> {
                when (result.number) {
                    1, 3, 7, 9 -> 2.0f
                    5 -> 1.5f // Half win on dual
                    else -> 0f
                }
            }
            BetChoiceType.RED -> {
                when (result.number) {
                    2, 4, 6, 8 -> 2.0f
                    0 -> 1.5f // Half win on dual
                    else -> 0f
                }
            }
            BetChoiceType.VIOLET -> {
                if (result.number == 0 || result.number == 5) 4.5f else 0f
            }
            BetChoiceType.NUMBER_0 -> if (result.number == 0) 9.0f else 0f
            BetChoiceType.NUMBER_1 -> if (result.number == 1) 9.0f else 0f
            BetChoiceType.NUMBER_2 -> if (result.number == 2) 9.0f else 0f
            BetChoiceType.NUMBER_3 -> if (result.number == 3) 9.0f else 0f
            BetChoiceType.NUMBER_4 -> if (result.number == 4) 9.0f else 0f
            BetChoiceType.NUMBER_5 -> if (result.number == 5) 9.0f else 0f
            BetChoiceType.NUMBER_6 -> if (result.number == 6) 9.0f else 0f
            BetChoiceType.NUMBER_7 -> if (result.number == 7) 9.0f else 0f
            BetChoiceType.NUMBER_8 -> if (result.number == 8) 9.0f else 0f
            BetChoiceType.NUMBER_9 -> if (result.number == 9) 9.0f else 0f
            BetChoiceType.BIG -> if (result.isBig) 2.0f else 0f
            BetChoiceType.SMALL -> if (!result.isBig) 2.0f else 0f
        }

        return (totalBet * winMultiplier).toLong()
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun generateInitialHistory(): List<PeriodHistoryItem> {
        val list = mutableListOf<PeriodHistoryItem>()
        var baseCounter = 30080L
        for (i in 0 until 12) {
            val period = generatePeriodId(baseCounter + i)
            list.add(calculateRoundResult(period, "SEED_INIT_$i"))
        }
        return list.reversed()
    }
}
