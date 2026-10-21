package com.example

import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testGameEngineProvablyFairDeterministic() {
    val res1 = com.example.data.GameEngine.calculateRoundResult("20261021000100", "SEED1")
    val res2 = com.example.data.GameEngine.calculateRoundResult("20261021000100", "SEED1")
    assertEquals(res1.number, res2.number)
    assertEquals(res1.colorName, res2.colorName)
    assertEquals(res1.isBig, res2.isBig)
  }

  @Test
  fun testNumberBetMultiplier() {
    val result = com.example.data.model.PeriodHistoryItem(
      periodId = "20261021000101",
      number = 7,
      isBig = true,
      primaryColor = androidx.compose.ui.graphics.Color.Green,
      colorName = "Green"
    )
    val bet = com.example.data.model.ActiveBet(
      id = "B1",
      periodId = "20261021000101",
      choice = com.example.data.model.BetChoiceType.NUMBER_7,
      amount = 100L,
      multiplier = 1,
      totalBet = 100L
    )
    val payout = com.example.data.GameEngine.evaluateBet(bet, result)
    assertEquals(900L, payout) // 9x multiplier
  }
}
