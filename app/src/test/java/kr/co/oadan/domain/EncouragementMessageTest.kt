package kr.co.oadan.domain

import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EncouragementMessageTest {

    @Test
    fun testMidFastMessageReturnsValidContent() {
        for (hours in 0..24) {
            val cheer = EncouragementMessageProvider.getMidFastMessage(hours)
            assertNotNull(cheer)
            assertFalse("타이틀이 비어있지 않아야 함", cheer.title.isBlank())
            assertFalse("메시지 본문이 비어있지 않아야 함", cheer.message.isBlank())
            assertFalse("이모지가 비어있지 않아야 함", cheer.emoji.isBlank())
            assertTrue("해요체 톤앤매너 유지", cheer.message.endsWith("요.") || cheer.message.endsWith("해요."))
        }
    }

    @Test
    fun testOverachievedMessageReturnsCongratulatoryContent() {
        for (overHours in 0..10) {
            val cheer = EncouragementMessageProvider.getOverachievedMessage(overHours)
            assertNotNull(cheer)
            assertFalse("초과 달성 타이틀이 비어있지 않아야 함", cheer.title.isBlank())
            assertFalse("초과 달성 메시지가 비어있지 않아야 함", cheer.message.isBlank())
            assertTrue(
                "보식 또는 달성 관련 단어 포함",
                cheer.title.contains("목표") || cheer.title.contains("놀라운") ||
                    cheer.title.contains("오아시스") || cheer.title.contains("무리하지") ||
                    cheer.message.contains("보식") || cheer.message.contains("달성") || cheer.message.contains("식사")
            )
        }
    }

    @Test
    fun testRandomCheerReturnsNonNull() {
        val random = EncouragementMessageProvider.getRandomMidFastCheer()
        assertNotNull(random)
        assertTrue(random.title.isNotBlank())
    }
}
