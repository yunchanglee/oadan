package kr.co.oadan.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.oadan.MainActivity
import kr.co.oadan.R
import kr.co.oadan.data.local.OadanDatabase
import kr.co.oadan.data.repository.FastingRepository
import kr.co.oadan.domain.FastTimerEngine
import kr.co.oadan.service.FastingActionReceiver
import kr.co.oadan.service.FastingNotificationManager

class FastingAppWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val database = OadanDatabase.getDatabase(context)
        val fastingRepository = FastingRepository(database.fastSessionDao())

        CoroutineScope(Dispatchers.IO).launch {
            val active = fastingRepository.getActiveSessionOnce()
            val now = System.currentTimeMillis()

            for (widgetId in appWidgetIds) {
                val views2x2 = RemoteViews(context.packageName, R.layout.widget_2x2)
                val views4x2 = RemoteViews(context.packageName, R.layout.widget_4x2)

                // 클릭 시 메인 앱 실행
                val appIntent = Intent(context, MainActivity::class.java)
                val appPendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    appIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views2x2.setOnClickPendingIntent(R.id.widget_2x2_root, appPendingIntent)
                views4x2.setOnClickPendingIntent(R.id.widget_4x2_root, appPendingIntent)

                // 수분 +250ml 인텐트
                val waterIntent = Intent(context, FastingActionReceiver::class.java).apply {
                    action = FastingNotificationManager.ACTION_ADD_WATER_250
                }
                val waterPending = PendingIntent.getBroadcast(
                    context,
                    10,
                    waterIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views4x2.setOnClickPendingIntent(R.id.widget_btn_water, waterPending)

                if (active != null) {
                    val state = FastTimerEngine.calculateState(
                        startTimeUtc = active.startTimeUtc,
                        targetEndTimeUtc = active.targetEndTimeUtc,
                        currentTimeUtc = now
                    )

                    val elapsedStr = FastTimerEngine.formatFullHoursMinutes(state.elapsedMillis)
                    val remainingStr = if (state.remainingMillis > 0) {
                        "목표까지 ${FastTimerEngine.formatRemainingKorean(state.remainingMillis)}"
                    } else {
                        "목표 달성!"
                    }

                    // 2x2 업데이트
                    views2x2.setTextViewText(R.id.widget_2x2_stage_emoji, state.currentStage.emoji)
                    views2x2.setTextViewText(R.id.widget_2x2_elapsed, elapsedStr)
                    views2x2.setTextViewText(R.id.widget_2x2_remaining, remainingStr)

                    // 4x2 업데이트
                    views4x2.setTextViewText(
                        R.id.widget_4x2_stage_title,
                        "${state.currentStage.emoji} ${state.currentStage.stageName} · ${active.protocol}"
                    )
                    views4x2.setTextViewText(R.id.widget_4x2_elapsed, elapsedStr)
                    views4x2.setTextViewText(
                        R.id.widget_4x2_remaining,
                        "${state.targetEndTimeFormatted} 종료 · $remainingStr"
                    )

                    // 전체 목표 대비 진행률 (16:8 외 프로토콜도 정상 비율 반영)
                    val progressInt = (state.progressRatio * 100).toInt().coerceIn(0, 100)
                    views4x2.setProgressBar(R.id.widget_4x2_progress, 100, progressInt, false)
                    views4x2.setTextViewText(R.id.widget_btn_toggle, "공복 마치기")

                    val stopIntent = Intent(context, FastingActionReceiver::class.java).apply {
                        action = FastingNotificationManager.ACTION_STOP_FASTING
                    }
                    val stopPending = PendingIntent.getBroadcast(
                        context,
                        20,
                        stopIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    views4x2.setOnClickPendingIntent(R.id.widget_btn_toggle, stopPending)

                } else {
                    // 대기 상태
                    views2x2.setTextViewText(R.id.widget_2x2_stage_emoji, "💧")
                    views2x2.setTextViewText(R.id.widget_2x2_elapsed, "공복 준비")
                    views2x2.setTextViewText(R.id.widget_2x2_remaining, "탭하여 시작")

                    views4x2.setTextViewText(R.id.widget_4x2_stage_title, "오아단 · 나만의 오아시스")
                    views4x2.setTextViewText(R.id.widget_4x2_elapsed, "휴식 중")
                    views4x2.setTextViewText(R.id.widget_4x2_remaining, "탭하여 오늘의 공복을 시작하세요")
                    views4x2.setProgressBar(R.id.widget_4x2_progress, 100, 0, false)
                    views4x2.setTextViewText(R.id.widget_btn_toggle, "공복 시작")
                    views4x2.setOnClickPendingIntent(R.id.widget_btn_toggle, appPendingIntent)
                }

                val options = appWidgetManager.getAppWidgetOptions(widgetId)
                val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 140)
                if (minWidth >= 220) {
                    appWidgetManager.updateAppWidget(widgetId, views4x2)
                } else {
                    appWidgetManager.updateAppWidget(widgetId, views2x2)
                }
            }
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        newOptions: android.os.Bundle
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        onUpdate(context, appWidgetManager, intArrayOf(appWidgetId))
    }

    companion object {
        fun updateAllWidgets(context: Context) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, FastingAppWidgetProvider::class.java)
            val ids = appWidgetManager.getAppWidgetIds(componentName)
            if (ids.isNotEmpty()) {
                val intent = Intent(context, FastingAppWidgetProvider::class.java).apply {
                    action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                }
                context.sendBroadcast(intent)
            }
        }
    }
}
