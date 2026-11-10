package kr.co.oadan.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.oadan.data.model.ConditionLog
import kr.co.oadan.data.model.FastSession
import kr.co.oadan.data.model.MealLog
import kr.co.oadan.data.model.Recipe
import kr.co.oadan.data.model.WaterLog

@Database(
    entities = [
        FastSession::class,
        WaterLog::class,
        ConditionLog::class,
        MealLog::class,
        Recipe::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OadanDatabase : RoomDatabase() {
    abstract fun fastSessionDao(): FastSessionDao
    abstract fun waterLogDao(): WaterLogDao
    abstract fun conditionLogDao(): ConditionLogDao
    abstract fun mealLogDao(): MealLogDao
    abstract fun recipeDao(): RecipeDao

    companion object {
        @Volatile
        private var INSTANCE: OadanDatabase? = null

        fun getDatabase(context: Context): OadanDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    OadanDatabase::class.java,
                    "oadan_wellness.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate initial recipes
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.recipeDao()?.insertAll(InitialRecipes.list)
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object InitialRecipes {
    val list = listOf(
        Recipe(
            id = "rc_soft_01",
            title = "두부 채소 비빔 볼 & 맑은 된장국",
            subtitle = "부드러운 단백질과 식이섬유의 완벽한 조화",
            stage = "부드러운 보식",
            emoji = "🥗",
            prepTimeMinutes = 15,
            glycemicFriendly = "낮음",
            tags = listOf("비건", "제철배추", "고단백", "저당"),
            eatOrderGuide = "🥬 채소 먼저 → 🧈 두부 단백질 → 🍠 고구마/단호박",
            ingredients = listOf("부침용 유기농 두부 1/2모", "어린잎 채소 한 줌", "제철 배추 2장", "맑은 재래된장 1스푼", "들기름 1작은술"),
            instructions = listOf(
                "두부를 깍둑썰기하여 들기름을 두른 팬에 노릇하게 구워줍니다.",
                "어린잎 채소와 배추를 깨끗이 씻어 한 입 크기로 찢어 볼에 담습니다.",
                "따뜻한 물 300ml에 재래된장을 연하게 풀어 맑은 된장국을 끓입니다.",
                "구운 두부를 채소 위에 올리고 들기름을 살짝 둘러 완성합니다."
            ),
            seasonInfo = "가을·겨울 제철 배추는 달고 수분이 풍부해요"
        ),
        Recipe(
            id = "rc_soft_02",
            title = "단호박 수프",
            subtitle = "속을 부드럽게 감싸주는 자연의 단맛",
            stage = "부드러운 보식",
            emoji = "🍠",
            prepTimeMinutes = 10,
            glycemicFriendly = "보통",
            tags = listOf("비건", "글루텐프리", "소화편안"),
            eatOrderGuide = "따뜻하게 데워 한 스푼씩 30초 이상 음미하며 드세요",
            ingredients = listOf("찐 미니 단호박 1개", "무가당 아몬드 밀크 150ml", "소금 한 꼬집", "볶은 호박씨 약간"),
            instructions = listOf(
                "단호박 껍질을 벗겨 적당한 크기로 썹니다.",
                "블렌더에 단호박과 아몬드 밀크를 넣고 곱게 갈아줍니다.",
                "냄비에 부어 약불에서 3분간 데운 뒤 소금 한 꼬집을 넣습니다.",
                "그릇에 담고 볶은 호박씨를 고명으로 올립니다."
            ),
            seasonInfo = "제철 미니 단호박은 베타카로틴이 풍부해요"
        ),
        Recipe(
            id = "rc_soft_03",
            title = "현미 채소죽",
            subtitle = "거칠지 않게 곱게 쑨 곡물의 든든함",
            stage = "부드러운 보식",
            emoji = "🥣",
            prepTimeMinutes = 20,
            glycemicFriendly = "낮음",
            tags = listOf("비건", "자연식", "속편한"),
            eatOrderGuide = "따뜻한 죽을 천천히 씹어 침과 잘 섞이도록 드세요",
            ingredients = listOf("불린 발아현미밥 1공기", "당근 1/4개", "표고버섯 1개", "다시마 채수 500ml", "참기름 약간"),
            instructions = listOf(
                "표고버섯과 당근을 잘게 다집니다.",
                "냄비에 참기름을 살짝 두르고 채소를 볶다가 현미밥을 넣습니다.",
                "다시마 채수를 붓고 중약불에서 쌀알이 부드럽게 퍼질 때까지 저어가며 끓입니다.",
                "맑은 소금으로 약하게 간하여 완성합니다."
            ),
            seasonInfo = "발아현미는 소화 흡수가 부드럽고 가바(GABA) 성분이 풍부해요"
        ),
        Recipe(
            id = "rc_gentle_01",
            title = "맑은 미역 연두부 탕",
            subtitle = "18시간 이상 공복 후 위벽을 보호하는 순한 한 끼",
            stage = "순한 보식",
            emoji = "🍲",
            prepTimeMinutes = 12,
            glycemicFriendly = "낮음",
            tags = listOf("비건", "저탄수", "미네랄풍부"),
            eatOrderGuide = "맑은 국물을 먼저 마신 뒤 연두부를 부드럽게 떠드세요",
            ingredients = listOf("불린 자른미역 반 줌", "생식용 연두부 1팩", "맑은 국간장 반 스푼", "다시마 물 400ml"),
            instructions = listOf(
                "불린 미역의 물기를 꼭 짜고 참기름 없이 다시마 물에 바로 넣고 끓입니다.",
                "국물이 끓어오르면 연두부를 수저로 크게 떠 넣습니다.",
                "맑은 국간장으로 아주 슴슴하게 간을 맞춥니다."
            )
        ),
        Recipe(
            id = "rc_normal_01",
            title = "제철 나물 3종과 구운 두부 현미밥",
            subtitle = "풍성한 영양소와 고소한 풍미의 균형 밥상",
            stage = "일반 보식",
            emoji = "🍱",
            prepTimeMinutes = 20,
            glycemicFriendly = "낮음",
            tags = listOf("비건", "고식이섬유", "정갈한한식"),
            eatOrderGuide = "🥬 나물 반찬 먼저 충분히 → 🧈 구운 두부 → 🍚 현미밥 반 공기",
            ingredients = listOf("시금치나물", "고사리나물", "콩나물", "들기름 구운 두부 3조각", "현미귀리밥 반 공기"),
            instructions = listOf(
                "자연식으로 무친 제철 나물 3종을 준비합니다.",
                "두부를 들기름에 구워 바삭하고 담백하게 익힙니다.",
                "나물과 두부를 천천히 드신 후 현미밥을 곁들입니다."
            )
        ),
        Recipe(
            id = "rc_recovery_01",
            title = "1단계 맑은 채수 & 발효 요거트",
            subtitle = "24시간 이상 긴 공복 후 장내 미생물을 깨우는 시동 식단",
            stage = "회복 보식",
            emoji = "🍵",
            prepTimeMinutes = 5,
            glycemicFriendly = "낮음",
            tags = listOf("회복1단계", "유익균", "위장보호"),
            eatOrderGuide = "따뜻한 채수를 먼저 반 컵 천천히 마시고, 15분 후 요거트를 드세요",
            ingredients = listOf("따뜻한 무·배추 다시마 채수 200ml", "무가당 플레인 요거트 100g"),
            instructions = listOf(
                "따뜻하게 데운 채수를 차처럼 천천히 음미합니다.",
                "속이 편안해지면 15~30분 뒤 무가당 요거트를 섭취합니다.",
                "1시간 이후 부드러운 죽이나 순한 보식으로 2단계를 진행합니다."
            )
        )
    )
}
